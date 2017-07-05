package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.CallOrder;



public interface CallOrderDao {
	public int insertCallOrder(CallOrder callOrder) throws Exception;
	public int updateCallOrder(CallOrder callOrder) throws Exception;
	public int updateCallOrderState(@Param("id") String id, @Param("state") String state, @Param("empId") String empId
			, @Param("modifyDate") String modifyDate) throws Exception;
	public int deleteCallOrder(@Param("id") String id, @Param("userId") String userId) throws Exception;
	
	public int countAll() throws Exception;
	public List<CallOrder> findAll(@Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	
	public int countAllByUserId(@Param("userId")String userId) throws Exception;
	public List<CallOrder> findAllByUserId(@Param("userId")String userId, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	public CallOrder findById(CallOrder callOrder) throws Exception;
	
	public int countByKey(@Param("key")String key, @Param("type")String type, @Param("state")String state, @Param("warehouseId")String warehouseId,
			@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd) throws Exception;
	public List<CallOrder> findByKey(@Param("key")String key, @Param("type")String type, @Param("state")String state, @Param("warehouseId")String warehouseId,
			@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	public int audit(CallOrder callOrder) throws Exception;
}
