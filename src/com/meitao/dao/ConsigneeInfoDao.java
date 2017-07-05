package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.ConsigneeInfo;


public interface ConsigneeInfoDao {
	public int insertConsigneeInfo(ConsigneeInfo deliveryAddress) throws Exception;

	public int updateConsigneeInfo(ConsigneeInfo deliveryAddress) throws Exception;

	public int deleteConsigneeInfo(@Param("id") String id, @Param("userId") String userId) throws Exception;
	
	public int deleteConsigneeInfoByUserIds(List<String> list) throws Exception;

	public ConsigneeInfo retrieveConsigneeInfoById(@Param("id") String id, @Param("userId") String userId) throws Exception;

	public List<ConsigneeInfo> searchConsigneeInfoByName(@Param("userId") String userId, @Param("key") String key,
	        @Param("index") int index, @Param("size") int size) throws Exception;

	public int countByName(@Param("userId") String userId, @Param("key") String key) throws Exception;
	
	public int countByInfo(@Param("userId") String userId, @Param("key") String key) throws Exception;
	public List<ConsigneeInfo> searchConsigneeInfoByInfo(@Param("userId") String userId, @Param("key") String key,
	        @Param("index") int index, @Param("size") int size) throws Exception;
	
	public int getcountuserphone(@Param("phone") String phone, @Param("userId") String userId) throws Exception;
}
