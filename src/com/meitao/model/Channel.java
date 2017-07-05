package com.meitao.model;

import java.io.Serializable;

public class Channel implements Serializable {
	private static final long serialVersionUID = 7838550354788740448L;
	private String id;
	private String name;   		//渠道名称
	private String alias;  		//渠道别名
	private String warehouseId; //所属仓库
	private String state;
	private String additivePrice;//渠道包裹的附加价格，即原来的价格基础上加上此价格
	private String storeList;//授权使用的门店列表，使用;分号符来隔开的门店id
	private String storeListName;//授权使用的门店名称列表
	private String modifyDate;
	private String createDate;
	
	private String show_remark;//显示前端的渠道说明
	private String show_type;//显示前端的类型说明
	
	private String tran_type;//转运渠道，1表示是转运渠首，其它表示不支持转运渠道
	
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getShow_remark() {
		return show_remark;
	}
	public void setShow_remark(String show_remark) {
		this.show_remark = show_remark;
	}
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getStoreListName() {
		return storeListName;
	}
	public void setStoreListName(String storeListName) {
		this.storeListName = storeListName;
	}
	public String getStoreList() {
		return storeList;
	}
	public void setStoreList(String storeList) {
		this.storeList = storeList;
	}
	public String getAdditivePrice() {
		return additivePrice;
	}
	public void setAdditivePrice(String additivePrice) {
		this.additivePrice = additivePrice;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId; 
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
