//定义仓位的基本信息20160205 kai
package com.meitao.model;



public class m_Storage implements java.io.Serializable{
	private static final long serialVersionUID = 7748813652149838823L;
	
	
	private String id;
	private String storeId;//所属门店的id号
	private String channelId;//对应渠道的id号
	private String shelvesNo;//货架号，只能是两位字符，可以是数字或字母
	private String row;//行号，1位字符，可以是数字或字母
	private String column;//列号，1位字符，可以是数字或字母
	private String userId;//所属用户号
	private String type;//类型号，0表示转运州号，1表示本地转动号，2表示运单转运号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getShelvesNo() {
		return shelvesNo;
	}
	public void setShelvesNo(String shelvesNo) {
		this.shelvesNo = shelvesNo;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
