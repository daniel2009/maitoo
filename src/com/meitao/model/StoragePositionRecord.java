package com.meitao.model;

public class StoragePositionRecord implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5083384418809946852L;
	private String id;
	private String storagePositionId;
	private String relateId;
	public StoragePositionRecord(){}
	public StoragePositionRecord(String storagePositionId, String relateId){
		this.storagePositionId = storagePositionId;
		this.relateId = relateId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoragePositionId() {
		return storagePositionId;
	}
	public void setStoragePositionId(String storagePositionId) {
		this.storagePositionId = storagePositionId;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
}
