package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.StoragePositionRecord;

public interface StoragePositionRecordService {

	ResponseObject<Object> deleteByRelate(StoragePositionRecord storagePositionRecord) throws ServiceException;

	ResponseObject<Object> insertByRelate(StoragePositionRecord storagePositionRecord) throws ServiceException;

}
