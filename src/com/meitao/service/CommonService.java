package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;


public interface CommonService {
	/**
	 * 插入email和token到数据中去，成功则返回200
	 * 
	 * @param email
	 * @param token
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<String> insertToken(String email, String token) throws ServiceException;

	/**
	 * 检测对应的email和token是否和数据库中的值对应，是则返回200
	 * 
	 * @param email
	 * @param token
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<String> checkToken(String email, String token) throws ServiceException;

	/**
	 * 根据email获取最近一次有效的token，如果没有则返回null。
	 * 
	 * @param email
	 * @return
	 * @throws ServiceException
	 */
	public String getLastToken(String email) throws ServiceException;
}
