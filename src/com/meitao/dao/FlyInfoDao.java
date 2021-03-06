package com.meitao.dao;

import java.util.List;






import org.apache.ibatis.annotations.Param;

import com.meitao.model.FlyInfo;


public interface FlyInfoDao {
	public int insertFlyInfo(FlyInfo flyinfo) throws Exception;
	public FlyInfo getById(String id) throws Exception;

	public List<FlyInfo> getAll() throws Exception;
	public FlyInfo getByflightno(String flightno) throws Exception;
	
	public int countOfSearchKeys(@Param("flightno") String flightno, @Param("key") String key,
	        @Param("state") String state, @Param("sdate") String sdate, @Param("edate") String edate, @Param("storeId") String storeId) throws Exception;

	public List<FlyInfo> searchByKeys(@Param("flightno") String flightno, @Param("key") String key,
	        @Param("state") String state, @Param("sdate") String sdate, @Param("edate") String edate,@Param("index") int index, @Param("size") int size, @Param("storeId") String storeId) throws Exception;
	
	public int updateFlyInfo(FlyInfo flyinfo) throws Exception;
	public int deleteFlyInfoById(String id) throws Exception;
	/*public int updateWarehouse(Warehouse house) throws Exception;
	
	public int deleteWarehouseByIds(List<String> list) throws Exception;

	public Warehouse getById(String id) throws Exception;

	public List<Warehouse> getAll() throws Exception;

	public int count() throws Exception;

	public List<Warehouse> searchWarehouse(@Param("index") int index, @Param("size") int size, @Param("groupid") String groupid) throws Exception;*/

}
