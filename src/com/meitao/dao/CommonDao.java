package com.meitao.dao;

import org.apache.ibatis.annotations.Param;

public interface CommonDao {
	public int insertToken(@Param("email") String email, @Param("token") String token, @Param("date") String date)
	        throws Exception;

	public String retrieveToken(@Param("email") String email, @Param("date") String date) throws Exception;

	public int updateStatus(@Param("email") String email) throws Exception;
}