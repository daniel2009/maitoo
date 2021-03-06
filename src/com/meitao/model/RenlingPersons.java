/**
 *用户包裹认领类，用于用户提交证据
 *kai
 *2015-10-19 
 */

package com.meitao.model;

import java.io.Serializable;


public class RenlingPersons implements Serializable {

	private static final long serialVersionUID = 1567638188607823635L;
	private String id;	//
	private String warehouseId;
	private String userId;//已经认领的用户id
	private String renlingId;//快递公司
	private String baoguoId;//快递的包裹单号
	private String baoguoRemark; // 包含描述内容
	private String auditRemark;
	private String state;
	private String phone;
	private String userName;
	private String pic1;//认领的图片
	private String pic2;//
	private String pic3;
	private String pic4;//
	private String pic5;
	private String createDate;//创建时间
	private String modifyDate;//修改时间
	private String createPerson;//创建人的类型，0表示用户建立，1表示管理员建立
	private RenlingBaoguo renlingBaoguo;
	

	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRenlingId() {
		return renlingId;
	}
	public void setRenlingId(String renlingId) {
		this.renlingId = renlingId;
	}
	public String getBaoguoId() {
		return baoguoId;
	}
	public void setBaoguoId(String baoguoId) {
		this.baoguoId = baoguoId;
	}
	public String getBaoguoRemark() {
		return baoguoRemark;
	}
	public void setBaoguoRemark(String baoguoRemark) {
		this.baoguoRemark = baoguoRemark;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}
	public String getPic4() {
		return pic4;
	}
	public void setPic4(String pic4) {
		this.pic4 = pic4;
	}
	public String getPic5() {
		return pic5;
	}
	public void setPic5(String pic5) {
		this.pic5 = pic5;
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
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public RenlingBaoguo getRenlingBaoguo() {
		return renlingBaoguo;
	}
	public void setRenlingBaoguo(RenlingBaoguo renlingBaoguo) {
		this.renlingBaoguo = renlingBaoguo;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	


}
