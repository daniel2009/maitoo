package com.meitao.service;

import java.util.Map;

import com.meitao.exception.ServiceException;
import com.meitao.model.Message;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface MessageService {
	/**
	 * 保存留言对象到数据库中
	 * 
	 * @param msg
	 *            要保存的留言
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> saveMessage(Message msg) throws ServiceException;

	/**
	 * 根据用户id获取该分页获取该用户的留言列表。
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public <T> ResponseObject<PageSplit<T>> getByUserId(String userId, int pageSize, int pageNow)
	        throws ServiceException;

	/**
	 * 根据key获取数据和留言状态分页获取留言信息。
	 * @param key TODO
	 * @param userId
	 * @param state
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @param <T>
	 * @return
	 * @throws ServiceException
	 */
	public <T> ResponseObject<PageSplit<T>> searchByUserId(String key, String userId, String state, int pageSize, int pageNow, String groupid)
	        throws ServiceException;

	/**
	 * 获取留言数量，保存总数量，以及未回复的留言数量
	 * @param userId TODO
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Map<String, String>> getMessageCount(String userId) throws ServiceException;
}
