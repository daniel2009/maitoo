package com.meitao.service;



import com.meitao.model.Send_User;
import com.meitao.exception.ServiceException;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


public interface Send_UserService {
	public ResponseObject<PageSplit<Send_User>> searchSendUserByInfo(String info, int pageSize, int pageNow)
	        throws ServiceException;

}
