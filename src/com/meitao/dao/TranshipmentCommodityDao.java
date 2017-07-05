package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.TranshipmentCommodity;


public interface TranshipmentCommodityDao {
	public int insertTranshipmentCommodity(List<TranshipmentCommodity> list) throws Exception;

	// 删除所有给定记录的id值
	public int deleteMultiTranshipmentCommodity(List<String> list) throws Exception;

	public List<TranshipmentCommodity> getById(String id) throws Exception;

	public int countOfTranshipmentIds(List<String> ids) throws Exception;
	
	//kai 20151002 添加根据id获取当前的商品行,此idsn是指真正的表的id
	public TranshipmentCommodity getByIdsn(String idsn) throws Exception;
	
	public int modifyTranshipmentnyIdsn(TranshipmentCommodity billcomm) throws Exception;
	public int deletesimpleTranshipmentCommodity(String idsn) throws Exception;
	public int countTranshipmentCommodity(String id) throws Exception;//计算转运单
	
	public int updatetranidandpackageidbyid(@Param("tranWarehouseId")String tranWarehouseId,@Param("transitNo")String transitNo,@Param("transhipmentId")String transhipmentId) throws Exception;//列据id号更新包裹号和转运id
	
}
