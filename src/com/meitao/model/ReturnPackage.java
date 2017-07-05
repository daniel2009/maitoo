package com.meitao.model;

public class ReturnPackage implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6270110964140967390L;
	private String id;
	private String userId;
	private String transhipmentId;
	private String packageNo;
	private String shippingFee;//管理员输入
	private String pic1;
	private String receipt;
	private String phone;
	private String address;
	private String pic2;
	private String pic3;
	private String pic4;
	private String pic5;
	private String remark;
	private String createDate;
	private String modifyDate;
	private String empId;
	private String empRemark;
	private String empExpressNo;
	private String empExpressCompany;
	private String empPic;
	private String state;
	private String transhipmentPreState;//提交退货前的transhipment。state
	
	private String packageNoInTranshipment;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmpRemark() {
		return empRemark;
	}
	public void setEmpRemark(String empRemark) {
		this.empRemark = empRemark;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmpExpressNo() {
		return empExpressNo;
	}
	public void setEmpExpressNo(String empExpressNo) {
		this.empExpressNo = empExpressNo;
	}
	public String getEmpPic() {
		return empPic;
	}
	public void setEmpPic(String empPic) {
		this.empPic = empPic;
	}
	public String getEmpExpressCompany() {
		return empExpressCompany;
	}
	public void setEmpExpressCompany(String empExpressCompany) {
		this.empExpressCompany = empExpressCompany;
	}
	public String getPackageNoInTranshipment() {
		return packageNoInTranshipment;
	}
	public void setPackageNoInTranshipment(String packageNoInTranshipment) {
		this.packageNoInTranshipment = packageNoInTranshipment;
	}
	public String getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getTranshipmentId() {
		return transhipmentId;
	}
	public void setTranshipmentId(String transhipmentId) {
		this.transhipmentId = transhipmentId;
	}
	public String getTranshipmentPreState() {
		return transhipmentPreState;
	}
	public void setTranshipmentPreState(String transhipmentPreState) {
		this.transhipmentPreState = transhipmentPreState;
	}
}
