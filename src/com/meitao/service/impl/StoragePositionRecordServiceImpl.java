package com.meitao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.StoragePositionRecordDao;
import com.meitao.service.StoragePositionRecordService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.StoragePositionRecord;

@Service("storagePositionRecordService")
public class StoragePositionRecordServiceImpl implements StoragePositionRecordService{
	@Autowired
	private StoragePositionRecordDao storagePositionRecordDao;

	@Override
	public ResponseObject<Object> deleteByRelate(StoragePositionRecord storagePositionRecord) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try {
			int result = this.storagePositionRecordDao.deleteByRelate(storagePositionRecord);
			if(result > 0){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}else{
				responseObject.setCode(ResponseCode.STORAGE_POSITION_DELETE_ERROR);
				responseObject.setMessage("直接删除包裹在库书局失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> insertByRelate(StoragePositionRecord storagePositionRecord) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try {
			int result = this.storagePositionRecordDao.insert(storagePositionRecord);
			if(result > 0){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}else{
				responseObject.setCode(ResponseCode.STORAGE_POSITION_INSERT_ERROR);
				responseObject.setMessage("直接添加包裹在库书局失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
}
