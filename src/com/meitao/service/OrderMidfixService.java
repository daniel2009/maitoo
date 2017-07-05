package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.OrderMidfix;
import com.meitao.model.ResponseObject;


public abstract interface OrderMidfixService
{
  public abstract ResponseObject<Object> modifyOrderMidfix(String paramString1, String paramString2)
    throws ServiceException;
  
  public abstract OrderMidfix findOrderMidfixByState0() throws ServiceException;
  
}