package com.meitao.dao;

import java.util.List;








import org.apache.ibatis.annotations.Param;


import com.meitao.model.SendUser;


public interface SendUserDao {
	//插入发送用户信息
	public int insertSendUser(SendUser suser) throws Exception;
	//根据用户名和电话是否存在
	public int checksenduser(@Param("name") String name,@Param("phone") String phone,@Param("userid") String userid)  throws Exception;;
	
	
	//获取所有发送用户信息
	public List<SendUser> getAllbyuserid(@Param("userid") String userid) throws Exception;
	

}
