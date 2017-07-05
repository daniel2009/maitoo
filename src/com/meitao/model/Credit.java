package com.meitao.model;

import java.io.Serializable;

public class Credit implements Serializable {
	private static final long serialVersionUID = 3644937711009295150L;
	private String id;
	private String userId;
	private String name;
	private String address1;
	private String address2;
	private String city;
	private String province;
	private String zipCode;
	private String type;
	private String creditNo;
	private String creditSecurityCode;
	private String expireYear;
	private String expireMonth;
	private String modifyDate;
	
	private String groupId;

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}

	public String getCreditSecurityCode() {
		return creditSecurityCode;
	}

	public void setCreditSecurityCode(String creditSecurityCode) {
		this.creditSecurityCode = creditSecurityCode;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getModifyDate() {
    	return modifyDate;
    }

	public void setModifyDate(String modifyDate) {
    	this.modifyDate = modifyDate;
    }
}
