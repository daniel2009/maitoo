package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.TranshipmentRoute;

public interface TranshipmentRouteService {

	ResponseObject<List<TranshipmentRoute>> findByTranshipmentBill(String transhipmentId) throws ServiceException;

	ResponseObject<Object> add(String transhipmentId, String state, String warehouseId) throws ServiceException;

	ResponseObject<Object> add(TranshipmentRoute transhipmentRoute)
			throws ServiceException;
	//根据id修改状态
	public ResponseObject<Object> modifystate(String state,String id,String storeId)
			throws ServiceException;

}
