package com.meitao.model;

import java.io.Serializable;



public class FlyInfo implements Serializable {

	private static final long serialVersionUID = -5785623169959658890L;
	private String id; // 用户编号，自动生成
	private String flightno; // 航班号
	private String createDate = ""; // 创建时间
	private String modifyDate = ""; // 最后修改时间
	private String remark = "";
	private String state;
	private String warehouseId;//创建人所属门店ID,用于分属权限
    private String warehouseName;//创建人所属门店名称
    private String ordersno;//运单数量
    
	public String getOrdersno() {
		return ordersno;
	}
	public void setOrdersno(String ordersno) {
		this.ordersno = ordersno;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlightno() {
		return flightno;
	}
	public void setFlightno(String flightno) {
		this.flightno = flightno;
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
	
}
