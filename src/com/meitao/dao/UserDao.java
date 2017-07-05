package com.meitao.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.meitao.model.User;

public interface UserDao {
	public User getUserById(String id) throws Exception;

	public User getUserByOrderId(@Param("id") String id, @Param("orderId") String orderId) throws Exception;

	public User getUserByAccount(@Param("account") String account) throws Exception;
	public User getUserByPhone(@Param("phone") String phone) throws Exception;
	public List<User> getUserByPhonecheck(@Param("phone") String phone) throws Exception;
	
	public User getUserByEmailAccount(@Param("email") String email) throws Exception;
	
	public User getUserByUserCodeOrUserAlias(@Param("usercode") String usercode,@Param("useralias") String useralias) throws Exception;

	public int insertUser(User user) throws Exception;

	public int updateUserById(User user) throws Exception;

	public int updatePassword(@Param("id") String id, @Param("phone") String phone, @Param("password") String password,
	        @Param("oldpwd") String oldpwd) throws Exception;
	public int updatePasswordbyuser(@Param("id") String id, @Param("password") String password,
	        @Param("oldpwd") String oldpwd) throws Exception;	
	
	
	
	public int updatePasswordByEmail(@Param("id") String id, @Param("email") String phone, @Param("password") String password,
	        @Param("oldpwd") String oldpwd) throws Exception;

	public int deleteUserByIds(List<String> list) throws Exception;

	public List<User> searchUserByKey(@Param("userId") String userId, @Param("key") String key,
	        @Param("column") String column, @Param("index") int index, @Param("size") int size, @Param("groupId") String groupId) throws Exception;

	public int countByKey(@Param("userId") String userId, @Param("key") String key, @Param("column") String column)
	        throws Exception;

	/**
	 * 时间格式为:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param sdate
	 * @param edate
	 * @param type
	 *            用户类型，0表示普通用户，1表示店面用户，2表示会员用户
	 * @return
	 * @throws Exception
	 */
	public List<User> getExportUsersBySignDate(@Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("type") String type, @Param("groupId") String groupid) throws Exception;
	
	public List<User> getExportAllUsersBySignDate(@Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("type") String type) throws Exception;

	public int updatephoneById(User user) throws Exception;
	public int updateEmailById(User user) throws Exception;
	public List<User> checkexistsforphone(@Param("phone") String phone);
	public List<User> checkexistsforemail(@Param("email") String email);
	
	public int countOfuserbyphone(@Param("phone") String phone);
	public String getuseridbyphone(@Param("phone") String phone);
	
	
	public List<User> searchUserByInfo(@Param("id") String id,@Param("info") String info, @Param("index") int index, @Param("size") int size) throws Exception;

	public int countByInfo(@Param("id") String id,@Param("info") String info)
	        throws Exception;
	
	public List<User> searchByKeyAdmin(@Param("userId") String userId, @Param("key") String key,
	        @Param("type") String type, @Param("index") int index, @Param("size") int size) throws Exception;

	public int countByKeyAdmin(@Param("userId") String userId, @Param("key") String key,
	        @Param("type") String type)
	        throws Exception;
	
	//转运查找用户标识
	public List<User> searchUserByInfoforzy(@Param("id") String id,@Param("info") String info, @Param("index") int index, @Param("size") int size) throws Exception;

	public int countByInfoforzy(@Param("id") String id,@Param("info") String info)
	        throws Exception;
	
}
