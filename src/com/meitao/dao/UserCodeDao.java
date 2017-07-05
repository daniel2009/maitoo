package com.meitao.dao;

import com.meitao.model.UserCode;

public abstract interface UserCodeDao
{
  public abstract int insertUserCode(UserCode paramUserCode)
    throws Exception;

  public abstract int updateUserCode(UserCode paramUserCode)
    throws Exception;

  public abstract UserCode findUserCodeById(int paramInt)
    throws Exception;
  
  public abstract UserCode findUserCodeByState(String state)
		    throws Exception;
}