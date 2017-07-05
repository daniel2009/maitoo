package com.meitao.dao;

import java.util.List;











import org.apache.ibatis.annotations.Param;

import com.meitao.model.Receive_User;


public interface Receive_UserDao {
	//插入接收用户信息
	public int insertReceiveUser(Receive_User ruser) throws Exception;

	
	/**
	 * 计算接收用户的数量
	 * 
	 */
	public int countOfReceiveUser(@Param("info") String info) throws Exception;
	

	/**
	 * 搜索接收用户
	 * 
	 * @param storeId
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<Receive_User> searchReceiveUser(@Param("info") String info,@Param("index") int index,
	        @Param("size") int size) throws Exception;
	
	
	public Receive_User getById(@Param("id") int id) throws Exception;
	
	public int modifymatchinfo(Receive_User ruser)  throws Exception;;
	public int modifyuploadcardinfo(Receive_User ruser)  throws Exception;;
	public int updateRuser(Receive_User ruser)  throws Exception;;//更新收件人信息
	
	public int updatecardid_flag(@Param("id") String id,@Param("cardid_flag") String cardid_flag,@Param("modifyDate") String modifyDate);//更新验证状态，即是否已经验证过身份证信息
	
	public int get_allneedverfycardid();//获取所有没有验证的运单数量
	public Receive_User getOneNeedverfyCardid() throws Exception;//获取一条要验证的信息
	
	//public int updatecardidflag(@Param("id") String id
	
}
