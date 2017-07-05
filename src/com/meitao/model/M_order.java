package com.meitao.model;

import java.io.Serializable;
import java.util.List;



public class M_order implements Serializable {

	private static final long serialVersionUID = -5785602319959658890L;
	private String id; // 运单编号
	private String orderId; // 运单号
	private String type; // 运单类型
	private String userId;//归属用户id
	private String payWay;//支付方式
	private String totalmoney;//总费用
	private String totalcost;//总成本
	private String weight;//计费总重量
	private String sjweight;//实际重量
	private String other;//其它费用的总费用
	private String quantity;//商品总数量
	private String tariff;//关税总费用;
	private String value;//申报总价值
	private String insurance;//保险费用
	private String remark;//备注
	private String channelId;//渠道id
	private String channelName;//所属渠道名称
	private String storeId;//归属的门店id
	private String storeName;//所属门市名称
	
	private String employeeId;//操作的员工id
	private String employeeName;//操作人的名称
	private String createDate = ""; // 收件人创建日期
	private String modifyDate = ""; // 修改日期
	private String suserId;//发送用户的id
	private String ruserId;//接收用户的id
	private Send_User suser=null;//发送用户信息
	private Receive_User ruser=null;//接收用户信息
	private List<M_OrderDetail> detail=null;//商品信息
	private User user;//归属的用户信息
	private String state;
	private String payDate;
	private String nowtime;//保存当前提取的时间，主要用了打印
	private String flyno;//航班号
	private String thirdPNS = ""; // 第三方快递公司
	private String thirdNo = ""; // 第三方快递单号
	private String sorderId;//海关单号，即本单号关联海关的单号
	private String automessage;//标记是否自动发短信，1表示发送，0表示不发送，用于门市提交
	private String printway;//打印方式，0表示普通A4打印，1表示打印海关单号
	
	private String payornot;//是否已经支付运费，1表示已经支付，0表示没有支付，其它表示支付状态异常
	private String qremark;//异常备注，用于后台记录运单的异常及必要的备注信息
	
	private String result;//保存一些操作的结果
	
	private String i_weight;//入库重量
	
	private String user_price;//保存高于或等于totalmoney的价格，即当存在多种计算方式时，保存高者
	private String i_state;//入库状态，1表示已经入库
	private String i_employeeId;//入库操作人
	private String i_storeId;//入库门店id
	private String i_employeeName;//入库操作员工名
	private String i_storeName;//入库所在折门店名称
	private String i_date;//入库时间
	private String i_jwweight;
	private String huitongNo;//匹配的汇通单号
	
	private List<String> torderIds;
	
	private List<T_order> torders;//相关的转运单号
	
	
	private String freezId;//冻结资金的id号
	private String tcost;//转运的成本费用
	
	private String tmoney;//转运费用
	
	private String positionId;//保存仓位id
	
	private Shelves_position sposition;//运单仓位
	
	private String confirm_user_pay;//用户支付处理状态，1表示已经处理，其它表示没有处理
	private FreezeMoney freezeMoney;//本运单冻结的钱
	
	public FreezeMoney getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(FreezeMoney freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public String getConfirm_user_pay() {
		return confirm_user_pay;
	}
	public void setConfirm_user_pay(String confirm_user_pay) {
		this.confirm_user_pay = confirm_user_pay;
	}
	public Shelves_position getSposition() {
		return sposition;
	}
	public void setSposition(Shelves_position sposition) {
		this.sposition = sposition;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getTmoney() {
		return tmoney;
	}
	public void setTmoney(String tmoney) {
		this.tmoney = tmoney;
	}
	public String getFreezId() {
		return freezId;
	}
	public void setFreezId(String freezId) {
		this.freezId = freezId;
	}
	public String getTcost() {
		return tcost;
	}
	public void setTcost(String tcost) {
		this.tcost = tcost;
	}
	public List<T_order> getTorders() {
		return torders;
	}
	public void setTorders(List<T_order> torders) {
		this.torders = torders;
	}
	public List<String> getTorderIds() {
		return torderIds;
	}
	public void setTorderIds(List<String> torderIds) {
		this.torderIds = torderIds;
	}
	public String getI_jwweight() {
		return i_jwweight;
	}
	public void setI_jwweight(String i_jwweight) {
		this.i_jwweight = i_jwweight;
	}
	public String getI_weight() {
		return i_weight;
	}
	public void setI_weight(String i_weight) {
		this.i_weight = i_weight;
	}
	public String getHuitongNo() {
		return huitongNo;
	}
	public void setHuitongNo(String huitongNo) {
		this.huitongNo = huitongNo;
	}
	public String getI_date() {
		return i_date;
	}
	public void setI_date(String i_date) {
		this.i_date = i_date;
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
	public String getI_storeId() {
		return i_storeId;
	}
	public void setI_storeId(String i_storeId) {
		this.i_storeId = i_storeId;
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
	public String getUser_price() {
		return user_price;
	}
	public void setUser_price(String user_price) {
		this.user_price = user_price;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}
	public String getPayornot() {
		return payornot;
	}
	public void setPayornot(String payornot) {
		this.payornot = payornot;
	}
	public String getQremark() {
		return qremark;
	}
	public void setQremark(String qremark) {
		this.qremark = qremark;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getPrintway() {
		return printway;
	}
	public void setPrintway(String printway) {
		this.printway = printway;
	}
	public String getAutomessage() {
		return automessage;
	}
	public void setAutomessage(String automessage) {
		this.automessage = automessage;
	}
	public String getSorderId() {
		return sorderId;
	}
	public void setSorderId(String sorderId) {
		this.sorderId = sorderId;
	}
	public String getFlyno() {
		return flyno;
	}
	public void setFlyno(String flyno) {
		this.flyno = flyno;
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
	public String getNowtime() {
		return nowtime;
	}
	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getSuserId() {
		return suserId;
	}
	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}
	public String getRuserId() {
		return ruserId;
	}
	public void setRuserId(String ruserId) {
		this.ruserId = ruserId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSjweight() {
		return sjweight;
	}
	public void setSjweight(String sjweight) {
		this.sjweight = sjweight;
	}
	public List<M_OrderDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<M_OrderDetail> detail) {
		this.detail = detail;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
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
	public Send_User getSuser() {
		return suser;
	}
	public void setSuser(Send_User suser) {
		this.suser = suser;
	}
	public Receive_User getRuser() {
		return ruser;
	}
	public void setRuser(Receive_User ruser) {
		this.ruser = ruser;
	}
	
	

   
}
