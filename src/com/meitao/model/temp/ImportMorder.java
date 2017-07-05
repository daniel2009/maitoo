package com.meitao.model.temp;


public class ImportMorder {
	private static final long serialVersionUID = -6104520153516666L;
	private String orderId;
	private String state;

	private String thirdPNS; // 第三方快递公司
	private String thirdNo; // 第三方快递单号
	private String stateremark;
	private String stateResult;//修改状态的结果描述
	private String messageResult;//保存短信发送结果
	public String getMessageResult() {
		return messageResult;
	}
	public void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getThirdPNS() {
		return thirdPNS;
	}
	public void setThirdPNS(String thirdPNS) {
		this.thirdPNS = thirdPNS;
	}
	public String getThirdNo() {
		return thirdNo;
	}
	public void setThirdNo(String thirdNo) {
		this.thirdNo = thirdNo;
	}
	public String getStateremark() {
		return stateremark;
	}
	public void setStateremark(String stateremark) {
		this.stateremark = stateremark;
	}
	public String getStateResult() {
		return stateResult;
	}
	public void setStateResult(String stateResult) {
		this.stateResult = stateResult;
	}




}
