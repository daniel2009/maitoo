package com.meitao.model;

import java.io.Serializable;



public class SendUser implements Serializable {

	private static final long serialVersionUID = -5785623169959658890L;
	private String id; // 用户编号，自动生成
	private String phone; // 发件人电话
	private String name; // 发件人姓名
	private String createDate = ""; // 发件人创建日期
	private String modifyDate = ""; // 修改日期
	private String userid;//保存归属用户的id
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
