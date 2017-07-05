package com.meitao.model;

import java.io.Serializable;



import com.meitao.cardid.manage.CardId_lib;

public class Receive_User implements Serializable {

	private static final long serialVersionUID = -5785623160129658890L;
	private String id; // 用户编号，自动生成
	private String phone; // 收件人电话
	private String name; // 收件人姓名
	private String secondName;//第二名称，用于保存与身份证号相对应的名称
	private String address;//地址
	private String zipcode;//邮编
	private String createDate = ""; // 收件人创建日期
	private String modifyDate = ""; // 修改日期
	private String dist;//区县
	private String city;//市
	private String state;//省县
	private String email;//邮箱
	private String company;//公司
	private String useState;//使用状态，1表示显示在前端，其它表示禁肯显示在状态端
	private String cardurl;//身份证正面图片
	private String cardother;//身份证反面图片
	private String cardtogether;//身份证合成图片
	private String cardid;//身份证号
	private String remark;//保存匹配身份证时的备注信息
	private String userId;//归属的用户id
	private String uploadflag;//标志用户自己上传身份证信息，
	private String cardlibId;//调用身份证库存的id
	private CardId_lib cardlib;//调用的身份证库存信息
	private String cardid_flag="0";//后台审核标志，1表示已经审核，其它表示没有审核
	

	public String getCardid_flag() {
		return cardid_flag;
	}
	public void setCardid_flag(String cardid_flag) {
		this.cardid_flag = cardid_flag;
	}
	public String getUploadflag() {
		return uploadflag;
	}
	public void setUploadflag(String uploadflag) {
		this.uploadflag = uploadflag;
	}
	public String getCardlibId() {
		return cardlibId;
	}
	public void setCardlibId(String cardlibId) {
		this.cardlibId = cardlibId;
	}
	public CardId_lib getCardlib() {
		return cardlib;
	}
	public void setCardlib(CardId_lib cardlib) {
		this.cardlib = cardlib;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getUseState() {
		return useState;
	}
	public void setUseState(String useState) {
		this.useState = useState;
	}
	public String getCardurl() {
		return cardurl;
	}
	public void setCardurl(String cardurl) {
		this.cardurl = cardurl;
	}
	public String getCardother() {
		return cardother;
	}
	public void setCardother(String cardother) {
		this.cardother = cardother;
	}
	public String getCardtogether() {
		return cardtogether;
	}
	public void setCardtogether(String cardtogether) {
		this.cardtogether = cardtogether;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
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
