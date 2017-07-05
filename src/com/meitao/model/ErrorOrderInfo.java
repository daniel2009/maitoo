package com.meitao.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ErrorOrderInfo implements Serializable {

	private static final long serialVersionUID = 7058267171713712436L;
	private String id;
	private String orderId;
	private String empName;
	private String content;
	private String createDate;
	private String modifyDate;
	private String state;
	private String groupId;
	@JsonIgnore
	private String parentId;
	private List<ErrorOrderInfo> replyInfos;

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<ErrorOrderInfo> getReplyInfos() {
		return replyInfos;
	}

	public void setReplyInfos(List<ErrorOrderInfo> replyInfos) {
		this.replyInfos = replyInfos;
	}
}
