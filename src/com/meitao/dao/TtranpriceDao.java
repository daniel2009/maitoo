//转运商品价格设置
package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Route;
import com.meitao.model.T_route;
import com.meitao.model.T_tran_price;


public interface TtranpriceDao {
	public int insert(T_tran_price tranprice) throws Exception;

	public List<T_tran_price> getBystoreId(@Param("storeId") String storeId) throws Exception;//通过门店ID获取商品价格

	public int modify(T_tran_price tranprice) throws Exception;//
	public T_tran_price getoneBystoreId(@Param("storeId") String storeId) throws Exception;//通过门店id获取一个价格信息
}
