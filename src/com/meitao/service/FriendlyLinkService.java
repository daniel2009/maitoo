package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.FriendlyLink;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface FriendlyLinkService {
	/**
	 * 保存友情链接
	 * 
	 * @param link
	 *            友情链接对象
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> saveLink(FriendlyLink link) throws ServiceException;

	/**
	 * 修改友情链接
	 * 
	 * @param link
	 *            友情链接对象
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyLink(FriendlyLink link) throws ServiceException;

	/**
	 * 删除友情链接
	 * 
	 * @param ids
	 *            友情链接id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteLink(String[] ids) throws ServiceException;

	/**
	 * 根据id获取友情链接对象
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<FriendlyLink> getLinkById(String id) throws ServiceException;

	/**
	 * 获取所有友情链接列表
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<List<FriendlyLink>> getAllLink() throws ServiceException;

	/**
	 * 根据key值获取链接对象的分页记录
	 * 
	 * @param key
	 * @param pageSize
	 * @param pageNow
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<FriendlyLink>> getLinkByKey(String key, int pageSize, int pageNow)
	        throws ServiceException;
}
