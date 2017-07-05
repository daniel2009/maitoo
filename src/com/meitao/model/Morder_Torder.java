package com.meitao.model;

import java.io.Serializable;
import java.util.List;



public class Morder_Torder implements Serializable {

	private static final long serialVersionUID = -5785602254859658390L;
	private String id; //
	private String orderId; // 运单号
	private String torderId; // 转运编号
	private String date;
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
	public String getTorderId() {
		return torderId;
	}
	public void setTorderId(String torderId) {
		this.torderId = torderId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	

   
}
