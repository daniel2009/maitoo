package com.meitao.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
	private static final long serialVersionUID = -2196500650367006795L;
	private String orderId;
	private String transhipmentId;
	private String commodityId;
	private String quantity;
	private String name;
	private String weight;
	private String tariff;
	private String or;
	private String other;
	private String remark;
	private String ctype;
	private String sjweight;
	//private String jfweight;
	private String xiangqing;
	private String transhipNo;
	private String jwweight;//进位前的重量
	
	private String brands;

	public String getJwweight() {
		return jwweight;
	}

	public void setJwweight(String jwweight) {
		this.jwweight = jwweight;
	}

	public String getBrands() {
		return brands;
	}

	public void setBrands(String brands) {
		this.brands = brands;
	}

	public String getSjweight() {
		return this.sjweight;
	}

	public void setSjweight(String sjweight) {
		this.sjweight = sjweight;
	}

	/*public String getJfweight() {
		return this.jfweight;
	}

	public void setJfweight(String jfweight) {
		this.jfweight = jfweight;
	}*/

	public String getXiangqing() {
		return this.xiangqing;
	}

	public void setXiangqing(String xiangqing) {
		this.xiangqing = xiangqing;
	}
	  
    public String getTranshipNo() {
    	return transhipNo;
    }

	public void setTranshipNo(String transhipNo) {
    	this.transhipNo = transhipNo;
    }

	public String getCtype() {
    	return ctype;
    }

	public void setCtype(String ctype) {
    	this.ctype = ctype;
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

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public String getOr() {
		return or;
	}

	public void setOr(String or) {
		this.or = or;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTranshipmentId() {
		return transhipmentId;
	}

	public void setTranshipmentId(String transhipmentId) {
		this.transhipmentId = transhipmentId;
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
}
