package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface ConsigneeInfoService {
	/**
	 * 将收货地址对象保存的数据库中。<br/>
	 * 如果保存成功返回200
	 * 
	 * @param consigneeInfo
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> saveConsigneeInfo(ConsigneeInfo consigneeInfo) throws ServiceException;

	/**
	 * 修改收货地址信息<br/>
	 * 如果修改成功则返回200
	 * 
	 * @param consigneeInfo
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyConsigneeInfo(ConsigneeInfo consigneeInfo) throws ServiceException;

	/**
	 * 根据id删除收货地址<br/>
	 * 如果删除成功返回200
	 * 
	 * @param id
	 *            删除收件人id
	 * @param userId
	 *            删除记录人id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteConsigneeInfo(String id, String userId) throws ServiceException;

	/**
	 * 根据id获取收货地址信息<br/>
	 * 如果操作成功返回200
	 * 
	 * @param id
	 *            删除收件人id
	 * @param userId
	 *            删除记录人id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<ConsigneeInfo> getById(String id, String userId) throws ServiceException;

	/**
	 * 根据收货人姓名获取收货地址列表.<br/>
	 * 跳转到pageNow页面，比如跳转到第一页<br/>
	 * 获取pageSize个记录，如果总的记录数少于pageSize，就返回所有的记录列表<br/>
	 * 如果操作成功就返回200
	 * 
	 * @param userId
	 * @param name
	 * @param pageSize
	 * @param pageNow
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<ConsigneeInfo>> getByName(String userId, String name, int pageSize, int pageNow)
	        throws ServiceException;
	
	
	public ResponseObject<PageSplit<ConsigneeInfo>> getByInfo(String userId, String name, int pageSize, int pageNow)
	        throws ServiceException;
}
