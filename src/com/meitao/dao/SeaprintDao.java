package com.meitao.dao;

import java.util.List;











import org.apache.ibatis.annotations.Param;


import com.meitao.model.Seaprint;


public interface SeaprintDao {
	public int insertSeaprint(Seaprint seaprint) throws Exception;
	public Seaprint getbyid(@Param("id") String id, @Param("storeId") String storeId) throws Exception;
	
	public int countbyseaprintno(@Param("seaprintno") String seaprintno)  throws Exception;;//根据批次号查找数量
	
	public int countOfSearchKeys(@Param("seaprintno") String seaprintno, @Param("storeId") String storeId) throws Exception;

	public List<Seaprint> searchByKeys(@Param("seaprintno") String seaprintno, @Param("index") int index, @Param("size") int size, @Param("storeId") String storeId) throws Exception;
	
	public int deleteseaprint(@Param("id") String id) throws Exception;
	
	

}
