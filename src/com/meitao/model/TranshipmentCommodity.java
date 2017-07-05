package com.meitao.model;

import java.io.Serializable;

public class TranshipmentCommodity implements Serializable {

	private static final long serialVersionUID = -7948212311107942992L;
	private String id;


	private String transhipmentId;
	private String commodityId;
	private String commodityName;
	private String quantity;
	private String sjweight;//一个分类商品的价格
	private String tariff;
	private String commoditySku;//kai 20150928 商品的sku
	private String eachjfweight;//kai 20150928 单件商品价格
	private String transitNo;//商品归属的包裹号
	private Commodity commodity;
	private String tranWarehouseId;//转运的仓库，退转运州的仓库,因为包裹可能存在分合箱，所发转运仓库只能也跟着商品走
	
	public String getTranWarehouseId() {
		return tranWarehouseId;
	}

	public void setTranWarehouseId(String tranWarehouseId) {
		this.tranWarehouseId = tranWarehouseId;
	}

	public String getTransitNo() {
		return transitNo;
	}

	public void setTransitNo(String transitNo) {
		this.transitNo = transitNo;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCommoditySku() {
		return commoditySku;
	}

	public void setCommoditySku(String commoditySku) {
		this.commoditySku = commoditySku;
	}

	public String getEachjfweight() {
		return eachjfweight;
	}

	public void setEachjfweight(String eachjfweight) {
		this.eachjfweight = eachjfweight;
	}

	//private String jfweight;
	private String xiangqing;  //add by chenkanghua
	
	
	public String getXiangqing() {
		return this.xiangqing;
	}

	public void setXiangqing(String xiangqing) {
		this.xiangqing = xiangqing;
	}



	/*public String getJfweight() {
		return this.jfweight;
	}

	public void setJfweight(String jfweight) {
		this.jfweight = jfweight;
	}*/

	public String getSjweight() {
		return this.sjweight;
	}

	public void setSjweight(String sjweight) {
		this.sjweight = sjweight;
	}

	public String getTariff() {
    	return tariff;
    }

	public void setTariff(String tariff) {
    	this.tariff = tariff;
    }

	public String getTranshipmentId() {
		return transhipmentId;
	}

	public void setTranshipmentId(String transhipmentId) {
		this.transhipmentId = transhipmentId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}
}
