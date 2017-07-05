package com.meitao.model;
//用于记录商品在特定门店的价格信息
import java.io.Serializable;

public class Commodity_price implements Serializable {

	private static final long serialVersionUID = 6137201026424971606L;
	private String id;
	private String price="0";      		 //普通会员价格
	private String vip_0_Price="0";     	 //银牌会员
	private String vip_1_Price="0";      //金牌会员
	private String vip_2_Price="0";      //白金会员
	private String vip_3_Price="0";    //钻金会员
	private String vip_4_Price="0";    //至尊会员1
	private String vip_5_Price="0";    //至尊会员2
	private String vip_6_Price="0";    //至尊会员3
	private String vip_7_Price="0";    //至尊会员4
	private String m_price="0";//网上置单的价格计算
	private String s_price="0";//上门收货的价格
	
	private String storeId;          //门店
	private String cost="0";             //成本
	private String commudityId;//商品类型id
	private String channelId;//所属渠道
	private String state;//显示状态，1表示使用，其它表示不使用，此有门市自决定
	private String modifyDate;//修改时间
	private String createDate;//创建时间
	private Channel channel;
	private CommudityAdmin commudityAdmin;
	
	public CommudityAdmin getCommudityAdmin() {
		return commudityAdmin;
	}
	public void setCommudityAdmin(CommudityAdmin commudityAdmin) {
		this.commudityAdmin = commudityAdmin;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVip_0_Price() {
		return vip_0_Price;
	}
	public void setVip_0_Price(String vip_0_Price) {
		this.vip_0_Price = vip_0_Price;
	}
	public String getVip_1_Price() {
		return vip_1_Price;
	}
	public void setVip_1_Price(String vip_1_Price) {
		this.vip_1_Price = vip_1_Price;
	}
	public String getVip_2_Price() {
		return vip_2_Price;
	}
	public void setVip_2_Price(String vip_2_Price) {
		this.vip_2_Price = vip_2_Price;
	}
	public String getVip_3_Price() {
		return vip_3_Price;
	}
	public void setVip_3_Price(String vip_3_Price) {
		this.vip_3_Price = vip_3_Price;
	}
	public String getVip_4_Price() {
		return vip_4_Price;
	}
	public void setVip_4_Price(String vip_4_Price) {
		this.vip_4_Price = vip_4_Price;
	}
	public String getVip_5_Price() {
		return vip_5_Price;
	}
	public void setVip_5_Price(String vip_5_Price) {
		this.vip_5_Price = vip_5_Price;
	}
	public String getVip_6_Price() {
		return vip_6_Price;
	}
	public void setVip_6_Price(String vip_6_Price) {
		this.vip_6_Price = vip_6_Price;
	}
	public String getVip_7_Price() {
		return vip_7_Price;
	}
	public void setVip_7_Price(String vip_7_Price) {
		this.vip_7_Price = vip_7_Price;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getCommudityId() {
		return commudityId;
	}
	public void setCommudityId(String commudityId) {
		this.commudityId = commudityId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getM_price() {
		return m_price;
	}
	public void setM_price(String m_price) {
		this.m_price = m_price;
	}
	public String getS_price() {
		return s_price;
	}
	public void setS_price(String s_price) {
		this.s_price = s_price;
	}
	
	
	
}
