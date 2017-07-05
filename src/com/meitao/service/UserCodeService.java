package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.UserCode;


public abstract interface UserCodeService
{
  public abstract ResponseObject<Object> modifyUserCode(String paramString1, String paramString2)
    throws ServiceException;

  public abstract UserCode findUserCodeById(int paramInt)
    throws ServiceException;
  
  public abstract UserCode findUserCodeByState0()
		    throws ServiceException;
}