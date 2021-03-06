package com.meitao.model;

import java.io.Serializable;



public class Seaprint implements Serializable {

	private static final long serialVersionUID = -5785623169901018890L;
	private String id; // 用户编号，自动生成
	private String seaprintno; // 航班号
	private String createDate = ""; // 创建时间
	private String modifyDate = ""; // 最后修改时间
	private String remark = "";

	private String warehouseId;//创建人所属门店ID,用于分属权限
    private String warehouseName;//创建人所属门店名称
    private String pordersno;//打印单的运单数量
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeaprintno() {
		return seaprintno;
	}
	public void setSeaprintno(String seaprintno) {
		this.seaprintno = seaprintno;
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
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getPordersno() {
		return pordersno;
	}
	public void setPordersno(String pordersno) {
		this.pordersno = pordersno;
	}
    
	
	
}
