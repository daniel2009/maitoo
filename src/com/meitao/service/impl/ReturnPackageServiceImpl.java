package com.meitao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.ReturnPackageDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.ReturnPackageService;
import com.meitao.service.UserService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PageSplitUtil;
import com.meitao.common.util.PaymentUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ReturnPackage;

import com.meitao.model.User;

@Service("returnPackageService")
public class ReturnPackageServiceImpl implements ReturnPackageService{
	@Autowired
	private ReturnPackageDao returnPackageDao;
	@Resource(name = "userService")
	private UserService userService; 
	@Autowired
	private globalargsDao globalargsDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private TranshipmentBillDao transhipmentBillDao;
	@Override
	public ResponseObject<PageSplit<ReturnPackage>> findAll(int pageIndex, int pageSize)
			throws ServiceException {
		ResponseObject<PageSplit<ReturnPackage>> responseObject = new ResponseObject<PageSplit<ReturnPackage>>();
		try{
			int rowCount = this.returnPackageDao.countAll();
			if(rowCount > 0){
				PageSplit<ReturnPackage> pageSplit = new PageSplit<ReturnPackage>();
				int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
				List<ReturnPackage> list = returnPackageDao.findAll(firstResult, pageSize);
				pageSplit.setDatas(list);
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(pageSplit);
			}else{
				responseObject.setCode(ResponseCode.RETURN_PACKAGE__EMPTY_LIST);
				responseObject.setMessage("没有退货信息");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("return package find by admin fail", e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<PageSplit<ReturnPackage>> findByUser(String userId, String state, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<ReturnPackage>> responseObject = new ResponseObject<PageSplit<ReturnPackage>>();
		if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				int rowCount = this.returnPackageDao.countByKey(null, null, null, userId, state, null, null);
				if(rowCount > 0){
					PageSplit<ReturnPackage> pageSplit = new PageSplit<ReturnPackage>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
					List<ReturnPackage> list = returnPackageDao.searchByKey(null, null, null, userId, state, null, null, firstResult, pageSize);
					pageSplit.setDatas(list);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE__EMPTY_LIST);
					responseObject.setMessage("对应的列表信息为空");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("return package find by user fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> add(ReturnPackage returnPackage)
			throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(returnPackage == null){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			returnPackage.setCreateDate(DateUtil.date2String(new Date()));
			returnPackage.setModifyDate(DateUtil.date2String(new Date()));
			returnPackage.setState(Constant.RETURN_PACKAGE_STATE_NOT_TREAT);
			try{
				int result1 = this.returnPackageDao.insert(returnPackage);
				int result2 = this.transhipmentBillDao.updateState(returnPackage.getTranshipmentId(), Constant.TRANSHIPMENT_STATE_9);
				if(result1 > 0 && result2 > 0){
					
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE_INSERT_ERROR);
					responseObject.setMessage("添加退回包裹数据出现异常");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("add return package data fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> audit(String id, String empId, String state, String empRemark, String empExpressNo, String empExpressCompany, String empPic, String shippingFee) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(state)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				ReturnPackage returnPackage = this.returnPackageDao.findById(id);
				//有没有receipt都要算钱
				if(state.equals(Constant.RETURN_PACKAGE_STATE_TREAT)){
					ResponseObject<User> responseUser = userService.getUserById(returnPackage.getUserId());
					String rateString = this.getCurUsaToCn();
					String returnFee = this.getRetrunFee();
					String paymentString = String.valueOf(StringUtil.string2Double(returnFee)+StringUtil.string2Double(shippingFee));
					if(PaymentUtil.calculatePayment(responseUser.getData().getUsdBalance(), responseUser.getData().getRmbBalance(), paymentString, rateString)==null){
						responseObject.setCode(ResponseCode.RETURN_PACKAGE_AUDIT_ERROR);
						responseObject.setMessage("账户余额不足");
						return responseObject;
					}else{
						AccountDetail accountDetail = new AccountDetail();
						Account account = new Account();
						PaymentUtil.setAccountAndAccountDetailByPayment(account, accountDetail, responseUser.getData(), paymentString, rateString, false, "退货费用");
						accountDetail.setEmpId(empId);
						accountDetailDao.insertAccountDetail(accountDetail);
						this.accountDao.modifyAccount(account);
					}
				}
				String date = DateUtil.date2String(new Date());
				int result = this.returnPackageDao.audit(id, empId, state, empRemark, empExpressNo, empExpressCompany, empPic, shippingFee, date);
				if(result >= 0 && state.equals(Constant.RETURN_PACKAGE_STATE_TREAT)){
					String transhipmentId = this.returnPackageDao.findById(id).getTranshipmentId();
					result = this.transhipmentBillDao.updateState(transhipmentId, Constant.TRANSHIPMENT_STATE_8);
				}
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE_AUDIT_ERROR);
					responseObject.setMessage("提交退货数据的审核结果失败");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("audit return package fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<ReturnPackage> findById(String id)
			throws ServiceException {
		ResponseObject<ReturnPackage> responseObject = new ResponseObject<ReturnPackage>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				ReturnPackage returnPackage = this.returnPackageDao.findById(id);
				if(returnPackage == null){
					responseObject.setCode(ResponseCode.RETURN_PACKAGE_FIND_BY_ID_ERROR);
					responseObject.setMessage("获取指定退货数据失败");
				}else{
					responseObject.setData(returnPackage);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("find by id of return package fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> modifyByUser(ReturnPackage returnPackage) throws ServiceException{
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(returnPackage.getId())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				returnPackage.setModifyDate(DateUtil.date2String(new Date()));
				returnPackage.setState(Constant.RETURN_PACKAGE_STATE_NOT_TREAT);
				int result = this.returnPackageDao.updateByUser(returnPackage);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE_UPDATE_BY_USER_ERROR);
					responseObject.setMessage("更新对应退货数据异常");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("modify return package by user fail", e);
			}
		}
		return responseObject;
	}
	private String getCurUsaToCn() throws ServiceException {
		try {
			String usatoch = this.globalargsDao.getcontentbyflag("cur_usa_cn");
			if ((usatoch != null) && (!usatoch.equalsIgnoreCase(""))) {
				return usatoch;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	private String getRetrunFee() throws ServiceException {
		try {
			String usatoch = this.globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_RETURN_PACKAGE_RETURN_FEE);
			if ((usatoch != null) && (!usatoch.equalsIgnoreCase(""))) {
				return usatoch;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public ResponseObject<PageSplit<ReturnPackage>> findByKey(String id, String key, String type, String state, String createDateStart, String createDateEnd, int pageIndex, int pageSize)
			throws ServiceException {
		ResponseObject<PageSplit<ReturnPackage>> responseObject = new ResponseObject<PageSplit<ReturnPackage>>();
		if(!StringUtil.isEmpty(type)){
			if(type.equals("1")){
				type = "user_id";
			}else if(type.equals("2")){
				type = "package_no";
			}else if(type.equals("3")){
				type = "address";
			}else{
				type = "";
			}
		}
		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			int rowCount = this.returnPackageDao.countByKey(id, key, type, null, state, createDateStart, createDateEnd);
			if (rowCount > 0) {
				PageSplit<ReturnPackage> pageSplit = new PageSplit<ReturnPackage>();
				int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
				List<ReturnPackage> list = this.returnPackageDao.searchByKey(id, key, type, null, state, createDateStart, createDateEnd, firstResult, pageSize);
				if (list != null && !list.isEmpty()) {
					pageSplit.setDatas(list);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE__EMPTY_LIST);
					responseObject.setMessage("获取退货列表");
				}
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> cancelByUser(String transhipmentId, String userId) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(transhipmentId) || StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				int result1 = this.transhipmentBillDao.cancelReturn(transhipmentId, userId);
				int result2 = this.returnPackageDao.deleteByTranshipmentIdAndUserId(transhipmentId, userId);
				if(result1 > 0 && result2 > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.RETURN_PACKAGE_UPDATE_BY_USER_ERROR);
					responseObject.setMessage("更新对应退货数据异常");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("modify return package by user fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<String[]> getAllStateCount() throws ServiceException {
		ResponseObject<String[]> responseObject = new ResponseObject<String[]>();
		try {
			String[] array = new String[6];
			int i = 0;
			//1
			array[i++] = "countReturnPackageState0";
			array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, null, Constant.RETURN_PACKAGE_STATE_NOT_TREAT, null, null));
			array[i++] = "countReturnPackageState1";
			array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, null, Constant.RETURN_PACKAGE_STATE_TREAT, null, null));
			array[i++] = "countReturnPackageState2";
			array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, null, Constant.RETURN_PACKAGE_STATE_ISSUE, null, null));
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(array);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
}
