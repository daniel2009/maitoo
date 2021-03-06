package com.meitao.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Employee implements Serializable {

	private static final long serialVersionUID = 658275226201075890L;
	private String id;
	private String account;
	@JsonIgnore
	private String password;
	private String storeId;
	private String storeName;
	private String signDate;
	private String phone;
	private String picUrl;
	private String groupId;  //add by chenkanghua
	private List<Authority_url> authority;
	private String superadmin;//kai 20151117 增加超级管理员概念
	private String storeMaster;//是否是店主，1表示是店主，其它表示不是店主

	 public String getStoreMaster() {
		return storeMaster;
	}

	public void setStoreMaster(String storeMaster) {
		this.storeMaster = storeMaster;
	}

	public String getSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(String superadmin) {
		this.superadmin = superadmin;
	}

	public String getGroupId()
	  {
	    return this.groupId;
	  }

	  public void setGroupId(String groupId) {
	    this.groupId = groupId;
	  }
	  
	public String getId() {
		return id;
	}

	public List<Authority_url> getAuthority() {
		return authority;
	}

	public void setAuthority(List<Authority_url> authority) {
		this.authority = authority;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
