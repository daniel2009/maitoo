//转运路由
package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Route;
import com.meitao.model.T_route;


public interface TrouteDao {
	public int insertTroute(T_route route) throws Exception;

	public List<T_route> getRouteByTid(@Param("t_id") String t_id) throws Exception;

	public int deleteRouteByTids(List<String> list) throws Exception;
}
