package com.meitao.dao;



import org.apache.ibatis.annotations.Param;


import com.meitao.model.MenshidadanControl;


public interface MenshidadanControlDao {

	int insert(MenshidadanControl control) throws Exception;
	MenshidadanControl findself(@Param("storeId")String storeId,@Param("employeeId")String employeeId) throws Exception;
	
	int update(@Param("storeId")String storeId,@Param("employeeId")String employeeId,
			@Param("modifyDate")String modifyDate,@Param("printItems")String printItems,@Param("keydownItem")String keydownItem) throws Exception;
	


}
