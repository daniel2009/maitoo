package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;
import com.meitao.model.AutoSend;
import com.meitao.model.User;

public interface AutoSendService {

	ResponseObject<List<AutoSend>> findAll() throws ServiceException;

	ResponseObject<Object> add(AutoSend autoSend) throws ServiceException;

	ResponseObject<Object> modify(AutoSend autoSend) throws ServiceException;

	ResponseObject<Object> delete(AutoSend autoSend) throws ServiceException;

	ResponseObject<AutoSend> findByName(AutoSend autoSend) throws ServiceException;

	ResponseObject<Object> send(User user, Object object) throws ServiceException;

	ResponseObject<Object> send(User user, Object object, String code) throws ServiceException;

	ResponseObject<Object> getNeedResiterCode() throws ServiceException;

}
