package com.meitao.model;

public class SpiderOrder implements java.io.Serializable{
	private static final long serialVersionUID = 4539492348743852610L;
	private String id;
	//网页信息
	private String url;
	private String website;
	private String name;
	private String brand;
	private String currency;
	private String price;
	private String shippingPrice;
	private String weight;
	private String[] pics;
	private String featureHtml;
	private String detailHtml;
	//spiderOrder
	private String state;
	private String createDate;
	private String modifyDate;
	
	//user的信息
	private String quantity;
	private String userId;
	private String userRemark;
	//admin
	private String empId;
	private String empRemark;
	
	
	//附加
	private String userName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getShippingPrice() {
		return shippingPrice;
	}
	public void setShippingPrice(String shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getFeatureHtml() {
		return featureHtml;
	}
	public void setFeatureHtml(String featureHtml) {
		this.featureHtml = featureHtml;
	}
	public String getDetailHtml() {
		return detailHtml;
	}
	public void setDetailHtml(String detailHtml) {
		this.detailHtml = detailHtml;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String[] getPics() {
		return pics;
	}
	public void setPics(String[] pics) {
		this.pics = pics;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpRemark() {
		return empRemark;
	}
	public void setEmpRemark(String empRemark) {
		this.empRemark = empRemark;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
