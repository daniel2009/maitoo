package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.CommudityAdmin;
//import com.meitao.model.Channel;
//import com.meitao.model.globalargs;

public interface CommudityAdminDao {
	public CommudityAdmin getById(String id) throws Exception;
	public List<CommudityAdmin> getByChannelId(@Param("channelId") String channelId) throws Exception;//根据渠道来获取商品信息
	public int insert(CommudityAdmin commudity) throws Exception;

	

	public int modify(CommudityAdmin commudity) throws Exception;;
	

	
	public List<CommudityAdmin> searchByKeys(@Param("id") String id,@Param("wordkey") String wordkey,@Param("channelId") String channelId, @Param("index") int index, @Param("size") int size) throws Exception;

	
	public int countByKeys(@Param("id") String id,@Param("wordkey") String wordkey,@Param("channelId") String channelId) throws Exception;
	
	public int delete(@Param("id") String id,@Param("channelId") String channelId) throws Exception;
	
	public List<CommudityAdmin> getByChannelIdandstate(@Param("channelId") String channelId,@Param("state") String state) throws Exception;//根据渠道id和状态来获取商品
}
