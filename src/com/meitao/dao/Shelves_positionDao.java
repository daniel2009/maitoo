package com.meitao.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;





import com.meitao.model.Shelves;
import com.meitao.model.Shelves_position;


public interface Shelves_positionDao {
	

	//插入仓位
	public int insert(Shelves_position position) throws Exception;
	
	//获取一个货架
	public Shelves_position getone(@Param("indexId")String indexId)throws Exception;//获取特定货架的一个空闲仓位
	
	public int updatestate(@Param("id")String id,@Param("state")String state,@Param("modifyDate")String modifyDate,@Param("remark")String remark)throws Exception;
	
	public int countOfunused(@Param("indexId")String indexId)throws Exception;//获取没有使用的数量
	public int countOfused(@Param("indexId")String indexId)throws Exception;//获取已使用的数量
	
	public int detelebyindex(@Param("indexId")String indexId)throws Exception;//获取已使用的数量
	
	public int countOfsearchShelvesPosition(@Param("indexId")String indexId,@Param("state")String state,@Param("wordkey")String wordkey)throws Exception;//计算查找的个数
	public List<Shelves_position> searchShelvesPosition(@Param("indexId")String indexId,@Param("state")String state,@Param("wordkey")String wordkey, @Param("index") int index, @Param("size") int size)throws Exception;;
	
	public Shelves_position getbyid(@Param("id")String id);
	
	

}
