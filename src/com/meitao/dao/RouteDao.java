package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Route;


public interface RouteDao {
	public int insertRoute(Route route) throws Exception;

	public List<Route> getRouteByOrderId(@Param("orderId") String orderId) throws Exception;

	public int deleteRouteByOrderIds(List<String> list) throws Exception;
}
