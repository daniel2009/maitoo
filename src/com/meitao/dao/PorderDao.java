package com.meitao.dao;

import java.util.List;



















import org.apache.ibatis.annotations.Param;


import com.meitao.model.Porder;


public interface PorderDao {
	public int insertPorder(Porder porder) throws Exception;

	
	public int countbyorderId(@Param("orderId") String orderId, @Param("storeId") String storeId)  throws Exception;;//根据单号查找个数，用于查找单号是否已经存在
	//orders表示要查找的运单或海关单号
	//sate表示打印状态
	//content表示搜索内容
	//storeId表示所属门店
	//seaprintno 表示所属的批次号
	public int countOfSearchKeys(@Param("seaprintno") String seaprintno,
			@Param("orders") String orders,@Param("state") String state,@Param("content") String content, 
			@Param("storeId") String storeId) throws Exception;
	
	
	public List<Porder> searchByKeys(@Param("seaprintno") String seaprintno,
			@Param("orders") String orders,@Param("state") String state,@Param("content") String content, 
			@Param("storeId") String storeId, @Param("index") int index, @Param("size") int size) throws Exception;
	
	
	public Porder getbyid(@Param("id") String id)  throws Exception;;
	public int modifystate(@Param("id") String id,@Param("state") String state,@Param("modifyDate") String modifyDate)  throws Exception;;
	public int deletebyid(@Param("id") String id)  throws Exception;;
	public int deletebyseaprintid(@Param("seaprintId") String seaprintId)  throws Exception;;//通过批次来删除整个表
}
