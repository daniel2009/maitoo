package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.ErrorTranshipmentBillInfo;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface ErrorTranshipmentBillService {
	public ResponseObject<Object> saveErrorTranshipmentBillInfo(ErrorTranshipmentBillInfo info) throws ServiceException;

	public ResponseObject<Object> deleteErrorTranshipmentBillInfos(List<String> eids) throws ServiceException;

	public ResponseObject<Object> modifyErrorTranshipmentBillInfo(ErrorTranshipmentBillInfo info)
	        throws ServiceException;

	public ResponseObject<PageSplit<ErrorTranshipmentBillInfo>> searchByTranshipmentId(String tId, int pageSize,
	        int pageNow) throws ServiceException;
}