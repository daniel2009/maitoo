package com.meitao.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Message implements Serializable {

	private static final long serialVersionUID = -6702990940932483841L;
	private String id;
	private String userId;
	private String userName;
	private String content;
	private String createDate;
	private String modifyDate;
	private String goupId;
	@JsonIgnore
	private String parentId;
	private String state;
	private List<Message> replyMessages;
	
	public String getGoupId() {
		return this.goupId;
	}

	public void setGoupId(String goupId) {
		this.goupId = goupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public List<Message> getReplyMessages() {
		return replyMessages;
	}

	public void setReplyMessages(List<Message> replyMessages) {
		this.replyMessages = replyMessages;
	}
}
