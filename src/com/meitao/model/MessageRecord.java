package com.meitao.model;
//短信记录
public class MessageRecord implements java.io.Serializable{
	private static final long serialVersionUID = -700061259870150402L;
	private String id;
	private String createDate;
	private String modifyDate;
	private String usa_nos;//美元数量
	private String rmb_nos;//人民币数量
	
	private String type;
	private String remark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUsa_nos() {
		return usa_nos;
	}
	public void setUsa_nos(String usa_nos) {
		this.usa_nos = usa_nos;
	}
	public String getRmb_nos() {
		return rmb_nos;
	}
	public void setRmb_nos(String rmb_nos) {
		this.rmb_nos = rmb_nos;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
