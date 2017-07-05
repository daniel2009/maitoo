package com.meitao.dao;

import java.util.List;























import org.apache.ibatis.annotations.Param;

import com.meitao.cardid.manage.CardId_lib;

public interface CardIdManageDao {

	
	public int insertcaridinfo(CardId_lib cardid)  throws Exception;;//身份证信息插入
	public int countbycardid(@Param("cardid") String cardid)  throws Exception;;//查找身分证id数量
	public List<CardId_lib> getcardidbyname(@Param("cname") String cname)  throws Exception;;//查找身分证id数量
	
	
	public int countOfSearchKeys(@Param("key") String key) throws Exception;

	public List<CardId_lib> searchByKeys(@Param("key") String key,@Param("index") int index, @Param("size") int size) throws Exception;
	
	public CardId_lib getbyid(@Param("id") String id)  throws Exception;;//
	public int deleteonecard(@Param("id") String id)  throws Exception;;//根据id删除一条数据
	
	public List<CardId_lib> getcardidbysearcnames(@Param("key") String key)  throws Exception;;
	public String getmaxid()  throws Exception;;
	public String getminid()  throws Exception;;
	public CardId_lib getonebyrand(@Param("randnumber") int randnumber)  throws Exception;;
	
	public List<CardId_lib> getbycardid(@Param("cardid") String cardid) throws Exception;//根据身份号获取信息
	
	public int updatepicurl(@Param("id") String id,@Param("picurl") String picurl,@Param("modifyDate") String modifyDate,@Param("phone") String phone) throws Exception;//修改身份证图片
	
	public int countphoneandname(@Param("phone") String phone,@Param("name") String name,@Param("modifyDate") String modifyDate) throws Exception;//根据身份号获取信息
}
