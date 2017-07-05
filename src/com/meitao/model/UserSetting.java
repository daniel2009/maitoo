package com.meitao.model;

public class UserSetting implements java.io.Serializable{
	private static final long serialVersionUID = -7000602652630150402L;
	private String id;
	private String z_store;//在线置单默认门店
	private String z_cid;//在线置单默认渠首
	private String s_store;//上门收货默认门店
	private String userId;
	private String modifyDate;
	private String createDate;
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
	public String getZ_store() {
		return z_store;
	}
	public void setZ_store(String z_store) {
		this.z_store = z_store;
	}
	public String getZ_cid() {
		return z_cid;
	}
	public void setZ_cid(String z_cid) {
		this.z_cid = z_cid;
	}
	public String getS_store() {
		return s_store;
	}
	public void setS_store(String s_store) {
		this.s_store = s_store;
	}
	
	
}
