package com.meitao.model;

public class AutoSend implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8440710125919477548L;
	private String id;
	private String name;//constant可能会用到的name
	private String cnName;//显示的给用户的name
	private String autoEmail;//0,1
	private String autoMessage;
	private String emailContent;
	private String messageContent;
	private String enMessageContent;
	private String send2Consignor;
	private String send2Recipient;
	private String modifyDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getAutoEmail() {
		return autoEmail;
	}
	public void setAutoEmail(String autoEmail) {
		this.autoEmail = autoEmail;
	}
	public String getAutoMessage() {
		return autoMessage;
	}
	public void setAutoMessage(String autoMessage) {
		this.autoMessage = autoMessage;
	}
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getEnMessageContent() {
		return enMessageContent;
	}
	public void setEnMessageContent(String enMessageContent) {
		this.enMessageContent = enMessageContent;
	}
	public String getSend2Consignor() {
		return send2Consignor;
	}
	public void setSend2Consignor(String send2Consignor) {
		this.send2Consignor = send2Consignor;
	}
	public String getSend2Recipient() {
		return send2Recipient;
	}
	public void setSend2Recipient(String send2Recipient) {
		this.send2Recipient = send2Recipient;
	}
}
