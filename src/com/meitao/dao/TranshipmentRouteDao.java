package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.TranshipmentRoute;

public interface TranshipmentRouteDao {

	List<TranshipmentRoute> findByTranshipmentBillId(@Param("transhipmentId")String transhipmentId) throws Exception;

	int insert(TranshipmentRoute transhipmentRoute) throws Exception;

}
