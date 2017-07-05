package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.ErrorOrderInfo;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface ErrorOrderInfoService {
	public ResponseObject<Object> saveErrorOrderInfo(ErrorOrderInfo info) throws ServiceException;

	public ResponseObject<Object> deleteErrorOrderInfos(List<String> eids) throws ServiceException;

	public ResponseObject<Object> modifyErrorOrderInfo(ErrorOrderInfo info) throws ServiceException;

	public ResponseObject<PageSplit<ErrorOrderInfo>> searchByOrderId(String orderId, int pageSize, int pageNow)
	        throws ServiceException;
}
