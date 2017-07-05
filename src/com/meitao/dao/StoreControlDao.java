package com.meitao.dao;



import org.apache.ibatis.annotations.Param;



import com.meitao.model.MenshidadanControl;
import com.meitao.model.StoreControl;


public interface StoreControlDao {

	int insert(StoreControl control) throws Exception;
	StoreControl findbyflag(@Param("storeId")String storeId,@Param("flag")String flag) throws Exception;
	
	int update(@Param("storeId")String storeId,@Param("flag")String flag,
			@Param("modifyDate")String modifyDate,@Param("value")String value) throws Exception;
	


}
