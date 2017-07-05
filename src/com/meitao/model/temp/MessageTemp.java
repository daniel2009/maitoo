package com.meitao.model.temp;

import java.util.HashSet;
import java.util.Set;

/*
 * 发送消息，保存的临时值 
 * */
public class MessageTemp {
	private static final long serialVersionUID = -69825987455666L;
	private String phone;//保存电话
	private Set<String> orderids = new HashSet<String>();
	private String state;//保存状态值
	private int number;//保存运单号的个数
	private String remark;//备注
	
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Set<String> getOrderids() {
		return orderids;
	}
	public void setOrderids(Set<String> orderids) {
		this.orderids = orderids;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}



}
