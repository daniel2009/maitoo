package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.SpencialCommodity;

public interface SpencialCommodityDao {
	public int insertCommodity(SpencialCommodity commodity) throws Exception;

	public List<SpencialCommodity> getCommoditys(@Param("storeId") String storeId,@Param("channelId") String channelId,@Param("countryId") String countryId) throws Exception;

	public String getById(@Param("storeId") String storeId,String id) throws Exception;
	public SpencialCommodity getByOnlyId(@Param("id")String id) throws Exception;

	/***
	 * 查询转运快递单价
	 * @param ids  'commodity'.'id' list
	 * @return 快递单价
	 * @throws Exception
	 */
	public String selectMaxPriceByCommoditys(@Param("storeId") String storeId,@Param("ids") List<String> ids) throws Exception;

	/***
	 * 查询门市快递单价
	 * @param ids  'commodity'.'id' list
	 * @return 快递单价
	 * @throws Exception
	 */
	public String selectMaxMsPriceByCommoditys(@Param("storeId") String storeId,@Param("ids") List<String> ids) throws Exception;

	public String selectMaxVipOnePriceByCommoditys(@Param("storeId") String storeId,@Param("ids") List<String> ids) throws Exception;

	public String selectMaxVipTwoPriceByCommoditys(@Param("storeId") String storeId,@Param("ids") List<String> ids) throws Exception;

	public String selectMaxVipThreePriceByCommoditys(@Param("storeId") String storeId,@Param("ids") List<String> ids) throws Exception;

	public int modifyCommodity(SpencialCommodity commodity) throws Exception;
	
	public int insertCommodityByList(List<SpencialCommodity> list) throws Exception;
	
	public String getPriceById(@Param("priceType") String priceType,@Param("id") String id) throws Exception;
	
	public List<String> getChannelCountrysById(@Param("storeId") String storeId,@Param("channelId") String channelId) throws Exception;//根据渠道id和所属门店的国家id

}
