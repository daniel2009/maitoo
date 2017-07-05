package com.meitao.model;

import java.io.Serializable;

public class OrderMidfix implements Serializable {
	private static final long serialVersionUID = 8449581911698059791L;
	private String id;
	private String orderMidfix;
	private String state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderMidfix() {
		return orderMidfix;
	}
	public void setOrderMidfix(String orderMidfix) {
		this.orderMidfix = orderMidfix;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	
}