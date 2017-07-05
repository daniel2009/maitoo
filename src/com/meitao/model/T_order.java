package com.meitao.model;

import java.io.Serializable;
import java.util.List;



public class T_order implements Serializable {

	private static final long serialVersionUID = -5735602312345958890L;
	private String id; // 运单编号
	private String torderId; // 转运单号
	private String userId;//归属用户id
	private String totalmoney="0";//总费用
	private String totalcost="0";//总成本
	private String weight="0";//计费总重量
	private String sjweight="0";//实际重量
	private String other="0";//其它费用的总费用
	private String quantity="0";//商品总数量
	private String tariff="0";//关税总费用;
	private String value="0";//申报总价值
	private String insurance="0";//保险费用
	private String remark;//备注
	private String channelId="0";//渠道id
	private String channelName;//所属渠道名称
	private String storeId="0";//归属的门店id
	private String storeName;//所属门市名称
	
	private String employeeId="0";//操作的员工id
	private String employeeName;//操作人的名称
	private String createDate = ""; // 收件人创建日期
	private String modifyDate = ""; // 修改日期
	private String ruserId="0";//接收用户的id
	private Receive_User ruser=null;//接收用户信息
	private User user;//归属的用户信息
	private String state;
	
	private String qremark;//异常备注，用于后台记录运单的异常及必要的备注信息
	
	private String result;//保存一些操作的结果
	
	private String order_ids;//生成运单号后保存的运单号
	
	private String type;//转运类型
	
	
	
	private String i_state="0";//入库状态，1表示已经入库
	private String i_employeeId="0";//入库操作人
	private String i_storeId="0";//入库门店id
	private String i_employeeName;//入库操作员工名
	private String i_storeName;//入库所在折门店名称
	private String i_date;//入库时间
	private String i_jwweight="0";
	private String i_weight="0";//入库重量
	
	
	private String positionId;//仓位id
	private Shelves_position position;//仓位信息
	
	private String payway;
	
	private String tremark;
	
	
	public String getTremark() {
		return tremark;
	}

	public void setTremark(String tremark) {
		this.tremark = tremark;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public Shelves_position getPosition() {
		return position;
	}

	public void setPosition(Shelves_position position) {
		this.position = position;
	}

	public String getI_weight() {
		return i_weight;
	}

	public void setI_weight(String i_weight) {
		this.i_weight = i_weight;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	private List<T_route> route;//转运路由

	public List<T_route> getRoute() {
		return route;
	}

	public void setRoute(List<T_route> route) {
		this.route = route;
	}

	public String getI_state() {
		return i_state;
	}

	public void setI_state(String i_state) {
		this.i_state = i_state;
	}

	public String getI_employeeId() {
		return i_employeeId;
	}

	public void setI_employeeId(String i_employeeId) {
		this.i_employeeId = i_employeeId;
	}

	public String getI_storeId() {
		return i_storeId;
	}

	public void setI_storeId(String i_storeId) {
		this.i_storeId = i_storeId;
	}

	public String getI_employeeName() {
		return i_employeeName;
	}

	public void setI_employeeName(String i_employeeName) {
		this.i_employeeName = i_employeeName;
	}

	public String getI_storeName() {
		return i_storeName;
	}

	public void setI_storeName(String i_storeName) {
		this.i_storeName = i_storeName;
	}

	public String getI_date() {
		return i_date;
	}

	public void setI_date(String i_date) {
		this.i_date = i_date;
	}

	public String getI_jwweight() {
		return i_jwweight;
	}

	public void setI_jwweight(String i_jwweight) {
		this.i_jwweight = i_jwweight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTorderId() {
		return torderId;
	}

	public void setTorderId(String torderId) {
		this.torderId = torderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSjweight() {
		return sjweight;
	}

	public void setSjweight(String sjweight) {
		this.sjweight = sjweight;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public String getRuserId() {
		return ruserId;
	}

	public void setRuserId(String ruserId) {
		this.ruserId = ruserId;
	}

	public Receive_User getRuser() {
		return ruser;
	}

	public void setRuser(Receive_User ruser) {
		this.ruser = ruser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getQremark() {
		return qremark;
	}

	public void setQremark(String qremark) {
		this.qremark = qremark;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOrder_ids() {
		return order_ids;
	}

	public void setOrder_ids(String order_ids) {
		this.order_ids = order_ids;
	}
	
	
	
	
	
	

   
}
