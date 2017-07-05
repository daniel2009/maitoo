package com.meitao.model;

import java.io.Serializable;

//存入仓库的基本参数
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1724526267925480139L;
	private String id; // 仓库id
	private String name; // 仓库名称
	private String company; // 仓库地址
	private String country; // 国家
	private String province; // 州
	private String city; // 城市
	private String address; // 地址
	private String zipCode; // 邮编
	private String telephone; // 电话
	private String contactName; // 联系人姓名
	private String remark; // 备注
	private String createDate; // 创建时间
	private String modifyDate; // 修改时间
	private String groupId;//组id
	private String printP_CN;//打印在运单上的中国电话
	private String printP_USA;//打印在运单上的美国电话
	
	private String type;//门店类型，预留
	
	private String callOrderAvailable;//是否上门
	private String callOrderCity;//上门的城市
	
	private String printWidCode;//打印运单的倒数第1、2位的门店标识
	
	private String sendmessage;//是否发送短信，1表示发送
	private String showstore;//前端是否显示门店

	public String getSendmessage() {
		return sendmessage;
	}

	public void setSendmessage(String sendmessage) {
		this.sendmessage = sendmessage;
	}

	public String getShowstore() {
		return showstore;
	}

	public void setShowstore(String showstore) {
		this.showstore = showstore;
	}

	public String getPrintP_CN() {
		return printP_CN;
	}

	public void setPrintP_CN(String printP_CN) {
		this.printP_CN = printP_CN;
	}

	public String getPrintP_USA() {
		return printP_USA;
	}

	public void setPrintP_USA(String printP_USA) {
		this.printP_USA = printP_USA;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCallOrderAvailable() {
		return callOrderAvailable;
	}

	public void setCallOrderAvailable(String callOrderAvailable) {
		this.callOrderAvailable = callOrderAvailable;
	}

	public String getCallOrderCity() {
		return callOrderCity;
	}

	public void setCallOrderCity(String callOrderCity) {
		this.callOrderCity = callOrderCity;
	}

	public String getPrintWidCode() {
		return printWidCode;
	}

	public void setPrintWidCode(String printWidCode) {
		this.printWidCode = printWidCode;
	}

	
}
