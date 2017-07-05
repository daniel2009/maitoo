package com.meitao.model;

import java.io.Serializable;

public class ConsigneeInfo implements Serializable {

	private static final long serialVersionUID = -2457007743546559568L;
	private String id; // 收货地址id
	private String name; // 收货人姓名
	private String province; // 省
	private String city; // 市
	private String district; // 区
	private String streetAddress; // 街道地址
	private String zipCode; // 邮编
	private String phone; // 手机号码
	private String telephone; // 固定电话
	private String cardId; // 身份证号码
	private String cardUrl; // 身份证url
	private String cardurlother; // 身份证反面url
	private String cardurltogether;//身份证合成后url

	private String userId; // 该收货地址对应的用户id
	private String lastDate; // 最后操作时间
	
	public String getCardurlother() {
		return cardurlother;
	}

	public void setCardurlother(String cardurlother) {
		this.cardurlother = cardurlother;
	}

	public String getCardurltogether() {
		return cardurltogether;
	}

	public void setCardurltogether(String cardurltogether) {
		this.cardurltogether = cardurltogether;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCardId() {
    	return cardId;
    }

	public void setCardId(String cardId) {
    	this.cardId = cardId;
    }

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public String getUserId() {
    	return userId;
    }

	public void setUserId(String userId) {
    	this.userId = userId;
    }

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
}
