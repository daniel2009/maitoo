package com.meitao.model.temp;
import java.util.List;

import com.meitao.model.Order;
public class ImportthirdOrder {
	List<Order> Orders;
	String[] orderflag;//检查订单是否已经存在
	String[] userflag;//检查用户是否存在

	public List<Order> getOrders() {
		return Orders;
	}
	public void setOrders(List<Order> orders) {
		Orders = orders;
	}
	public String[] getOrderflag() {
		return orderflag;
	}
	public void setOrderflag(String[] orderflag) {
		this.orderflag = orderflag;
	}
	public String[] getUserflag() {
		return userflag;
	}
	public void setUserflag(String[] userflag) {
		this.userflag = userflag;
	}
	
}
