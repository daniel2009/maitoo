package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Storage;

public interface StorageDao {

	int insert(Storage storage) throws Exception;

	int countByWarehouseId(@Param("warehouseId")String warehouseId) throws Exception;

	List<Storage> findByWarehouseId(@Param("warehouseId")String warehouseId,@Param("type")String type , @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;

	int delete(@Param("id")String id) throws Exception;

	int countByWarehouseIdAndTypeAndRelateId(@Param("warehouseId")String warehouseId, @Param("type")String type, @Param("typeRelateId")String typeRelateId)  throws Exception;;

	List<Storage> findByWarehouseIdAndTypeAndRelateId(@Param("warehouseId")String warehouseId, @Param("type")String type, @Param("typeRelateId")String typeRelateId, 
			@Param("firstResult")int firstResult, @Param("pageSize")int pageSize)  throws Exception;;

	Storage findById(@Param("id")String id) throws Exception;
	
	List<Storage> findByWarehouseIdAndTypeAndRelateIdList(@Param("warehouseId")String warehouseId, @Param("type")String type, @Param("typeRelateId")String typeRelateId)  throws Exception;;

}
