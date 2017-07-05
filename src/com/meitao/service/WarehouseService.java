package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Warehouse;


public interface WarehouseService {
	/**
	 * 添加仓库地址
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> addWarehouse(Warehouse warehouse) throws ServiceException;

	/**
	 * 修改仓库
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyWarehouse(Warehouse warehouse) throws ServiceException;

	/**
	 * 根据id删除仓库
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteWarehouseByIds(String[] ids) throws ServiceException;
	
	/**
	 * 根据id获取仓库
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Warehouse> getWarehouseById(String id) throws ServiceException;

	/**
	 * 获取全部仓库地址
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<List<Warehouse>> getAll() throws ServiceException;

	/**
	 * 分页获取仓库列表
	 * 
	 * @param pageSize
	 * @param pageNow
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<Warehouse>> searchPageSplit(int pageSize, int pageNow, String groupid) throws ServiceException;
	//根据用户信息获取门店信息
	public ResponseObject<List<Warehouse>> getwarehousebyadmin(String storeids) throws ServiceException;
	
	//查找门店信息
	public ResponseObject<PageSplit<Warehouse>> searchPageSplitbyadmin(String keyword,String storeids,int pageSize, int pageNow) throws ServiceException;
}
