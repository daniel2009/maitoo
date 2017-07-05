package com.meitao.model;

public class MenshidadanControl implements java.io.Serializable{
	private static final long serialVersionUID = -701258637076150402L;
	private String id;
	private String storeId;//门店id号
	private String employeeId;//员工id号
	private String createDate;//创建时间
	
	private String modifyDate;//修改时间
	private String printItems;//打印显示在前端的打印选项
	private String keydownItem;//当门市打单按下回车时，执行打单的按钮
	public String getPrintItems() {
		return printItems;
	}
	public void setPrintItems(String printItems) {
		this.printItems = printItems;
	}
	public String getKeydownItem() {
		return keydownItem;
	}
	public void setKeydownItem(String keydownItem) {
		this.keydownItem = keydownItem;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	
}
