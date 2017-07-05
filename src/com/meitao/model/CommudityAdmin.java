package com.meitao.model;

import java.io.Serializable;

public class CommudityAdmin implements Serializable {
	private static final long serialVersionUID = 7838550354788740448L;
	private String id;
	private String name;   		//商品名称
	private String remark;  		//备注
	private String state;//状态，1表示启用，其它表示禁用
	private String channelId;//渠道id

	private String modifyDate;//修改时间
	private String createDate;//修改日期
	private Channel channel;//此商品归属的渠道
	private Commodity_price price;//特定门店和渠道下，此商品的价格列表
	
	public Commodity_price getPrice() {
		return price;
	}
	public void setPrice(Commodity_price price) {
		this.price = price;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	

	
}
