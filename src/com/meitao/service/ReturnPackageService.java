package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ReturnPackage;

public interface ReturnPackageService {

	ResponseObject<PageSplit<ReturnPackage>> findAll(int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<PageSplit<ReturnPackage>> findByUser(String userId, String state, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<Object> add(ReturnPackage returnPackage) throws ServiceException;

	ResponseObject<Object> audit(String id, String empId, String state,
			String empRemark, String empExpressNo, String empExpressCompany, String empPic, String shippingFee) throws ServiceException;

	ResponseObject<ReturnPackage> findById(String id) throws ServiceException;

	ResponseObject<Object> modifyByUser(ReturnPackage returnPackage) throws ServiceException;

	ResponseObject<PageSplit<ReturnPackage>> findByKey(String id, String key, String type, String state, String createDateStart, String createDateEnd, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<Object> cancelByUser(String transhipmentId, String userId) throws ServiceException;

	ResponseObject<String[]> getAllStateCount() throws ServiceException;

}
