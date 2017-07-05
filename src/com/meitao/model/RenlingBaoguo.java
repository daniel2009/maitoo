/**
 *包裹认领类
 *kai
 *2015-10-19 
 */

package com.meitao.model;

import java.io.Serializable;


public class RenlingBaoguo implements Serializable {

	private static final long serialVersionUID = 1567638188607823635L;
	private String id;	//
	private String warehouseId;//仓库id
	private String userId;//已经认领的用户id
	private String empId;//入库员id
	private String company;//快递公司
	private String state;//包裹状态
	private String baoguoId;//快递的包裹单号
	private String title; // 认领的标题
	private String content; // 认领内容
	private String tranId;//认领后的转运id
	private String pic1;//认领的图片
	private String pic2;//
	private String pic3;
	private String createDate;//创建时间
	private String modifyDate;//修改时间
	private String revName;//收件人
	private String baoguoPhone;//包裹的收件电话
	private String remark;
	
	private String userName;
    private String warehouseName;
    private String empName;
    private String totalAudit;//搜索关联的，需要审核的claimperson数量
    
    private RenlingPersons reninfo;//已认领的人的信息
    
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	
	public RenlingPersons getReninfo() {
		return reninfo;
	}
	public void setReninfo(RenlingPersons reninfo) {
		this.reninfo = reninfo;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBaoguoId() {
		return baoguoId;
	}
	public void setBaoguoId(String baoguoId) {
		this.baoguoId = baoguoId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
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
	public String getRevName() {
		return revName;
	}
	public void setRevName(String revName) {
		this.revName = revName;
	}
	public String getBaoguoPhone() {
		return baoguoPhone;
	}
	public void setBaoguoPhone(String baoguoPhone) {
		this.baoguoPhone = baoguoPhone;
	}
	public String getTotalAudit() {
		return totalAudit;
	}
	public void setTotalAudit(String totalAudit) {
		this.totalAudit = totalAudit;
	}
	

}
