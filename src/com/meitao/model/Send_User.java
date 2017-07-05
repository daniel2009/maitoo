package com.meitao.model;

import java.io.Serializable;



public class Send_User implements Serializable {

	private static final long serialVersionUID = -5782303169959658890L;
	private String id; // 用户编号，自动生成
	private String phone; // 发件人电话
	private String name; // 发件人姓名
	private String address;//地址
	private String zipcode;//邮编
	private String createDate = ""; // 发件人创建日期
	private String modifyDate = ""; // 修改日期
	private String dist;//区县
	private String city;//市
	private String state;//省县
	private String email;//邮箱
	private String company;//公司
	private String useState;//使用状态，1表示显示在前端，其它表示禁肯显示在状态端
	private String userId;//归属的用户id
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUseState() {
		return useState;
	}
	public void setUseState(String useState) {
		this.useState = useState;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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
