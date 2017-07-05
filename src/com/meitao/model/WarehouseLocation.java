package com.meitao.model;

import java.io.Serializable;

public class WarehouseLocation implements Serializable {
	private static final long serialVersionUID = 1724526267951882219L;
	private String id; // id
	private String warehouseId; // 所属仓库
	private String name; // 位置名
	private String state; // 状态
	private String modifyDate; // 最后修改时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	
}
