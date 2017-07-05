package com.meitao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.codehaus.jackson.annotate.JsonIgnore;

import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.StringUtil;

public class Order implements Serializable {

	private static final long serialVersionUID = -6311805699711040060L;
	private String id; // 系统id，主键自增长
	private String orderId = ""; // 运单id, string
	private String warehouseId; // 仓库id
	private String userId; // 用户id
	private String consigneeId; // 收件人id，如果有的话
	private String cName = ""; // 收件人姓名
	private String cProvince = ""; // 收件人省份
	private String cCity = ""; // 收件人城市
	private String cDistrict = ""; // 收件人区市
	private String cStreetAddress = ""; // 收件人具体地址
	private String cZipCode = ""; // 收件人邮编
	private String cPhone = ""; // 电话号码
	private String weight; // 重量
	private String jwweight;//进位前的重量总计
	private String tariff; // 关税
	private String or; // 转运费
	private String other; // 其他费用
	private String premium; // 保险金额
	private String parceValue; // 预报价值
	private String remark = ""; // 备注
	private String state = ""; // 状态
	private String createDate = ""; // 创建时间
	private String modifyDate = ""; // 最后修改时间
	private String mail = ""; // 邮寄方式
	private String empId; // 职工id，门市件就会存在次字段
	private String storeId; // 门店id
	private String payDate = ""; // 支付时间
	
	private String length; // 长  add by chenkanghua 
	private String width; // 宽     add by chenkanghua 
	private String height; // 高 add by chenkanghua 
	private String cardUrl; 
	private String cardId; //身份证

	private String thirdPNS = ""; // 第三方快递公司
	private String thirdNo = ""; // 第三方快递单号
	private String flight = ""; // 航班信息
	private String batchId; // 批次号
	private String totalMoney;// 总运费
	private String groupId;
	private String type; // 门市件为1
	private String orderIdPrefix;
	private String quantity;
	private OrderDetail[] details;
	private List<String> orderDetailTranshipmentIds;
	private List<String> transhipmentIds;
	private List<Route> routes;
	private User user;
	private Warehouse warehouse;
	@JsonIgnore
	private String[][] orderCommoditys;
	@JsonIgnore
	private String userName;
	private String channelId;
	private Channel channel;


	// kai 20150912 添加重量单位为kg的字段,唯一快递要求
	private String weightKg;
	// private String sendusername;//添加发件人姓名，用于不用注册即可在线提交
	// kai 20150916
	private String addressAll;// 收件地址的全称，即由收件人的省市区详细地址组成
	private String NamePinyi;// 记录收件人的拼音
	private String expressName;// 快递公司名称，只要用于批量上传
	
	private String senduserphone;//发件人电话
	private String senduserAddress;//发件人地址
	private String senduserName;
	
	private String cardurlother;//身份证图片反面
	private String cardurltogether;//身份证图片合成后图片
	private String tranPro;//kai 20151003 转运单处理状态
	
	private String thirdCreateDate;//第三方导入时的收件时间
	private String boxNo;//集装箱号
	private String payOrNot;//第三导入时标识是否已经付款
	private String cardOrNot;//第三方导入时标记是否有身份证
	private String commodityThirdList;//第三方导入时的商品信息
	private String importRemark;//导入信息备注
	//private String orderType;//订单的类型，主要用于区别是第三方订单还是本系统生成的订单
	private String baseMoney;//基本费用
	private String stateRemark;//状态说明
	private String thirdBrands;
	
	private String CommodityCost;//kai 保存商品成本价
	
	private String senduserZipcode;//kai 发送人邮篇
	private String senduserCity;//kai 发送人所在城市
	private String senduserState;//kai 发送人所在州
	
	private String payChangeWarehouse;//在门店间花去的费用
	private String payToChina;//在运回中国花去的费用
	
	private StoragePosition storagePosition;
	private String typeKey;//保存类型分类，如是门市运单，转动运单等
	
