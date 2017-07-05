package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ConsigneeSendperson;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface ConsigneeSendpersonService {
	/**
	 * 将发货地址对象保存的数据库中。<br/>
	 * 如果保存成功返回200
	 * 
	 * @param consigneeInfo
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> saveConsigneeSendperson(ConsigneeSendperson consigneeInfo) throws ServiceException;

	/**
	 * 修改发货地址信息<br/>
	 * 如果修改成功则返回200
	 * 
	 * @param consigneeInfo
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifySendperson(ConsigneeSendperson consigneeInfo) throws ServiceException;

	/**
	 * 根据id删除发货地址<br/>
	 * 如果删除成功返回200
	 * 
	 * @param id
	 *            删除收件人id
	 * @param userId
	 *            删除记录人id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteConsigneeSendperson(String id, String userId) throws ServiceException;

	/**
	 * 根据id获取发货地址信息<br/>
	 * 如果操作成功返回200
	 * 
	 * @param id
	 *            删除收件人id
	 * @param userId
	 *            删除记录人id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<ConsigneeSendperson> getById(String id, String userId) throws ServiceException;

	
	
	
	public ResponseObject<PageSplit<ConsigneeSendperson>> getByInfo(String userId, String name, int pageSize, int pageNow)
	        throws ServiceException;
}
