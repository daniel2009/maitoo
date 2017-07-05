package com.meitao.dao;

import java.util.List;















import org.apache.ibatis.annotations.Param;

import com.meitao.model.CardId;


public interface CardIdDao {

	
	public int insertcaridinfo(CardId cardid)  throws Exception;;//身份证信息插入
	public int countbycardid(@Param("cardid") String cardid)  throws Exception;;//查找身分证id数量
	public List<CardId> getcardidbyname(@Param("cname") String cname)  throws Exception;;//查找身分证id数量
}
