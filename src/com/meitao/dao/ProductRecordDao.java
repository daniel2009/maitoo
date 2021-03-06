package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Product;
import com.meitao.model.ProductRecord;

public interface ProductRecordDao {

	int insert(ProductRecord productRecord) throws Exception;
	int updateComment(ProductRecord productRecord) throws Exception;
	int updateByUser(ProductRecord productRecord) throws Exception;
	
	int countByKey(@Param("productRecord")ProductRecord productRecord, @Param("dateStart")String dateStart, @Param("dateEnd")String dateEnd) throws Exception;
	List<ProductRecord> findByKey(@Param("productRecord")ProductRecord productRecord, @Param("dateStart")String dateStart, @Param("dateEnd")String dateEnd, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	
	int countByKeyAndWithComment(@Param("productRecord")ProductRecord productRecord, @Param("dateStart")String dateStart, @Param("dateEnd")String dateEnd) throws Exception;
	List<ProductRecord> findByKeyAndWithComment(@Param("productRecord")ProductRecord productRecord, @Param("dateStart")String dateStart, @Param("dateEnd")String dateEnd, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	
	ProductRecord findById(ProductRecord productRecord) throws Exception;
	int updateState(ProductRecord productRecord) throws Exception;
	int updateStateByAdmin(ProductRecord productRecord) throws Exception;
	int delete(ProductRecord productRecord) throws Exception;
	int countPurchase(Product product) throws Exception;


}
