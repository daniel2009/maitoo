package com.meitao.model.temp;

import java.io.Serializable;



public class ImportFlyInfo implements Serializable {

	private static final long serialVersionUID = -5785653620959658890L;
	private String id; // 用户编号，自动生成
	private String orderId; // 要更新的订单号
	private String result; // 更新的结果
	private String messageResult;//发送消息结果
	public String getMessageResult() {
		return messageResult;
	}
	public void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
