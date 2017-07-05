package com.meitao.model;

import java.io.Serializable;
import java.util.List;

public class OrderBatch implements Serializable {

	private static final long serialVersionUID = 5951926040029645944L;
	private String id;
	private String createDate;
	private List<Order> orders;

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

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
