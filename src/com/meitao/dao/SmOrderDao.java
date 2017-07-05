package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.SmOrder;


public interface SmOrderDao {
	public int insert(SmOrder smorder) throws Exception;
	public int update(SmOrder smorder) throws Exception;
	public int updateInfo(@Param("id") String id, @Param("state") String state, @Param("empId") String empId
			, @Param("modifyDate") String modifyDate, @Param("confirmDate") String confirmDate, @Param("remark") String remark) throws Exception;
	public int deletebyuser(@Param("id") String id, @Param("userId") String userId) throws Exception;
	
	
	public List<SmOrder> searchByKeys(@Param("userId") String userId,@Param("storeId") String storeId,@Param("state") String state,@Param("wordkey") String wordkey, @Param("index") int index, @Param("size") int size) throws Exception;


	public int countByKeys(@Param("userId") String userId,@Param("storeId") String storeId,@Param("state") String state,@Param("wordkey") String wordkey) throws Exception;
	
	
	public List<SmOrder> searchKeysByadmin(@Param("userId") String userId,@Param("storeId") String storeId,@Param("city") String city,@Param("csdate") String csdate,@Param("cedate") String cedate,@Param("cosdate") String cosdate,@Param("coedate") String coedate,@Param("state") String state,@Param("wordkey") String wordkey, @Param("index") int index, @Param("size") int size) throws Exception;


	public int countKeysbyadmin(@Param("userId") String userId,@Param("storeId") String storeId,@Param("city") String city,@Param("csdate") String csdate,@Param("cedate") String cedate,@Param("cosdate") String cosdate,@Param("coedate") String coedate,@Param("state") String state,@Param("wordkey") String wordkey) throws Exception;
	
	public List<SmOrder> selectlistbyids(@Param("ids") List<String> ids) throws Exception;

	
	//public int countAll() throws Exception;
	//public List<CallOrder> findAll(@Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	
	//public int countAllByUserId(@Param("userId")String userId) throws Exception;
	//public List<CallOrder> findAllByUserId(@Param("userId")String userId, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	//public CallOrder findById(CallOrder callOrder) throws Exception;
	
	//public int countByKey(@Param("key")String key, @Param("type")String type, @Param("state")String state, @Param("warehouseId")String warehouseId,
	//		@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd) throws Exception;
	//public List<CallOrder> findByKey(@Param("key")String key, @Param("type")String type, @Param("state")String state, @Param("warehouseId")String warehouseId,
	//		@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
//	public int audit(CallOrder callOrder) throws Exception;
}
