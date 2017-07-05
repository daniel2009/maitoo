package com.meitao.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.meitao.model.Warehouse;

public interface WarehouseDao {
	public int insertWarehouse(Warehouse warehouse) throws Exception;

	public int updateWarehouse(Warehouse house) throws Exception;
	
	public int deleteWarehouseByIds(List<String> list) throws Exception;

	public Warehouse getById(String id) throws Exception;

	public List<Warehouse> getAll() throws Exception;

	public int count() throws Exception;

	public List<Warehouse> searchWarehouse(@Param("index") int index, @Param("size") int size, @Param("groupid") String groupid) throws Exception;
	
	
	public List<Warehouse> getwarehousebyadmin(@Param("storeids") String storeids) throws Exception;
	public String getNamebyId(String id) throws Exception;
	
	public int countbyadmin(@Param("keyword") String keyword,@Param("storeId") String storeId) throws Exception;
	public List<Warehouse> searchWarehousebyadmin(@Param("keyword") String keyword,@Param("storeId") String storeId,@Param("index") int index, @Param("size") int size) throws Exception;

}
