package com.meitao.model;

import java.io.Serializable;

public class Shelves_position implements Serializable {

	private static final long serialVersionUID = 6136231254624264606L;
	private String id;
	private String indexId;//货架的存储id
	
	private String position; //货架的仓位编号
	private String modifyDate;     	 //修改时间
	private String createDate;      //创建时间
	private String state;//使用状态，1表示已经使用
	private String remark;//保存此仓位的信息，一般记录使用此仓位的运单号
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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

	
	
	
	
}
