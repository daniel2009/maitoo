//kai 20160429 导入第三方运单获取得到的数据及结果
package com.meitao.model.temp;

import java.util.List;

import com.meitao.model.M_order;


public class ImportthirdMorder {
	private static final long serialVersionUID = -620320156836666L;
	private List<M_order> orders;//保存上传的运单信息
	private String storeId;//门店id号
	private String channelId;//渠道号
	private String empId;//操作人编号
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public List<M_order> getOrders() {
		return orders;
	}
	public void setOrders(List<M_order> orders) {
		this.orders = orders;
	}



}
