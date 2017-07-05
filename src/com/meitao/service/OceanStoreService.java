package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.OceanStore;
import com.meitao.model.ResponseObject;

public interface OceanStoreService {

	ResponseObject<Object> add(OceanStore oceanStore) throws ServiceException;

	ResponseObject<OceanStore> getById(String id) throws ServiceException;

	ResponseObject<Object> modify(OceanStore oceanStore) throws ServiceException;

	ResponseObject<List<OceanStore>> getAll() throws ServiceException;

	ResponseObject<Object> delete(String id) throws ServiceException;

}
