package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.Shelves;


public interface ShelvesDao {
	

	//插入仓位
	public int insert(Shelves shelves) throws Exception;
	
	//获取一个货架
	public Shelves getone(@Param("id")String id,@Param("storeId")String storeId)throws Exception;
	
	public int updateRemark(Shelves shelves)throws Exception;;
	public int countOfsearchShelves(@Param("storeId")String storeId,@Param("wordkey")String wordkey)throws Exception;//计算查找的个数
	public List<Shelves> searchShelves(@Param("storeId")String storeId,@Param("wordkey")String wordkey, @Param("index") int index, @Param("size") int size)throws Exception;;
    public int deleteone(@Param("id")String id) throws Exception;
    //检查是否已经有货架
    public int existShelves(@Param("storeId")String storeId,@Param("shelvesNo")String shelvesNo)  throws Exception;;
    
    public List<Shelves> getbytstoreId(@Param("storeId")String storeId)  throws Exception;//
    
}
