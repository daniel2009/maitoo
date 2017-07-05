package com.meitao.model;
/*
 * 20160407
 * kai
 * endicial的参数配置表格
 * 
 * 
 * */
public class Endicial_arg implements java.io.Serializable{
	private static final long serialVersionUID = -7000602607076150402L;
	private String id;
	private String accountId;
	private String passPhrase;
	private String requesterId;
	
	private String labelSize;
	private String imageFormat;
	
	private String test;
	private String apiUrl;
	private String createDate;
	private String modifyDate;
	private String authWarehouseId;//授权可以使用此功能的门店，门店id之间使用";"来分隔
	
	//会员价格
	private String user_price_0;//普通会员
	private String user_price_1;
	private String user_price_2;
	private String user_price_3;
	private String user_price_4;
	private String user_price_5;
	private String user_price_6;
	private String user_price_7;
	private String user_price_8;
	private String userprice;

	

	
	
	public String getUserprice() {
		return userprice;
	}
	public void setUserprice(String userprice) {
		this.userprice = userprice;
	}
	public String getUser_price_0() {
		return user_price_0;
	}
	public void setUser_price_0(String user_price_0) {
		this.user_price_0 = user_price_0;
	}
	public String getUser_price_1() {
		return user_price_1;
	}
	public void setUser_price_1(String user_price_1) {
		this.user_price_1 = user_price_1;
	}
	public String getUser_price_2() {
		return user_price_2;
	}
	public void setUser_price_2(String user_price_2) {
		this.user_price_2 = user_price_2;
	}
	public String getUser_price_3() {
		return user_price_3;
	}
	public void setUser_price_3(String user_price_3) {
		this.user_price_3 = user_price_3;
	}
	public String getUser_price_4() {
		return user_price_4;
	}
	public void setUser_price_4(String user_price_4) {
		this.user_price_4 = user_price_4;
	}
	public String getUser_price_5() {
		return user_price_5;
	}
	public void setUser_price_5(String user_price_5) {
		this.user_price_5 = user_price_5;
	}
	public String getUser_price_6() {
		return user_price_6;
	}
	public void setUser_price_6(String user_price_6) {
		this.user_price_6 = user_price_6;
	}
	public String getUser_price_7() {
		return user_price_7;
	}
	public void setUser_price_7(String user_price_7) {
		this.user_price_7 = user_price_7;
	}
	public String getUser_price_8() {
		return user_price_8;
	}
	public void setUser_price_8(String user_price_8) {
		this.user_price_8 = user_price_8;
	}
	public String getAuthWarehouseId() {
		return authWarehouseId;
	}
	public void setAuthWarehouseId(String authWarehouseId) {
		this.authWarehouseId = authWarehouseId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPassPhrase() {
		return passPhrase;
	}
	public void setPassPhrase(String passPhrase) {
		this.passPhrase = passPhrase;
	}
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getLabelSize() {
		return labelSize;
	}
	public void setLabelSize(String labelSize) {
		this.labelSize = labelSize;
	}
	public String getImageFormat() {
		return imageFormat;
	}
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
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
