package com.meitao.model;

import java.io.Serializable;

public class Commodity implements Serializable {

	private static final long serialVersionUID = 6137201026424971606L;
	private String id;
	private String name;
	
	private String price;      		 //普通会员价格
	private String msPrice;     	 //门市会员价格
	private String vipOnePrice;      //vip会员价格
	private String vipTwoPrice;      //vip2会员价格
	private String vipThreePrice;    //vip3会员价格
	
	private String storeId;          //门市id
	private String cost;             //成本价
	private String firstFee;//首磅附加费
	private String channelId;
	
	public String getFirstFee() {
		return firstFee;
	}

	public void setFirstFee(String firstFee) {
		this.firstFee = firstFee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMsPrice() {
		return msPrice;
	}

	public void setMsPrice(String msPrice) {
		this.msPrice = msPrice;
	}

	public String getVipOnePrice() {
		return vipOnePrice;
	}

	public void setVipOnePrice(String vipOnePrice) {
		this.vipOnePrice = vipOnePrice;
	}

	public String getVipTwoPrice() {
		return vipTwoPrice;
	}

	public void setVipTwoPrice(String vipTwoPrice) {
		this.vipTwoPrice = vipTwoPrice;
	}

	public String getVipThreePrice() {
		return vipThreePrice;
	}

	public void setVipThreePrice(String vipThreePrice) {
		this.vipThreePrice = vipThreePrice;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
