package com.meitao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.CallOrderDao;
import com.meitao.service.CallOrderService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.CallOrderUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PageSplitUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.CallOrder;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


@Service("callOrderService")
public class CallOrderServiceImpl extends BasicService implements CallOrderService {
	@Autowired
	private CallOrderDao callOrderDao;

	public ResponseObject<Object> deleteCallOrder(String id, String userId) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(userId)) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				int iresult = this.callOrderDao.deleteCallOrder(id, userId);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CALL_ORDER_DELETE_FAILURE);
					responseObj.setMessage("根据id删除上门收件信息失败，id:" + id);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("删除上门收件信息失败", e);
			}
		}
		return responseObj;
	}

	public ResponseObject<Object> modifyCallOrder(CallOrder callOrder) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (null == callOrder) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				callOrder.setModifyDate(DateUtil.date2String(new Date()));
				int iresult = this.callOrderDao.updateCallOrder(callOrder);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CALL_ORDER_MODIFY_FAILURE);
					responseObj.setMessage("修改上门收取信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("修改上门收件 信息失败", e);
			}
		}
		return responseObj;
	}

	public ResponseObject<Object> saveCallOrder(CallOrder callOrder) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if (null == callOrder) {
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else if(!CallOrderUtil.validateCallOrderName(callOrder.getsName())){
			responseObject.setCode(ResponseCode.CONSIGNEE_NAME_ERROR);
			responseObject.setMessage("寄件人姓名不正确，请重新输入!");
		}else if(!CallOrderUtil.validateStreetAddress(callOrder.getsStreetAddress())){
			responseObject.setCode(ResponseCode.CALL_ORDER_STREET_ADDRESS_ERROR);
			responseObject.setMessage("详细地址填写不正确，请重新输入！");
		}else if(!CallOrderUtil.validateZipCode(callOrder.getsZipCode())){
			responseObject.setCode(ResponseCode.CALL_ORDER_ZIP_CODE_ERROR);
			responseObject.setMessage("邮编填写不正确，请重新输入！");
		}else if(!CallOrderUtil.validatePhone(callOrder.getsPhone())){
			responseObject.setCode(ResponseCode.CALL_ORDER_PHONE_ERROR);
			responseObject.setMessage("手机号码填写不正确，请重新输入！");
		}else {
			callOrder.setState(Constant.CALL_ORDER_STATE_NOT_TREAT);//设置没处理
			//callOrder.setType("1");
			callOrder.setCreateDate(DateUtil.date2String(new Date()));
			callOrder.setModifyDate(callOrder.getCreateDate());
			try {
				int result = this.callOrderDao.insertCallOrder(callOrder);
				if (result > 0) {
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObject.setCode(ResponseCode.CALL_ORDER_INSERT_FAILURE);
					responseObject.setMessage("添加上门收件信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("添加收件信息失败", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> modifyCallOrderState(String id, String state,
			String empId) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (null == id||null == state||null == empId) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		}  else {
			try {
				String modifyDate = DateUtil.date2String(new Date());
				int iresult = this.callOrderDao.updateCallOrderState(id,state,empId,modifyDate);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CALL_ORDER_MODIFY_FAILURE);
					responseObj.setMessage("修改上门收取状态失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("修改上门收件 状态失败", e);
			}
		}
		return responseObj;
	}

	@Override
	public ResponseObject<PageSplit<CallOrder>> getAllCallOrder(int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<CallOrder>> responseObject = new ResponseObject<PageSplit<CallOrder>>();
		try{
			int rowCount = this.callOrderDao.countAll();
			if(rowCount > 0){
				PageSplit<CallOrder> pageSplit = new PageSplit<CallOrder>();
				int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
				List<CallOrder> list = this.callOrderDao.findAll(firstResult, pageSize);
				pageSplit.setDatas(list);
				responseObject.setData(pageSplit);
			}else{
				responseObject.setCode(ResponseCode.CALL_ORDER_LIST_EMPTY);
				responseObject.setMessage("没有上门取件信息");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("获取上门取件出错", e);
		}
		return responseObject;
	}
	@Override
	public ResponseObject<PageSplit<CallOrder>> getCallOrderByUserId(String userId, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<CallOrder>> responseObject = new ResponseObject<PageSplit<CallOrder>>();
		if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try{
				int rowCount = this.callOrderDao.countAllByUserId(userId);
				if(rowCount > 0){
					PageSplit<CallOrder> pageSplit = new PageSplit<CallOrder>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
					List<CallOrder> list = this.callOrderDao.findAllByUserId(userId, firstResult, pageSize);
					pageSplit.setDatas(list);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setCode(ResponseCode.CALL_ORDER_LIST_EMPTY);
					responseObject.setMessage("没有上门取件信息");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("获取上门取件出错", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<CallOrder> findById(CallOrder callOrder) throws ServiceException {
		ResponseObject<CallOrder> responseObject = new ResponseObject<CallOrder>();
		try{
			callOrder = this.callOrderDao.findById(callOrder);
			if(callOrder != null){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(callOrder);
			}else{
				responseObject.setCode(ResponseCode.CALL_ORDER_LIST_EMPTY);
				responseObject.setMessage("没有上门取件信息");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("获取上门取件出错", e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<PageSplit<CallOrder>> findByKey(String key, String type, String state, String warehouseId, String createDateStart, String createDateEnd, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<CallOrder>> responseObject = new ResponseObject<PageSplit<CallOrder>>();
		if(!StringUtil.isEmpty(type)){
			if(type.equals("1")){
				type = "user_id";
			}else if(type.equals("2")){
				type = "send_name";
			}else if(type.equals("3")){
				type = "send_phone";
			}else{
				type = "";
			}
		}
		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			int rowCount = this.callOrderDao.countByKey(key, type, state, warehouseId, createDateStart, createDateEnd);
			if(rowCount > 0){
				PageSplit<CallOrder> pageSplit = new PageSplit<CallOrder>();
				int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
				List<CallOrder> list = this.callOrderDao.findByKey(key, type, state, warehouseId, createDateStart, createDateEnd, firstResult, pageSize);
				pageSplit.setDatas(list);
				responseObject.setData(pageSplit);
			}else{
				responseObject.setCode(ResponseCode.CALL_ORDER_LIST_EMPTY);
				responseObject.setMessage("没有上门取件信息");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("获取上门取件出错", e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> modifyByUser(CallOrder callOrder)
			throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (StringUtil.isEmpty(callOrder.getId()) || StringUtil.isEmpty(callOrder.getUserId())) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			callOrder.setModifyDate(DateUtil.date2String(new Date()));
			callOrder.setState(Constant.CALL_ORDER_STATE_NOT_TREAT);
			try {
				int iresult = this.callOrderDao.updateCallOrder(callOrder);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CALL_ORDER_UPDATE_FAILURE);
					responseObj.setMessage("更新上门收件信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("更新上门收件信息失败", e);
			}
		}
		return responseObj;
	}

	@Override
	public ResponseObject<Object> audit(CallOrder callOrder)
			throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (StringUtil.isEmpty(callOrder.getId()) || StringUtil.isEmpty(callOrder.getEmpId())) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else if(StringUtil.isEmpty(callOrder.getConfirmDate()) && Constant.CALL_ORDER_STATE_TREAT.equals(callOrder.getState())){
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("请输入确认时间");
		} else {
			callOrder.setModifyDate(DateUtil.date2String(new Date()));
			try {
				int iresult = this.callOrderDao.audit(callOrder);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CALL_ORDER_UPDATE_FAILURE);
					responseObj.setMessage("更新上门收件信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("更新上门收件信息失败", e);
			}
		}
		return responseObj;
	}
}
