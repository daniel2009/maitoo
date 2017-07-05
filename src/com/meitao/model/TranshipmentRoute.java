package com.meitao.model;

import java.util.Date;

import com.meitao.common.util.DateUtil;

public class TranshipmentRoute implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3741998435403184597L;
	private String id;
	private String transhipmentId;
	private String state;
	private String date;
	private String warehouseId;
	private String remark;
	
	private String warehouseName;
	
	public TranshipmentRoute(){
	}
	public TranshipmentRoute(String transhipmentId, String state, String warehouseId, String remark){
		this.transhipmentId = transhipmentId;
		this.state = state;
		this.warehouseId = warehouseId;
		this.remark = remark;
		this.date = DateUtil.date2String(new Date());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTranshipmentId() {
		return transhipmentId;
	}
	public void setTranshipmentId(String transhipmentId) {
		this.transhipmentId = transhipmentId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
}
