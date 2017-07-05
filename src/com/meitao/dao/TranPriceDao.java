package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.TranPrice;

public interface TranPriceDao {
	public int inserttranprice(TranPrice tranprice) throws Exception;

	public List<TranPrice> getAllbyWarehouseId(@Param("storeid") String storeid) throws Exception;

	public String getById(String id) throws Exception;
	public List<TranPrice> getAll() throws Exception;
	public TranPrice getby2id(@Param("warehouseId") String warehouseId,@Param("tranwarehouseId") String tranwarehouseId) throws Exception;
	public int modifytranpricebi2id(TranPrice tranprice) throws Exception;
	public int modifytranpricebyid(TranPrice tranprice) throws Exception;
	
}
