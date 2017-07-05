package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.CallOrder;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface CallOrderService {
	public ResponseObject<Object> saveCallOrder(CallOrder callOrder) throws ServiceException;

	public ResponseObject<Object> modifyCallOrder(CallOrder callOrder) throws ServiceException;
	
	public ResponseObject<Object> modifyCallOrderState(String id, String state, String empId) throws ServiceException;

	public ResponseObject<Object> deleteCallOrder(String id, String userId) throws ServiceException;
	
	public ResponseObject<PageSplit<CallOrder>> getAllCallOrder(int pageIndex, int pageSize) throws ServiceException;

	public ResponseObject<PageSplit<CallOrder>> getCallOrderByUserId(String userId, int pageIndex, int pageSize) throws ServiceException;

	public ResponseObject<CallOrder> findById(CallOrder callOrder) throws ServiceException;

	public ResponseObject<PageSplit<CallOrder>> findByKey(String key, String type, String state, String warehouseId, String createDateStart, String createDateEnd, int pageIndex, int pageSize) throws ServiceException;

	public ResponseObject<Object> modifyByUser(CallOrder callOrder) throws ServiceException;

	public ResponseObject<Object> audit(CallOrder callOrder) throws ServiceException;

}
