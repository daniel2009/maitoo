package com.meitao.model;

import java.util.List;

public class Storage implements java.io.Serializable{
	private static final long serialVersionUID = 7748815975699838823L;
	
	
	private String id;
	private String name;
	private String warehouseId;
	private String type;//transhipment拆包前，transhipment拆包后，order
	private String typeRelateId;//拆包前：要去到的转运州id， 拆包后：-1， order：渠道channelId
	
	private String maxColumn;
	private String maxRow;
	private String totalAvailable;
	private String warehouseName;
	private String typeRelateName;//拆包前：要去到的转运州name， 拆包后：-1/NULL/""， order：渠道channelName
	private List<StoragePosition> positionList;
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
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getMaxColumn() {
		return maxColumn;
	}
	public void setMaxColumn(String maxColumn) {
		this.maxColumn = maxColumn;
	}
	public String getTotalAvailable() {
		return totalAvailable;
	}
	public void setTotalAvailable(String totalAvailable) {
		this.totalAvailable = totalAvailable;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public List<StoragePosition> getPositionList() {
		return positionList;
	}
	public void setPositionList(List<StoragePosition> positionList) {
		this.positionList = positionList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeRelateId() {
		return typeRelateId;
	}
	public void setTypeRelateId(String typeRelateId) {
		this.typeRelateId = typeRelateId;
	}
	public String getTypeRelateName() {
		return typeRelateName;
	}
	public void setTypeRelateName(String typeRelateName) {
		this.typeRelateName = typeRelateName;
	}
	public String getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(String maxRow) {
		this.maxRow = maxRow;
	}
}
