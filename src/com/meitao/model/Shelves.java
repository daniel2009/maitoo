package com.meitao.model;

import java.io.Serializable;

public class Shelves implements Serializable {

	private static final long serialVersionUID = 6136589254624264606L;
	private String id;
	private String storeId;//所属门店id
	
	private String wNo; //仓位数量
	private String shelvesNo;     	 //货架编号
	private String unused_wNo;      //空闲仓位
	private String used_wNo;//已用仓位
	private String remark;//备注信息
	
	private String createDate;//创建时间
	private String modifyDate;//修改时间
	private String storeName;
	
	private String nickName;//货架别名
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getwNo() {
		return wNo;
	}
	public void setwNo(String wNo) {
		this.wNo = wNo;
	}
	public String getShelvesNo() {
		return shelvesNo;
	}
	public void setShelvesNo(String shelvesNo) {
		this.shelvesNo = shelvesNo;
	}
	public String getUnused_wNo() {
		return unused_wNo;
	}
	public void setUnused_wNo(String unused_wNo) {
		this.unused_wNo = unused_wNo;
	}
	public String getUsed_wNo() {
		return used_wNo;
	}
	public void setUsed_wNo(String used_wNo) {
		this.used_wNo = used_wNo;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	
	
	
}
