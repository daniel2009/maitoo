package com.meitao.model;

import java.io.Serializable;

public class FreezeMoney implements Serializable {

	private static final long serialVersionUID = 6137208956724971606L;
	private String id;
	private String rmb="0";//人民币
	
	private String usa="0"; //美元
	private String createDate;     	 //创建时间
	private String userId;//用户id
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRmb() {
		return rmb;
	}
	public void setRmb(String rmb) {
		this.rmb = rmb;
	}
	public String getUsa() {
		return usa;
	}
	public void setUsa(String usa) {
		this.usa = usa;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	
}
