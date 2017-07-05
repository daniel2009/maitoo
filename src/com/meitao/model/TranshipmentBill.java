package com.meitao.model;

import java.io.Serializable;
import java.util.List;

/**
 * 转运单对象
 */
public class TranshipmentBill implements Serializable {

	private static final long serialVersionUID = 3637616775548010801L;
	private String id; // 主键id
	private String transitNo;// 转运单号
	private String transitCompany; // 转运公司
	private String warehouseId;// 仓库id
	private String userId; // 所属用户
	private String empId; // 操作该记录的职工id
	private String fromUser; // 寄件人
	private String state; // 状态
	private String createDate;// 创建时间
	private String eta; // 预计到达时间
	private String awt; // 到库时间
	private String remark; // 备注
	private String weight; // 重量
	private String parcelValue; // 包裹价值
	private String tariff; // 关税
	private String or; // OR转运费
	private String other; // 其他费用
	private String modifyDate;
	
	private String weightKg;//kai
	private String premium;//保险
	private String mail;//邮寄方式
	
	//添加用户唯一标识
	private String usercode;
	private String useralias;
	
	
	private String length; // 长  add by chenkanghua 
	private String width; //  宽  add by chenkanghua 
	private String height; // 高  add by chenkanghua 
	private String channelId;//渠道id
	private String channelsName;//渠道名称
	
	
	private String warehouse_location_id;
	private String fromWarehousePrice;//离开这个州的时候，加上这个周的转运费用changeWarehousePrice
	
	// 数据库中没有对应的字段的property
	private String groupId;
	private String userName;
	private int commodityNumber; // 商品数
	private String warehouseName; // 仓库名称
	private List<TranshipmentCommodity> commoditys; // 运单商品列表
	private int unpackingNumber; // 拆包数量 
	
	
	
	private String transitType;//转运类型 kai 20150924
	
	//收件地址和图片
	private String cName = ""; // 收件人姓名
	private String cProvince = ""; // 收件人省份
	private String cCity = ""; // 收件人城市
	private String cDistrict = ""; // 收件人区市
	private String cStreetAddress = ""; // 收件人具体地址
	private String cZipCode = ""; // 收件人邮编
	private String cPhone = ""; // 电话号码
	private String cardid;//身份证号
	private String cardurl;//身份证图片正面
	private String cardurlother;//身份证图片反面
	private String cardurltogether;//身份证图片合成后图片
	
	
	private String rmbBalance;//人民币余额
	private String usdBalance;//美元余额
	private String routeState;
	private String tranWarehouseId;//转运的仓库，退转运州的仓库
	private String tranWarehouseName;//转运的仓库名称
	private String authorizeMoveMoney;
	private String totalMoney;
	private String pretranwarehouseId;//用于转运入库时，记录预计到达的仓库
	
	private String orderId;//保存生成运单后的运单号
	
	private StoragePosition storagePosition; // 查询的时候使用，保存仓位
	
	private String divideMoney;
	private String selfgetpackageFlag;
	private String selfPaytype;
	
	private String wrongRemark;//包裹异常时的描述
	private String userRealName;//用户姓名
	private String CommodityCost;//kai 保存转运产生的成本
	private  User user;//用于返回包含的用户信息

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCommodityCost() {
		return CommodityCost;
	}

	public void setCommodityCost(String commodityCost) {
		CommodityCost = commodityCost;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getWrongRemark() {
	return wrongRemark;
}

public void setWrongRemark(String wrongRemark) {
	this.wrongRemark = wrongRemark;
}

	public String getSelfgetpackageFlag() {
		return selfgetpackageFlag;
	}

	public void setSelfgetpackageFlag(String selfgetpackageFlag) {
		this.selfgetpackageFlag = selfgetpackageFlag;
	}

	public String getSelfPaytype() {
		return selfPaytype;
	}

	public void setSelfPaytype(String selfPaytype) {
		this.selfPaytype = selfPaytype;
	}

	public String getDivideMoney() {
		return divideMoney;
	}

	public void setDivideMoney(String divideMoney) {
		this.divideMoney = divideMoney;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPretranwarehouseId() {
		return pretranwarehouseId;
	}

	public void setPretranwarehouseId(String pretranwarehouseId) {
		this.pretranwarehouseId = pretranwarehouseId;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getAuthorizeMoveMoney() {
		return authorizeMoveMoney;
	}

	public void setAuthorizeMoveMoney(String authorizeMoveMoney) {
		this.authorizeMoveMoney = authorizeMoveMoney;
	}

	public String getTranWarehouseName() {
		return tranWarehouseName;
	}

	public void setTranWarehouseName(String tranWarehouseName) {
		this.tranWarehouseName = tranWarehouseName;
	}

	public String getTranWarehouseId() {
		return tranWarehouseId;
	}

	public void setTranWarehouseId(String tranWarehouseId) {
		this.tranWarehouseId = tranWarehouseId;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUseralias() {
		return useralias;
	}

	public void setUseralias(String useralias) {
		this.useralias = useralias;
	}

	public String getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(String weightKg) {
		this.weightKg = weightKg;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}


	
	public String getChannelsName() {
		return channelsName;
	}

	public void setChannelsName(String channelsName) {
		this.channelsName = channelsName;
	}


	public String getRmbBalance() {
		return rmbBalance;
	}

	public void setRmbBalance(String rmbBalance) {
		this.rmbBalance = rmbBalance;
	}

	public String getUsdBalance() {
		return usdBalance;
	}

	public void setUsdBalance(String usdBalance) {
		this.usdBalance = usdBalance;
	}

	public String getTransitType() {
		return transitType;
	}

	public void setTransitType(String transitType) {
		this.transitType = transitType;
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

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getCardurl() {
		return cardurl;
	}

	public void setCardurl(String cardurl) {
		this.cardurl = cardurl;
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

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransitNo() {
		return transitNo;
	}

	public String getParcelValue() {
		return parcelValue;
	}

	public void setParcelValue(String parcelValue) {
		this.parcelValue = parcelValue;
	}

	public void setTransitNo(String transitNo) {
		this.transitNo = transitNo;
	}

	public String getTransitCompany() {
		return transitCompany;
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

	public int getUnpackingNumber() {
    	return unpackingNumber;
    }

	public void setUnpackingNumber(int unpackingNumber) {
    	this.unpackingNumber = unpackingNumber;
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

	public void setTransitCompany(String transitCompany) {
		this.transitCompany = transitCompany;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAwt() {
		return awt;
	}

	public void setAwt(String awt) {
		this.awt = awt;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public int getCommodityNumber() {
		return commodityNumber;
	}

	public void setCommodityNumber(int commodityNumber) {
		this.commodityNumber = commodityNumber;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public List<TranshipmentCommodity> getCommoditys() {
		return commoditys;
	}

	public void setCommoditys(List<TranshipmentCommodity> commoditys) {
		this.commoditys = commoditys;
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

	public String getWarehouse_location_id() {
		return warehouse_location_id;
	}

	public void setWarehouse_location_id(String warehouse_location_id) {
		this.warehouse_location_id = warehouse_location_id;
	}

	public String getFromWarehousePrice() {
		return fromWarehousePrice;
	}

	public void setFromWarehousePrice(String fromWarehousePrice) {
		this.fromWarehousePrice = fromWarehousePrice;
	}

	public String getRouteState() {
		return routeState;
	}

	public void setRouteState(String routeState) {
		this.routeState = routeState;
	}

	public StoragePosition getStoragePosition() {
		return storagePosition;
	}

	public void setStoragePosition(StoragePosition storagePosition) {
		this.storagePosition = storagePosition;
	}
	
	
}
