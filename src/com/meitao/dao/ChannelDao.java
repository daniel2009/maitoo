package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Channel;


public interface ChannelDao {
	public Channel getChannelById(String id) throws Exception;
	public int insertChannel(Channel channel) throws Exception;

	public List<Channel> getByWarhouseId(@Param("warehouseId") String warehouseId, @Param("state") String state) throws Exception;

	//public String getById(@Param("warehouseId") String warehouseId,String id) throws Exception;

	public int modifyChannel(Channel channel) throws Exception;;
	
	public int insertChannelByList(List<Channel> list) throws Exception;
	
	public List<Channel> getByWarhouseIdandid(@Param("warehouseId") String warehouseId,@Param("id") String id, @Param("state") String state) throws Exception;
	public List<Channel> getall(@Param("id") String id) throws Exception;//获取所有的渠道,如果id为空，将提取所有的渠道
	
	
	public List<Channel> searchByKeys(@Param("id") String id,@Param("wordkey") String wordkey, @Param("index") int index, @Param("size") int size) throws Exception;


	public int countByKeys(@Param("id") String id,@Param("wordkey") String wordkey) throws Exception;
	
	public int deletebyid(@Param("id") String id) throws Exception;
	
	public List<Channel> gettrantype(@Param("tran_type") String tran_type) throws Exception;//获取指定类型的渠道

}
