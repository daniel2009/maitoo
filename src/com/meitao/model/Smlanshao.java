//发送揽收短信参数类
package com.meitao.model;

public class Smlanshao implements java.io.Serializable{
	private static final long serialVersionUID = -7000602606521490402L;
	private String id;
	private String senddate;//发送短信时间
	private String s_senddate;//发送短信的起始时间
	private String e_senddate;//发送短信的结束时间
	
	private String quantity;//数量
	private Product modityDate;//修改时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getS_senddate() {
		return s_senddate;
	}
	public void setS_senddate(String s_senddate) {
		this.s_senddate = s_senddate;
	}
	public String getE_senddate() {
		return e_senddate;
	}
	public void setE_senddate(String e_senddate) {
		this.e_senddate = e_senddate;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Product getModityDate() {
		return modityDate;
	}
	public void setModityDate(Product modityDate) {
		this.modityDate = modityDate;
	}
	
}
