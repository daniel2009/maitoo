package com.meitao.service;



import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;



public interface SendUserService {
	/**
	 * 添加航班信息
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> getallbyid(String userid) throws ServiceException;

}
