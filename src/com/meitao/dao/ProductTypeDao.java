package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.ProductType;

public interface ProductTypeDao {

	int insert(ProductType productType) throws Exception;
	int update(ProductType productType) throws Exception;
	int delete(ProductType productType) throws Exception;
	
	ProductType findById(ProductType productType) throws Exception;

	int countByKey(@Param("key")String key) throws Exception;
	List<ProductType> findByKey(@Param("key")String key, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;



}
