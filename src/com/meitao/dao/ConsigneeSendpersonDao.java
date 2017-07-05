package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.ConsigneeSendperson;


public interface ConsigneeSendpersonDao {
	public int insert(ConsigneeSendperson saddress) throws Exception;

	public int update(ConsigneeSendperson ssddress) throws Exception;

	public int delete(@Param("id") String id, @Param("userId") String userId) throws Exception;
	
	public int countByInfo(@Param("userId") String userId, @Param("key") String key) throws Exception;

	public List<ConsigneeSendperson> searchByInfo(@Param("userId") String userId, @Param("key") String key,
	        @Param("index") int index, @Param("size") int size) throws Exception;
	
	public ConsigneeSendperson getUserinfoById(@Param("id") String id, @Param("userId") String userId) throws Exception;
	
	
	public int getcountuserphone(@Param("phone") String phone, @Param("userId") String userId) throws Exception;
	
	
}