	private String seaNo;//海关单号 
	
	private String result;//用于保存更新数据时，插入数据
	private String newdate;//保存提取数据的时间


	public String getNewdate() {
		return newdate;
	}

	public void setNewdate(String newdate) {
		this.newdate = newdate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSeaNo() {
		return seaNo;
	}

	public void setSeaNo(String seaNo) {
		this.seaNo = seaNo;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public String getJwweight() {
		return jwweight;
	}

	public void setJwweight(String jwweight) {
		this.jwweight = jwweight;
	}

	public String getSenduserZipcode() {
		return senduserZipcode;
	}

	public void setSenduserZipcode(String senduserZipcode) {
		this.senduserZipcode = senduserZipcode;
	}

	public String getSenduserCity() {
		return senduserCity;
	}

	public void setSenduserCity(String senduserCity) {
		this.senduserCity = senduserCity;
	}

	public String getSenduserState() {
		return senduserState;
	}

	public void setSenduserState(String senduserState) {
		this.senduserState = senduserState;
	}

	public String getCommodityCost() {
		return CommodityCost;
	}

	public void setCommodityCost(String commodityCost) {
		CommodityCost = commodityCost;
	}

	public String getThirdBrands() {
		return thirdBrands;
	}

	public void setThirdBrands(String thirdBrands) {
		this.thirdBrands = thirdBrands;
	}

	public String getNamePinyi() {
		return NamePinyi;
	}

	public void setNamePinyi(String namePinyi) {
		NamePinyi = namePinyi;
	}

	public String getStateRemark() {
		return stateRemark;
	}

	public void setStateRemark(String stateRemark) {
		this.stateRemark = stateRemark;
	}

	public String getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(String baseMoney) {
		this.baseMoney = baseMoney;
	}

	public String getThirdCreateDate() {
		return thirdCreateDate;
	}

	public void setThirdCreateDate(String thirdCreateDate) {
		this.thirdCreateDate = thirdCreateDate;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getPayOrNot() {
		return payOrNot;
	}

	public void setPayOrNot(String payOrNot) {
		this.payOrNot = payOrNot;
	}

	public String getCardOrNot() {
		return cardOrNot;
	}

	public void setCardOrNot(String cardOrNot) {
		this.cardOrNot = cardOrNot;
	}

	public String getCommodityThirdList() {
		return commodityThirdList;
	}

	public void setCommodityThirdList(String commodityThirdList) {
		this.commodityThirdList = commodityThirdList;
	}

	public String getImportRemark() {
		return importRemark;
	}

	public void setImportRemark(String importRemark) {
		this.importRemark = importRemark;
	}

	/*public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}*/

	public String getTranPro() {
		return tranPro;
	}

	public void setTranPro(String tranPro) {
		this.tranPro = tranPro;
	}
	

	public String getCardurlother() {
		return cardurlother;
	}

	public void setCardurlother(String cardurlother) {
		this.cardurlother = cardurlother;
	}

	public String getCardurltogether() {
		return cardurltogether;
	}

	public void setCardurltogether(String cardurltogether) {
		this.cardurltogether = cardurltogether;
	}

	public String getSenduserName() {
		return senduserName;
	}

	public void setSenduserName(String senduserName) {
		this.senduserName = senduserName;
	}

	public String getSenduserAddress() {
		return senduserAddress;
	}

	public void setSenduserAddress(String senduserAddress) {
		this.senduserAddress = senduserAddress;
	}

	public String getAddressAll() {
		return this.addressAll;
	}

	public String setAddressAllbyauto() {
		return this.getcProvince() + this.getcCity() + this.getcDistrict()
				+ this.getcStreetAddress();
	}

	public void setAddressAll(String addressAll) {
		this.addressAll = addressAll;
	}



	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getSenduserphone() {
		return senduserphone;
	}

	public void setSenduserphone(String senduserphone) {
		this.senduserphone = senduserphone;
	}

	

	public String getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(String weightKg) {
		this.weightKg = weightKg;
	}
	
	public String getCardid() {
		return cardId;
	}

	public void setCardid(String cardid) {
		this.cardId = cardid;
	}

	public String getCardurl() {
		return cardUrl;
	}

	public void setCardurl(String cardurl) {
		this.cardUrl = cardurl;
	}	
	
	public String toString() {
		return "order [orderId=" + this.orderId + ", remark=" + this.remark
				+ ", details=" + this.details + ", cProvince=" + this.cProvince
				+ "]";
	}
	
	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public String getUserName() {
    	return userName;
    }

	public void setUserName(String userName) {
    	this.userName = userName;
    }

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getStateStr() {
		return OrderUtil.transformerState(0, this.state);
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
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

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String[][] getOrderCommoditys() {
		return orderCommoditys;
	}

	public void setOrderCommoditys(String[][] orderCommoditys) {
		this.orderCommoditys = orderCommoditys;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public List<String> getTranshipmentIds() {
		return transhipmentIds;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setTranshipmentIds(List<String> transhipmentIds) {
		this.transhipmentIds = transhipmentIds;
	}

	public OrderDetail[] getDetails() {
		return details;
	}

	public void setDetails(OrderDetail[] details) {
		this.details = details;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderIdPrefix() {
		return orderIdPrefix;
	}

	public void setOrderIdPrefix(String orderIdPrefix) {
		this.orderIdPrefix = orderIdPrefix;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getcProvince() {
		return cProvince;
	}

	public void setcProvince(String cProvince) {
		this.cProvince = cProvince;
	}

	public String getcCity() {
		return cCity;
	}

	public void setcCity(String cCity) {
		this.cCity = cCity;
	}

	public String getcDistrict() {
		return cDistrict;
	}

	public void setcDistrict(String cDistrict) {
		this.cDistrict = cDistrict;
	}

	public String getcStreetAddress() {
		return cStreetAddress;
	}

	public void setcStreetAddress(String cStreetAddress) {
		this.cStreetAddress = cStreetAddress;
	}

	public String getcZipCode() {
		return cZipCode;
	}

	public void setcZipCode(String cZipCode) {
		this.cZipCode = cZipCode;
	}

	public String getcPhone() {
		return cPhone;
	}

	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public String getOr() {
		return or;
	}

	public void setOr(String or) {
		this.or = or;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getParceValue() {
		return parceValue;
	}

	public void setParceValue(String parceValue) {
		this.parceValue = parceValue;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public List<String> getOrderDetailTranshipmentIds() {
		if (this.details != null) {
			Set<String> set = new HashSet<String>();
			for (OrderDetail detail : this.details) {
				if (!StringUtil.isEmpty(detail.getTranshipNo())) {
					set.add(detail.getTranshipNo());
				}
			}
			return new ArrayList<String>(set);
		}
		return orderDetailTranshipmentIds;
	}

	public void setOrderDetailTranshipmentIds(List<String> orderDetailTranshipmentIds) {
		this.orderDetailTranshipmentIds = orderDetailTranshipmentIds;
	}

	public String getAddress() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.cProvince).append(" ").append(this.cCity).append(" ");
		sb.append(this.cDistrict).append(" ").append(this.cStreetAddress).append(" ");
		sb.append(this.cZipCode);
		return sb.toString();
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getPayChangeWarehouse() {
		return payChangeWarehouse;
	}

	public void setPayChangeWarehouse(String payChangeWarehouse) {
		this.payChangeWarehouse = payChangeWarehouse;
	}

	public String getPayToChina() {
		return payToChina;
	}

	public void setPayToChina(String payToChina) {
		this.payToChina = payToChina;
	}

	public StoragePosition getStoragePosition() {
		return storagePosition;
	}

	public void setStoragePosition(StoragePosition storagePosition) {
		this.storagePosition = storagePosition;
	}

}
