package com.meitao.dao;

import com.meitao.model.WarehouseLocation;

public interface WarehouseLocationDao {
	public int insertWarehouseLocation(WarehouseLocation wl) throws Exception;
	public int updateWarehouseLocationState(WarehouseLocation wl) throws Exception;
}
