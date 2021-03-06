package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.SpencialChannel;

public interface SpencialChannelDao {
	public SpencialChannel getChannelById(String id) throws Exception;
	
	
	
	public int insertChannel(SpencialChannel channel) throws Exception;

	public List<SpencialChannel> getByWarhouseId(@Param("warehouseId") String warehouseId, @Param("state") String state) throws Exception;

	//public String getById(@Param("warehouseId") String warehouseId,String id) throws Exception;

	public int modifyChannel(SpencialChannel channel) throws Exception;;
	
	public int insertChannelByList(List<SpencialChannel> list) throws Exception;
	
	public List<SpencialChannel> getByWarhouseIdandid(@Param("warehouseId") String warehouseId,@Param("id") String id, @Param("state") String state) throws Exception;
	

}
