package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.M_OrderDetail;



public interface MdetailDao {
	public int insertMDetail(List<M_OrderDetail> list) throws Exception;
	
	public List<M_OrderDetail> getAllByOrderId(@Param("orderId") String orderId) throws Exception;

	public int deleteByOrderIds(List<String> list) throws Exception;
	
	
	
	
}
