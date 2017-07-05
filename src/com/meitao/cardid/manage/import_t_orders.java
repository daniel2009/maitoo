/*
 * 保存导入的运单信息，用于匹配身份信息。
 * */
package com.meitao.cardid.manage;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class import_t_orders implements Serializable {

	private static final long serialVersionUID = -578133620009368890L;
	private String orderid;//导入的运单号
	private String weight;//导入的重量
	private String boxq;//保存导入的箱数
	private String commudityslist;//导入的商品内容
	private String s_name;//保存发件人的姓名
	private String s_address;//保存发件人的地址
	private String s_dist;//保存发件人的匹信息
	private String s_city;//保存发件人的城市
	private String s_privice;//保存发件人的省
	private String s_zipcode;//保存发件人的邮篇
	
	private String r_name;//保存收件人的姓名
	private String r_address;//保存收件人的地址
	private String r_dist;//保存收件人的匹信息
	private String r_city;//保存收件人的城市
	private String r_privice;//保存收件人的省
	private String r_zipcode;//保存收件人的邮篇
	private String r_phone;//收件人电话
	
	//下面参数不是导入参数
	private String cardid;//保存与数据匹配的身份证号
	private String cardname;//保存与数据匹配的身份姓名，多与模糊查找相关
	private String cardurl;//保存身份证路径
	private String result;//保存匹配结果
	private boolean resultflag;//表示成功或失败
	private String zipurl;//保存压缩路径
	private String cardlibId;//库存中的id号
	
	
	public String getCardlibId() {
		return cardlibId;
	}
	public void setCardlibId(String cardlibId) {
		this.cardlibId = cardlibId;
	}
	public String getZipurl() {
		return zipurl;
	}
	public void setZipurl(String zipurl) {
		this.zipurl = zipurl;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public boolean isResultflag() {
		return resultflag;
	}
	public void setResultflag(boolean resultflag) {
		this.resultflag = resultflag;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getCardurl() {
		return cardurl;
	}
	public void setCardurl(String cardurl) {
		this.cardurl = cardurl;
	}
	public String getR_phone() {
		return r_phone;
	}
	public void setR_phone(String r_phone) {
		this.r_phone = r_phone;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getBoxq() {
		return boxq;
	}
	public void setBoxq(String boxq) {
		this.boxq = boxq;
	}
	public String getCommudityslist() {
		return commudityslist;
	}
	public void setCommudityslist(String commudityslist) {
		this.commudityslist = commudityslist;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getS_address() {
		return s_address;
	}
	public void setS_address(String s_address) {
		this.s_address = s_address;
	}
	public String getS_dist() {
		return s_dist;
	}
	public void setS_dist(String s_dist) {
		this.s_dist = s_dist;
	}
	public String getS_city() {
		return s_city;
	}
	public void setS_city(String s_city) {
		this.s_city = s_city;
	}
	public String getS_privice() {
		return s_privice;
	}
	public void setS_privice(String s_privice) {
		this.s_privice = s_privice;
	}
	public String getS_zipcode() {
		return s_zipcode;
	}
	public void setS_zipcode(String s_zipcode) {
		this.s_zipcode = s_zipcode;
	}
	public String getR_name() {
		return r_name;
	}
	public void setR_name(String r_name) {
		this.r_name = r_name;
	}
	public String getR_address() {
		return r_address;
	}
	public void setR_address(String r_address) {
		this.r_address = r_address;
	}
	public String getR_dist() {
		return r_dist;
	}
	public void setR_dist(String r_dist) {
		this.r_dist = r_dist;
	}
	public String getR_city() {
		return r_city;
	}
	public void setR_city(String r_city) {
		this.r_city = r_city;
	}
	public String getR_privice() {
		return r_privice;
	}
	public void setR_privice(String r_privice) {
		this.r_privice = r_privice;
	}
	public String getR_zipcode() {
		return r_zipcode;
	}
	public void setR_zipcode(String r_zipcode) {
		this.r_zipcode = r_zipcode;
	}
	


	

	

}
