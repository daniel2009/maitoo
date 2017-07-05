package com.meitao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.RenlingDao;
import com.meitao.dao.RenlingPersonDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.service.ClaimPackageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PageSplitUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

import com.meitao.model.TranshipmentBill;

@Service("claimPackageService")
public class ClaimPackageServiceImpl implements ClaimPackageService{
	@Autowired
	private RenlingDao renlingDao;
	@Autowired
	private RenlingPersonDao renlingPersonDao;
	@Autowired
	private TranshipmentBillDao transhipmentBillDao;

	@Override
	public ResponseObject<PageSplit<RenlingBaoguo>> findByUser(String userId, String nameCondition, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<RenlingBaoguo>> responseObject = new ResponseObject<PageSplit<RenlingBaoguo>>();
		if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			if(!StringUtil.isEmpty(nameCondition)){
				nameCondition = new StringBuilder("%").append(nameCondition).append("%").toString();
			}
			try{
				int rowCount = this.renlingDao.countByUser(userId, nameCondition);
				if(rowCount > 0){
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageIndex = Math.min(pageIndex, pageCount);
					int firstResult = (pageIndex - 1) * pageSize;
					PageSplit<RenlingBaoguo> pageSplit = new PageSplit<RenlingBaoguo>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageIndex);
					pageSplit.setPageSize(pageSize);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);
					List<RenlingBaoguo> list = renlingDao.findByUser(userId, nameCondition, firstResult, pageSize);
					pageSplit.setDatas(list);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setMessage("没有自己的认领包裹数据");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("claim package find by user fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<PageSplit<RenlingBaoguo>> findByNotClaimed(String nameCondition,
			int pageIndex, int pageSize) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseObject<PageSplit<RenlingBaoguo>> responseObject = new ResponseObject<PageSplit<RenlingBaoguo>>();
		if(!StringUtil.isEmpty(nameCondition)){
			nameCondition = new StringBuilder("%").append(nameCondition).append("%").toString();
		}
		try{
			int rowCount = this.renlingDao.countByNotClaimed(nameCondition);
			if(rowCount > 0){
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageIndex = Math.min(pageIndex, pageCount);
				int firstResult = (pageIndex - 1) * pageSize;
				PageSplit<RenlingBaoguo> pageSplit = new PageSplit<RenlingBaoguo>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageIndex);
				pageSplit.setPageSize(pageSize);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);
				List<RenlingBaoguo> list = renlingDao.findByNotClaimed(nameCondition, firstResult, pageSize);
				if(list.size() > 0){
					pageSplit.setDatas(list);
					responseObject.setData(pageSplit);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.CLAIM_PACKAGE_FIND_BY_NOT_CLAIMED_FAIL);
					responseObject.setMessage("获取认领包裹列表失败");
				}
			}else{
				responseObject.setMessage("没有可以认领的包裹");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("claim package find by not claimed fail", e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<PageSplit<RenlingBaoguo>> findByNotClaimedAndUser(String userId, String nameCondition,
			int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<RenlingBaoguo>> responseObject = new ResponseObject<PageSplit<RenlingBaoguo>>();
		if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			if(!StringUtil.isEmpty(nameCondition)){
				nameCondition = new StringBuilder("%").append(nameCondition).append("%").toString();
			}
			try{
				int rowCount = this.renlingDao.countByNotClaimedAndUser(userId, nameCondition);
				if(rowCount > 0){
					PageSplit<RenlingBaoguo> pageSplit = new PageSplit<RenlingBaoguo>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
					List<RenlingBaoguo> list = renlingDao.findByNotClaimedAndUser(userId, nameCondition, firstResult, pageSize);
					if(list.size() > 0){
						pageSplit.setDatas(list);
						responseObject.setData(pageSplit);
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.CLAIM_PACKAGE_FIND_BY_NOT_CLAIMED_FAIL);
						responseObject.setMessage("获取认领包裹列表失败");
					}
				}else{
					responseObject.setMessage("没有可以认领的包裹");
				}
				
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("claim package find by not claimed fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<RenlingBaoguo> getByIdWithClaimPerson(String id) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseObject<RenlingBaoguo> responseObject = new ResponseObject<RenlingBaoguo>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				RenlingBaoguo renlingBaoguo = this.renlingDao.getRenlingBillbyid(id);
				if(null == renlingBaoguo){
					responseObject.setCode(ResponseCode.CLAIM_PACKAGE_ID_NO_EXISTS);
					responseObject.setMessage("找不到需要审核的指定认领包裹数据");
				}else{
					responseObject.setData(renlingBaoguo);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("fail to get claim package info with claim person info", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> updateByAudit(String id, String state,
			String claimPersonId, String auditRemark) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(state) || StringUtil.isEmpty(claimPersonId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				if("1".equals(state)){//审核通过
					int result = this.renlingDao.updateByAuditPass(state, claimPersonId);
					if(result > 0){
						//帮用户录入转运信息
						TranshipmentBill transhipmentBill = new TranshipmentBill();
						transhipmentBill.setState(Constant.TRANSHIPMENT_STATE2);
						transhipmentBill.setTransitType("0");
						transhipmentBill.setCreateDate(DateUtil.date2String(new Date()));
						transhipmentBill.setModifyDate(DateUtil.date2String(new Date()));
						transhipmentBill.setTransitNo(claimPersonId);//如果有实体，有变量，貌似获取不到、借用这个传递吧
						result = transhipmentBillDao.addByClaimPackage(transhipmentBill);
						responseObject.setData(transhipmentBill.getId());
						this.renlingDao.updateTransitId(id, transhipmentBill.getId());
						auditRemark = "已加入转运列表，转运id：" + transhipmentBill.getId() + ";" + auditRemark;
						result = this.renlingPersonDao.updateStateAndRemark(claimPersonId, state, auditRemark);
						if(result > 0){
							auditRemark = "该包裹已被其他人领走";
							state = Constant.CLAIM_PERSON_STATE_INVALID;
							this.renlingPersonDao.updateOtherByAudit(id, claimPersonId, state, auditRemark);//claimPackageId, passed claimPersonId, not passed remark
							responseObject.setCode(ResponseCode.SUCCESS_CODE);
						}else{
							responseObject.setCode(ResponseCode.CLAIM_PERSON_UPDATE_REMARK_FAIL);
							responseObject.setMessage("审核更新失败");
						}
					}else{
						responseObject.setCode(ResponseCode.CLAIM_PACKAGE_UPDATE_STATE_FAIL);
						responseObject.setMessage("审核更新失败");
					}
				}else{//没通过
					if(auditRemark == null){
						auditRemark = "";
					}
					state = "4";//try to make it same as state in renlingBaoguo
					int result = this.renlingPersonDao.updateStateAndRemark(claimPersonId, state, auditRemark);
					if(result > 0){
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.CLAIM_PERSON_UPDATE_REMARK_FAIL);
						responseObject.setMessage("审核更新失败");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("fail to update claim package by audit", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<PageSplit<RenlingPersons>> getByClaimPackageId(
			String claimPackageId, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<RenlingPersons>> responseObject = new ResponseObject<PageSplit<RenlingPersons>>();
		if(StringUtil.isEmpty(claimPackageId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				int rowCount = this.renlingPersonDao.countByClaimPackage(claimPackageId);
				if(rowCount > 0){
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageIndex = Math.min(pageIndex, pageCount);
					int firstResult = (pageIndex - 1) * pageSize;
					PageSplit<RenlingPersons> pageSplit = new PageSplit<RenlingPersons>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageIndex);
					pageSplit.setPageSize(pageSize);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);
					List<RenlingPersons> list = renlingPersonDao.findByClaimPackage(claimPackageId, firstResult, pageSize);
					if(list.size() > 0){
						pageSplit.setDatas(list);
						responseObject.setData(pageSplit);
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.CLAIM_PACKAGE_FIND_BY_NOT_CLAIMED_FAIL);
						responseObject.setMessage("获取认领包裹列表失败");
					}
				}else{
					responseObject.setMessage("没有可以认领的包裹");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("fail to update claim package by audit", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<PageSplit<RenlingPersons>> findAllByClaimPackageId(String claimPackageId, int pageIndex, int pageSize)
			throws ServiceException {
		ResponseObject<PageSplit<RenlingPersons>> responseObject = new ResponseObject<PageSplit<RenlingPersons>>();
		if(StringUtil.isEmpty(claimPackageId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				int rowCount = this.renlingPersonDao.countAllByClaimPackage(claimPackageId);
				if(rowCount > 0){
					PageSplit<RenlingPersons> pageSplit = new PageSplit<RenlingPersons>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
					List<RenlingPersons> list = renlingPersonDao.findAllByClaimPackage(claimPackageId, firstResult, pageSize);
					if(list.size() > 0){
						pageSplit.setDatas(list);
						responseObject.setData(pageSplit);
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.CLAIM_PACKAGE_FIND_BY_NOT_CLAIMED_FAIL);
						responseObject.setMessage("获取认领包裹列表失败");
					}
				}else{
					responseObject.setMessage("没有可以认领的包裹");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("fail to update claim package by audit", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<String[]> getAllStateCount() throws ServiceException {
		ResponseObject<String[]> responseObject = new ResponseObject<String[]>();
		try {
			String[] array = new String[8];
			int i = 0;
			//1
			array[i++] = "countClaimPackageState0";
			array[i++] = String.valueOf(this.renlingDao.countByKey(null, null, null, null, null, null, Constant.RENLING_STATE0));
			array[i++] = "countClaimPackageState1";
			array[i++] = String.valueOf(this.renlingDao.countByKey(null, null, null, null, null, null, Constant.RENLING_STATE1));
			array[i++] = "countClaimPackageState2";
			array[i++] = String.valueOf(this.renlingDao.countByKey(null, null, null, null, null, null, Constant.RENLING_STATE2));
			array[i++] = "countClaimPackageState3";
			array[i++] = String.valueOf(this.renlingDao.countByKey(null, null, null, null, null, null, Constant.CLAIM_PERSON_STATE_AUDIT));
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(array);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
}
