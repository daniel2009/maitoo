package com.meitao.dao;

import java.util.List;









import org.apache.ibatis.annotations.Param;

import com.meitao.model.Send_User;


public interface Send_UserDao {
	//插入发送用户信息
	public int insertSendUser(Send_User suser) throws Exception;

	public Send_User getById(@Param("id") int id) throws Exception;

	public int countOfSendUser(@Param("info") String info) throws Exception;
	


	public List<Send_User> searchSendUser(@Param("info") String info,@Param("index") int index,
	        @Param("size") int size) throws Exception;
	
	public int updateSuser(Send_User suser) throws Exception;

}
