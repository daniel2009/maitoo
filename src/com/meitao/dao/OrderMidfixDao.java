package com.meitao.dao;

import com.meitao.model.OrderMidfix;


public abstract interface OrderMidfixDao
{
  public abstract int insertOrderMidfix(OrderMidfix paramOrderMidfix)
    throws Exception;

  public abstract int updateOrderMidfix(OrderMidfix paramOrderMidfix)
    throws Exception;

  
  public abstract OrderMidfix findOrderMidfixByState(String state)
		    throws Exception;
}