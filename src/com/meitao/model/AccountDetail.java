package com.meitao.model;

import java.io.Serializable;

public class AccountDetail implements Serializable {

	private static final long serialVersionUID = -7715776590686348690L;
	private String id;
	private String userId;
	private String creditId;
	private String amount;
	private String currency;
	private String name;
	private String type;
	private String state;
	private String empId;
	private String empName;
	private String empStore;
	private String groupId;
	private String groutId;

	private String createDate;
	private String modifyDate;
	private String remark;

	private User user;
	private Credit credit;
	private String realAmount;
	
	private String storeId;//操作门店的id
	private String confirm_state;//管理员确认状态，1表示已经确认，其它表示没有确认
	private String admin_remark;//管理员确认备注,管理员确认此笔收款后，即进行此操作，只要用于确认现金收款
	

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getConfirm_state() {
		return confirm_state;
	}

	public void setConfirm_state(String confirm_state) {
		this.confirm_state = confirm_state;
	}

	public String getAdmin_remark() {
		return admin_remark;
	}

	public void setAdmin_remark(String admin_remark) {
		this.admin_remark = admin_remark;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpStore() {
		return empStore;
	}

	public void setEmpStore(String empStore) {
		this.empStore = empStore;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroutId() {
		return groutId;
	}

	public void setGroutId(String groutId) {
		this.groutId = groutId;
	}


	public User getUser() {
    	return user;
    }

	public void setUser(User user) {
    	this.user = user;
    }

	public Credit getCredit() {
    	return credit;
    }

	public String getRealAmount() {
    	return realAmount;
    }

	public void setRealAmount(String realAmount) {
    	this.realAmount = realAmount;
    }

	public void setCredit(Credit credit) {
    	this.credit = credit;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
