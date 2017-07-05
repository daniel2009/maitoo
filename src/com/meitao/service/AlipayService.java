package com.meitao.service;

import java.util.Map;

import com.meitao.exception.ServiceException;
import com.meitao.model.AccountDetail;
import com.meitao.model.ResponseObject;

public interface AlipayService {
	ResponseObject<String> recharge(String userId, String amount, String outTradeNo) throws ServiceException;

	/**
	 * set appid, mch_id, key from database
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	boolean setMapFromDataBase(Map<String, String> map) throws ServiceException;

	String checkRechargeState(String userId, String amount, String outTradeNo) throws ServiceException;
	
	//定义用户提交上来的，还没有确认成功能的处理方式
	public ResponseObject<String> beforerecharge(String userId, String amount, String outTradeNo) throws ServiceException;
	public AccountDetail checkbeforeRechargeState(String outTradeNo)
			throws ServiceException;
	public ResponseObject<String> modifyrecharge(String amount, String outTradeNo,AccountDetail accountDetail) throws ServiceException;
}
