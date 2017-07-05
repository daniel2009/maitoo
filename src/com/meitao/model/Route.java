package com.meitao.model;

import java.io.Serializable;

public class Route implements Serializable {

	private static final long serialVersionUID = 5946672479167332388L;
	private String orderId;
	private String date;
	private String name;
	private String state;
	private String address;
	private String remark;
	
	private String flight;
	private String thrid_pns;
	private String thrid_no;
	private String groupId;
	private String stateRemark;

	
	//kai return ems url 20151113
	private String returnurl;//第三方数据返回是链接的保存
	




	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	public String getStateRemark() {
		return stateRemark;
	}

	public void setStateRemark(String stateRemark) {
		this.stateRemark = stateRemark;
	}
	
	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFlight() {
		return this.flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getThrid_pns() {
		return this.thrid_pns;
	}

	public void setThrid_pns(String thridPns) {
		this.thrid_pns = thridPns;
	}

	public String getThrid_no() {
		return this.thrid_no;
	}

	public void setThrid_no(String thridNo) {
		this.thrid_no = thridNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
