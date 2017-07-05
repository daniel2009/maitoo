package com.meitao.model;

import java.io.Serializable;

public class T_tran_price implements Serializable {

	private static final long serialVersionUID = 5941597639167332388L;
	private String id;
	private String storeId;
	private String cost="0";
	private String price="0";
	private String self_price="0";
	private String createDate;
	private String modifyDate;
	private String storeName;
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSelf_price() {
		return self_price;
	}
	public void setSelf_price(String self_price) {
		this.self_price = self_price;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	


}
