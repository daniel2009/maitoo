package com.meitao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.StorageDao;
import com.meitao.dao.StoragePositionDao;
import com.meitao.service.StorageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PageSplitUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Storage;
import com.meitao.model.StoragePosition;

@Service("storageService")
public class StorageServiceImpl implements StorageService{
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private StoragePositionDao storagePostionDao;

	@Override
	public ResponseObject<Object> add(Storage storage, String colNumber, String rowNumber) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try {
			int result = this.storageDao.insert(storage);
			if (result > 0) {
				int rowNum = StringUtil.string2Integer(rowNumber);
				String modifyDate = DateUtil.date2String(new Date());
				try{
					for(int i = 1; i < rowNum+1; i++){
						int colNum = StringUtil.string2Integer(colNumber);
						for(int j = 1; j < colNum+1; j++){
							StoragePosition storagePosition = new StoragePosition();
							storagePosition.setStorageId(storage.getId());
							storagePosition.setModifyDate(modifyDate);
							storagePosition.setRowNumber(String.valueOf(i));
							storagePosition.setColNumber(String.valueOf(j));
							storagePosition.setAvailable("1");
							result = this.storagePostionDao.insert(storagePosition);
							if(result <= 0){
								responseObject.setCode(ResponseCode.STORAGE_POSITION_INSERT_ERROR);
								responseObject.setMessage("操作失败");
								throw new ServiceException("添加行，列过程中出现异常");
							}
						}
					}
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}catch(Exception e){
					throw ExceptionUtil.handle2ServiceException(e);
				}
			} else {
				responseObject.setCode(ResponseCode.STORAGE_INSERT_ERROR);
				responseObject.setMessage("操作失败,请检查是否已有同名储藏间");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<PageSplit<Storage>> findByWarehouse(
			String warehouseId, String type, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<Storage>> responseObject = new ResponseObject<PageSplit<Storage>>();
		if(StringUtil.isEmpty(warehouseId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try {
				int totalResult = this.storageDao.countByWarehouseId(warehouseId);
				if(totalResult > 0){
					PageSplit<Storage> pageSplit = new PageSplit<Storage>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, totalResult);
					List<Storage> list = this.storageDao.findByWarehouseId(warehouseId, type, firstResult, pageSize);
					if (list == null || list.size() <= 0) {
						responseObject.setCode(ResponseCode.STORAGE_LIST_ERROR);
						responseObject.setMessage("找不到指定要求的列表数据");
					} else {
						pageSplit.setDatas(list);
						responseObject.setData(pageSplit);
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}
				}else{
					responseObject.setCode(ResponseCode.STORAGE_LIST_IS_EMPTY);
					responseObject.setMessage("指定列表数据为空");
				}
				
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> delete(String id) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try {
			int result = this.storageDao.delete(id);
			if (result >= 0) {
				result = this.storagePostionDao.deleteByStorageId(id);
				if (result >= 0) {
				} else {
					responseObject.setCode(ResponseCode.STORAGE_POSITION_DELETE_ERROR);
					responseObject.setMessage("操作失败");
				}
			} else {
				responseObject.setCode(ResponseCode.STORAGE_DELETE_ERROR);
				responseObject.setMessage("操作失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<PageSplit<Storage>> findByWarehouseAndTypeAndRelateId(String warehouseId, String type, String typeRelateId, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<Storage>> responseObject = new ResponseObject<PageSplit<Storage>>();
		if(StringUtil.isEmpty(warehouseId) || StringUtil.isEmpty(type) || StringUtil.isEmpty(typeRelateId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			if(Constant.STORAGE_TYPE_AFTER_OPEN.equals(type)){
				typeRelateId = "-1";
			}
			try {
				int totalResult = this.storageDao.countByWarehouseIdAndTypeAndRelateId(warehouseId, type, typeRelateId);
				if(totalResult > 0){
					PageSplit<Storage> pageSplit = new PageSplit<Storage>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, totalResult);
					List<Storage> list = this.storageDao.findByWarehouseIdAndTypeAndRelateId(warehouseId, type, typeRelateId, firstResult, pageSize);
					if (list == null || list.size() <= 0) {
						responseObject.setCode(ResponseCode.STORAGE_LIST_ERROR);
						responseObject.setMessage("找不到指定要求的列表数据");
					} else {
						pageSplit.setDatas(list);
						responseObject.setData(pageSplit);
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}
				}else{
					responseObject.setCode(ResponseCode.STORAGE_LIST_IS_EMPTY);
					responseObject.setMessage("指定列表数据为空");
				}
				
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	/**
	 * 通过三个条件查询storage
	 */
	@Override
	public ResponseObject<List<Storage>> findByWarehouseIdAndTypeAndRelateIdList(String warehouseId, String type, String typeRelateId) throws ServiceException {
		ResponseObject<List<Storage>> responseObject = new ResponseObject<List<Storage>>();
		if(StringUtil.isEmpty(warehouseId) || StringUtil.isEmpty(type) || StringUtil.isEmpty(typeRelateId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try {
				List<Storage> list = this.storageDao.findByWarehouseIdAndTypeAndRelateIdList(warehouseId, type, typeRelateId);
				if(null == list || list.size() < 0 || list.isEmpty()){
					responseObject.setCode(ResponseCode.STORAGE_LIST_ERROR);
					responseObject.setMessage("找不到指定要求的列表数据");
				}
				else{
					responseObject.setData(list);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
				
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	
}
