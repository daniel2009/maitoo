package com.meitao.service;

import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;


/**
 * 提供新闻服务的接口
 */
public interface TranPriceService {
	public ResponseObject<Object> getalltranprice() throws ServiceException;
}
