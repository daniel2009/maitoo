package com.meitao.cardid.manage;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CardId_lib implements Serializable {

	private static final long serialVersionUID = -5781336101259368890L;
	private String id; // 
	private String cname; // 用户名
	private String province; // 省
	private String city; // 市
	private String dist; //县
	private String address; // address
	private String cardid; //身体
	private String createDate; // 创建日期
	private String modifyDate; // 修改日期
	private String picurl;//保存身份证的地址
	private String picflag;//
	private String phone="";//记录拥有人的电话
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPicflag() {
		return picflag;
	}
	public void setPicflag(String picflag) {
		this.picflag = picflag;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	private String result;//操作结果
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
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
