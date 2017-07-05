package com.meitao.model;

import java.io.Serializable;

public class M_OrderDetail implements Serializable  {
	private static final long serialVersionUID = -2196500650315006795L;
	private String id;//id
	private String orderId;//归属运单id
	private String commodityId;//商品id号,指的是商品价格Commodity_price的id
	private String quantity;//商品数量
	private String productName;//品名
	private String brands;//品牌
	private String name;//商品名称
	private String weight;//商品重量
	private String value;//商品价值
	private String tariff;//关税;
	private String other;//其它费用;
	private String or="0";//转运州费用
	private String remark;//备注
	private String ctype;//商品类型，用于区分此商品
	private String cprice="0";//商品的单价
	private String allcprice="0";//商品的总价值
	
	
	
	public String getAllcprice() {
		return allcprice;
	}
	public void setAllcprice(String allcprice) {
		this.allcprice = allcprice;
	}
	public String getCprice() {
		return cprice;
	}
	public void setCprice(String cprice) {
		this.cprice = cprice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTariff() {
		return tariff;
	}
	public void setTariff(String tariff) {
		this.tariff = tariff;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getOr() {
		return or;
	}
	public void setOr(String or) {
		this.or = or;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	
	
 
}
