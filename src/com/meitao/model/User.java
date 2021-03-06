package com.meitao.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class User implements Serializable {

	private static final long serialVersionUID = -5781336169509368890L;
	private String id; // 用户编号，自动生成
	private String nickName; // 昵称，用户名
	@JsonIgnore
	private String password; // 密码
	private String realName; // 真实姓名
	private String phone; // 电话号码
	private String email; // Email
	private String qq; // QQ
	private String recommender; // 推荐人
	private String signDate; // 注册时间
	private String type; // 类型
	private String status; // 状态
	private String country; // 国家
	private String address;
	private String empaccount;// 创建该用户的职工名，如果是在网站注册的，该值为null。

	private String rmbBalance;
	private String usdBalance;
	private String usercode;
	private String useralias;
	private String groupId;
	
	private String phoneState;
	private String emailState;
	private String regType;
	private String modifyDate;
	private String createDate;
	private List<FreezeMoney> freezeMoney;
	

	

	public List<FreezeMoney> getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(List<FreezeMoney> freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public String getPhoneState() {
		return phoneState;
	}

	public void setPhoneState(String phoneState) {
		this.phoneState = phoneState;
	}

	public String getEmailState() {
		return emailState;
	}

	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	
	
	public String getUseralias() {
		return useralias;
	}

	public void setUseralias(String useralias) {
		this.useralias = useralias;
	}

	public String getId() {
		return id;
	}

	public String getRmbBalance() {
		return rmbBalance;
	}

	public void setRmbBalance(String rmbBalance) {
		this.rmbBalance = rmbBalance;
	}

	public String getUsdBalance() {
		return usdBalance;
	}

	public String getAddress() {
    	return address;
    }

	public void setAddress(String address) {
    	this.address = address;
    }

	public void setUsdBalance(String usdBalance) {
		this.usdBalance = usdBalance;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRecommender() {
		return recommender;
	}

	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmpaccount() {
		return empaccount;
	}

	public void setEmpaccount(String empaccount) {
		this.empaccount = empaccount;
	}
}
