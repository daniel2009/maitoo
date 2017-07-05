package com.meitao.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.meitao.model.LoginInfo;

public interface LoginInfoDao {
	public int insertUpdate(@Param("id") String id, @Param("type") String type, @Param("date") String date)
	        throws Exception;

	public LoginInfo getById(@Param("id") String id, @Param("type") String type) throws Exception;

	public List<LoginInfo> getAll() throws Exception;
}
