//转运的路由记录
package com.meitao.model;

import java.io.Serializable;

public class T_route implements Serializable {

	private static final long serialVersionUID = 5946672489652332388L;
	private String id;
	private String t_id;//转运编号
	private String t_orderId;//转运单号
	private String state;//转运状态
	private String remark;//转运备注
	private String employeeName;//操作员工名称
	private String date;//插入时间
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getT_id() {
		return t_id;
	}
	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	public String getT_orderId() {
		return t_orderId;
	}
	public void setT_orderId(String t_orderId) {
		this.t_orderId = t_orderId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	

	




}
