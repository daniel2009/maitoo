package com.meitao.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.meitao.model.ErrorTranshipmentBillInfo;

public interface ErrorTranshipmentBillDao {
	public int insertErrorTranshInfo(ErrorTranshipmentBillInfo info) throws Exception;

	/**
	 * 更具id或者父id删除错误运单，只要是id或者父id在list集合只的所有异常预报运单都删除
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int deleteErrorTranshInfoByIds(List<String> list) throws Exception;

	public int updateState(ErrorTranshipmentBillInfo info) throws Exception;

	public List<ErrorTranshipmentBillInfo> getInfosByParentId(@Param("id") String id) throws Exception;

	public List<ErrorTranshipmentBillInfo> searchByTranshId(@Param("transhId") String transhId,
	        @Param("index") int index, @Param("size") int size) throws Exception;

	public int countSearchOfTranshId(@Param("transhId") String transhId) throws Exception;
}
