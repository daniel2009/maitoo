package com.meitao.dao;

import java.util.List;

import com.meitao.model.OceanStore;

public interface OceanStoreDao {

	int insert(OceanStore oceanStore) throws Exception;

	OceanStore getById(String id) throws Exception;

	int update(OceanStore oceanStore) throws Exception;

	List<OceanStore> getAll() throws Exception;

	int delete(String id) throws Exception;

}
