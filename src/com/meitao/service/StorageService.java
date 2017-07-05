package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Storage;

public interface StorageService {

	ResponseObject<Object> add(Storage storage, String colNumber, String rowNumber) throws ServiceException;

	ResponseObject<PageSplit<Storage>> findByWarehouse(String warehouseId, String type, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<Object> delete(String id) throws ServiceException;

	/**
	 * 通过储藏间的门店，类型，关联的要转运门店/渠道来确定的指定储藏间
	 * @param warehouseId
	 * @param type 转运拆包前0:STORAGE_TYPE_BEFORE_OPEN； 转运拆包后1：STORAGE_TYPE_AFTER_OPEN； 订单2:STORAGE_TYPE_ORDER
	 * @param relateId
	 * 		若是拆包前：要转运的州的门店id---tranWarehouseId
	 * 		若是拆包后：-1
	 * 		若是order: 渠道id---channelId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<PageSplit<Storage>> findByWarehouseAndTypeAndRelateId(String warehouseId, String type, String relateId, int pageIndex, int pageSize) throws ServiceException;
	/**
	 * 通过三个条件查询storage
	 * @param warehouseId
	 * @param type
	 * @param typeRelateId
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<List<Storage>> findByWarehouseIdAndTypeAndRelateIdList(
			String warehouseId, String type, String typeRelateId)
			throws ServiceException;

}
