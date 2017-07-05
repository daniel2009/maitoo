package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Product;


public interface ProductDao {

	int insert(Product product) throws Exception;
	Product findById(Product product) throws Exception;
	int update(Product product) throws Exception;
	int delete(Product product) throws Exception;
	
	int countByKey(@Param("product")Product product, @Param("priceStart")String priceStart, @Param("priceEnd")String priceEnd, @Param("userId")String userId) throws Exception;
	List<Product> findByKey(@Param("product")Product product, @Param("priceStart")String priceStart, @Param("priceEnd")String priceEnd, @Param("userId")String userId, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;

	int updateQuantity(Product product) throws Exception;
	
	int countByKeyAndUser(@Param("product")Product product, @Param("priceStart")String priceStart, @Param("priceEnd")String priceEnd) throws Exception;
	List<Product> findByKeyAndUser(@Param("product")Product product, @Param("priceStart")String priceStart, @Param("priceEnd")String priceEnd, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	List<Product> findByIds(@Param("ids")String[] ids, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	List<Product> findByIdsIncludeSame(@Param("ids")String[] ids, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
}
