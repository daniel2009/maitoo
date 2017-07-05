package com.meitao.service;

import java.util.Map;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;

public interface WeixinService {
	ResponseObject<String> scanRecharge(String userId, String amount, String flag) throws ServiceException;

	ResponseObject<Object> checkIfScanPay(String userId, String amount,
			String accountDetailId, String remark) throws ServiceException;

	/**
	 * accountDetail or remark must only has one of them
	 * @param userId
	 * @param amount
	 * @param accountDetailId
	 * @param remark
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<String> addPreRecharge(String userId, double amount, String remark) throws ServiceException;

	/**
	 * set appid, mch_id, key from database
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	boolean setMapFromDataBase(Map<String, String> map) throws ServiceException;

}
