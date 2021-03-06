/**
 *新闻实体类
 *张敬琦
 *2015-01-30 
 */

package com.meitao.model;

import java.io.Serializable;


public class EndiciaLabel implements Serializable {

	private static final long serialVersionUID = 1567012088660323595L;
	private String id;
	private String userId;
	private String labelType; // 打印类型，是指国内(美国)还是国外
	private String dateAdvance; // 邮资总付邮戳上的日历天数提前。使用时，该元素将通过选择数值范围内的日历天数提前邮戳上的日期。默认范围： (0-7)天。
	private String weightOz;//重量,单位是oz
	private String mailpieceShape;//邮件的形状。
	private String value;//邮件寄送物品的价值。
	private String description;//商品描述
	private String quantity;//数量
	private String weight;//重量
	private String toName;//收件人的姓名
	private String toCompany;//收件人所在公司
	private String toAddress1;//收件人地址
	private String toCity;//收件人所在城市
	private String toState;//收件人所在省
	private String toCountry;//收件人所在国家
	private String toCountryCode;//收件人所在国家代码
	private String toPostalCode;//收件人所在邮编
	private String toZIP4;
	private String toPhone;//收件人电话
	private String fromName;//发件人姓名
	private String returnAddress1;//返回地址
	private String fromCity;//发件人所在城市
	private String fromState;//
	private String fromPostalCode;//发件人邮篇
	private String fromZIP4;//+4美国地址的附加码。
	private String fromPhone;//发件人电话
	private String mailClass;//邮件类型
	
	private String createDate = ""; // 创建时间
	private String modifyDate = ""; // 最后修改时间
	private String labelUrl;//保存的label路径
	private String adminid;//门市管理员的id号，用于后台打单时使用
	
	private String amount;//价格
	private String storeId;//后台员工操作的门店id
	
	private String realmoney;//实际接收的价钱，只有后台可以显示
	private String trackingNumber;//生成运单的快递真实单号
	private String payway;//支付方式，0是会余额支付，1是现金支付
	
	
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getRealmoney() {
		return realmoney;
	}
	public void setRealmoney(String realmoney) {
		this.realmoney = realmoney;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLabelUrl() {
		return labelUrl;
	}
	public void setLabelUrl(String labelUrl) {
		this.labelUrl = labelUrl;
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
	public String getToPostalCode() {
		return toPostalCode;
	}
	public void setToPostalCode(String toPostalCode) {
		this.toPostalCode = toPostalCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getToZIP4() {
		return toZIP4;
	}
	public void setToZIP4(String toZIP4) {
		this.toZIP4 = toZIP4;
	}
	public String getMailClass() {
		return mailClass;
	}
	public void setMailClass(String mailClass) {
		this.mailClass = mailClass;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabelType() {
		return labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
	public String getDateAdvance() {
		return dateAdvance;
	}
	public void setDateAdvance(String dateAdvance) {
		this.dateAdvance = dateAdvance;
	}
	public String getWeightOz() {
		return weightOz;
	}
	public void setWeightOz(String weightOz) {
		this.weightOz = weightOz;
	}
	public String getMailpieceShape() {
		return mailpieceShape;
	}
	public void setMailpieceShape(String mailpieceShape) {
		this.mailpieceShape = mailpieceShape;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getToCompany() {
		return toCompany;
	}
	public void setToCompany(String toCompany) {
		this.toCompany = toCompany;
	}
	public String getToAddress1() {
		return toAddress1;
	}
	public void setToAddress1(String toAddress1) {
		this.toAddress1 = toAddress1;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public String getToState() {
		return toState;
	}
	public void setToState(String toState) {
		this.toState = toState;
	}
	public String getToCountry() {
		return toCountry;
	}
	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}
	public String getToCountryCode() {
		return toCountryCode;
	}
	public void setToCountryCode(String toCountryCode) {
		this.toCountryCode = toCountryCode;
	}
	public String getToPhone() {
		return toPhone;
	}
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getReturnAddress1() {
		return returnAddress1;
	}
	public void setReturnAddress1(String returnAddress1) {
		this.returnAddress1 = returnAddress1;
	}
	public String getFromCity() {
		return fromCity;
	}
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
	public String getFromState() {
		return fromState;
	}
	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	public String getFromPostalCode() {
		return fromPostalCode;
	}
	public void setFromPostalCode(String fromPostalCode) {
		this.fromPostalCode = fromPostalCode;
	}
	public String getFromZIP4() {
		return fromZIP4;
	}
	public void setFromZIP4(String fromZIP4) {
		this.fromZIP4 = fromZIP4;
	}
	public String getFromPhone() {
		return fromPhone;
	}
	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}
	
	//private int viewCount;

}
