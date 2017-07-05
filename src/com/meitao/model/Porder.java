//打印海关运单时使用的数据
package com.meitao.model;

import java.io.Serializable;



public class Porder implements Serializable {

	private static final long serialVersionUID = -5785623169901018890L;
	private String id; // 用户编号，自动生成
	private String orderId; // 运单id
	private String divideOrderId ; // 分单号，即海关单号
	private String commuditylists ; // 品名
	private String taxCommuditylists;//报关品名
	private String brands;//品牌
	private String guiguo;//

	private String numbers;//件数
    private String weight;//重量
    private String sendname;//发货人名称
    
    private String value;//
    private String sendcompany;//发货人公司
    private String sendaddress;//
    private String revcompany;//收
    private String cprovince;//
    
    private String ccity;//
    private String cdist;//
    private String caddress;//
    private String cname;//
    private String cphone;//
    private String boxno;//
    private String cardId;//
    private String printflag;
    private String seaprintId;
    
    private String repeatflag;
    
    private String result;//保存操作结果
    private String createDate;
    private String modifyDate;
    private String markresult;//标识匹配身份证结果

    
	public String getMarkresult() {
		return markresult;
	}
	public void setMarkresult(String markresult) {
		this.markresult = markresult;
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRepeatflag() {
		return repeatflag;
	}
	public void setRepeatflag(String repeatflag) {
		this.repeatflag = repeatflag;
	}
	public String getSeaprintId() {
		return seaprintId;
	}
	public void setSeaprintId(String seaprintId) {
		this.seaprintId = seaprintId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDivideOrderId() {
		return divideOrderId;
	}
	public void setDivideOrderId(String divideOrderId) {
		this.divideOrderId = divideOrderId;
	}
	public String getCommuditylists() {
		return commuditylists;
	}
	public void setCommuditylists(String commuditylists) {
		this.commuditylists = commuditylists;
	}
	public String getTaxCommuditylists() {
		return taxCommuditylists;
	}
	public void setTaxCommuditylists(String taxCommuditylists) {
		this.taxCommuditylists = taxCommuditylists;
	}
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
	public String getGuiguo() {
		return guiguo;
	}
	public void setGuiguo(String guiguo) {
		this.guiguo = guiguo;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSendname() {
		return sendname;
	}
	public void setSendname(String sendname) {
		this.sendname = sendname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSendcompany() {
		return sendcompany;
	}
	public void setSendcompany(String sendcompany) {
		this.sendcompany = sendcompany;
	}
	public String getSendaddress() {
		return sendaddress;
	}
	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}
	public String getRevcompany() {
		return revcompany;
	}
	public void setRevcompany(String revcompany) {
		this.revcompany = revcompany;
	}
	public String getCprovince() {
		return cprovince;
	}
	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}
	public String getCcity() {
		return ccity;
	}
	public void setCcity(String ccity) {
		this.ccity = ccity;
	}
	public String getCdist() {
		return cdist;
	}
	public void setCdist(String cdist) {
		this.cdist = cdist;
	}
	public String getCaddress() {
		return caddress;
	}
	public void setCaddress(String caddress) {
		this.caddress = caddress;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCphone() {
		return cphone;
	}
	public void setCphone(String cphone) {
		this.cphone = cphone;
	}
	public String getBoxno() {
		return boxno;
	}
	public void setBoxno(String boxno) {
		this.boxno = boxno;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getPrintflag() {
		return printflag;
	}
	public void setPrintflag(String printflag) {
		this.printflag = printflag;
	}
    
	
    
	
	
}
