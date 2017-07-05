package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;





import com.meitao.model.Commodity_price;



public interface CommudityPriceDao {
	public Commodity_price getById(String id) throws Exception;
	public Commodity_price getByInfo(@Param("commudityId") String commudityId,@Param("channelId") String channelId,@Param("storeId") String storeId,@Param("state") String state) throws Exception;
	
	public List<Commodity_price> getByChannelId(@Param("channelId") String channelId,@Param("state") String state) throws Exception;//根据渠道来获取商品信息,state表示商品的状态
	public int insert(Commodity_price commudity) throws Exception;

	

	public int modifyCost(Commodity_price commudity) throws Exception;//修改成本费用
	
	public int modifyPrice(Commodity_price commudity) throws Exception;//修改商品价格
	public int modifyPricebyid(Commodity_price commudity) throws Exception;//修改商品价格通过id
	

	
	//id表示搜索的商品价格的id,wordkey表示搜索的关键字，channelId表示查找的渠道关键字，ch_state表示查找的渠道的状态，c_state表示状态的状态，state表示商品价格的状态
	public List<Commodity_price> searchByKeys(@Param("id") String id,@Param("channelId") String channelId, @Param("ch_state") String ch_state,@Param("c_state") String c_state,@Param("state") String state,@Param("index") int index, @Param("size") int size) throws Exception;

	
	public int countByKeys(@Param("id") String id,@Param("channelId") String channelId, @Param("ch_state") String ch_state,@Param("c_state") String c_state,@Param("state") String state) throws Exception;
	
	public int deletebyid(@Param("id") String id) throws Exception;
	
	public int deletebyinfo(@Param("commudityId") String commudityId,@Param("channelId") String channelId,@Param("storeId") String storeId) throws Exception;
	
	public int countinfo(@Param("commudityId") String commudityId,@Param("channelId") String channelId,@Param("storeId") String storeId) throws Exception;//计算之前定义的商品信息


	public List<Commodity_price> searchPrice(@Param("commudityId") String commudityId,@Param("storeId") String storeId,@Param("wordkey") String wordkey,@Param("channelId") String channelId,@Param("state") String state,@Param("cstate") String cstate, @Param("index") int index, @Param("size") int size) throws Exception;

	
	public int countBysearchPrice(@Param("commudityId") String commudityId,@Param("storeId") String storeId,@Param("wordkey") String wordkey,@Param("channelId") String channelId,@Param("state") String state,@Param("cstate") String cstate) throws Exception;

	public int deletebycommudityId(@Param("commudityId") String commudityId) throws Exception;

	public int deletebychannelId(@Param("channelId") String channelId) throws Exception;
	
	public int deletebystoreId(@Param("storeId") String storeId) throws Exception;
	
	public List<Commodity_price> getByChannelIdandStoreId(@Param("channelId") String channelId,@Param("storeId") String storeId) throws Exception;//根据渠道和门店来获取价格
	
	public String getPriceById(@Param("priceType") String priceType,@Param("id") String id) throws Exception;
}
