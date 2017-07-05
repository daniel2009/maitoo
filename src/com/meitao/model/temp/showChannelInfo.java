package com.meitao.model.temp;


import java.util.List;

/*
 * 发送消息，保存的临时值 
 * */
public class showChannelInfo {
	private static final long serialVersionUID = -6982854892666L;
	private String channelName;//渠道名称
	private String show_remark;//显示前端的渠道说明
	private String show_type;//显示前端的类型说明
	private List<String> commudityName;//商品名称列表
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getShow_remark() {
		return show_remark;
	}
	public void setShow_remark(String show_remark) {
		this.show_remark = show_remark;
	}
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	public List<String> getCommudityName() {
		return commudityName;
	}
	public void setCommudityName(List<String> commudityName) {
		this.commudityName = commudityName;
	}
	
	

}
