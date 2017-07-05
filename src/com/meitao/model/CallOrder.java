package com.meitao.model;

import java.io.Serializable;

public class CallOrder implements Serializable {
	private static final long serialVersionUID = 1523603463061132828L;
	private String id;
	private String userId;
	private String sName;
	private String sProvince;
	private String sCity;
	private String sDistrict;
	private String sStreetAddress;
	private String sZipCode;
	private String sPhone;
	private String weight;
	private String quantity;
	private String state;
	private String empId;
	private String createDate;
	private String modifyDate;
	private String type;
	private String appointmentDate;
	private String warehouseId;
	private String remark;
	private String confirmDate;
	
	private String warehouseName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getsProvince() {
		return sProvince;
	}
	public void setsProvince(String sProvince) {
		this.sProvince = sProvince;
	}
	public String getsCity() {
		return sCity;
	}
	public void setsCity(String sCity) {
		this.sCity = sCity;
	}
	public String getsDistrict() {
		return sDistrict;
	}
	public void setsDistrict(String sDistrict) {
		this.sDistrict = sDistrict;
	}
	public String getsStreetAddress() {
		return sStreetAddress;
	}
	public void setsStreetAddress(String sStreetAddress) {
		this.sStreetAddress = sStreetAddress;
	}
	public String getsZipCode() {
		return sZipCode;
	}
	public void setsZipCode(String sZipCode) {
		this.sZipCode = sZipCode;
	}
	public String getsPhone() {
		return sPhone;
	}
	public void setsPhone(String sPhone) {
		this.sPhone = sPhone;
	}
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
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
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
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
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	
}
