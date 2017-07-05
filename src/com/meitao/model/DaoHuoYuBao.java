package com.meitao.model;

import java.io.Serializable;

public class DaoHuoYuBao implements Serializable {

	private static final long serialVersionUID = -3400398638086962093L;
	private String id;
	private String transitNo;
	private String transitCompany;
	private String warehouseId;
	private TranshipmentCommodity[] commodity;
	private String parcelValue;
	private String fromUser;
	private String remark;
	private String eta;

	private String state;
	private String weight;
	private String or;
	private String other;
	private String tariff;
	private String groupId;
	
	
	private String length; // 长  add by chenkanghua 
	private String width; //  宽  add by chenkanghua 
	private String height; // 高  add by chenkanghua 
	private String channelId;//渠道id
	private String warehouse_location_id;
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

	private String weightKg;//kai
	private String premium;//保险
	private String mail;//邮寄方式
	
    private String transtateflag;//转运州标志
    
	private String tranWarehouseId;//转运的仓库，退转运州的仓库
	private String authorizeMoveMoney;//用户组合转运包裹时，是否授权直接扣款，0表示不授权，1表示授权
	
	private String totalMoney;
	
	private String pretranwarehouseId;//用于转运入库时，记录预计到达的仓库
	private String divideMoney;//分合箱手续费
	
	private String selfgetpackageFlag;//自提标志，1表示自提
	private String selfPaytype;//自提支付方式 
	private String wrongRemark;//包裹异常时的描述!
	
	private String CommodityCost;//kai 保存转运产生的成本
	private StoragePosition storagePosition; // 查询的时候使用，保存仓位


	

	public StoragePosition getStoragePosition() {
		return storagePosition;
	}

	public void setStoragePosition(StoragePosition storagePosition) {
		this.storagePosition = storagePosition;
	}

	public String getCommodityCost() {
		return CommodityCost;
	}

	public void setCommodityCost(String commodityCost) {
		CommodityCost = commodityCost;
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

	public String getTranWarehouseId() {
		return tranWarehouseId;
	}

	public void setTranWarehouseId(String tranWarehouseId) {
		this.tranWarehouseId = tranWarehouseId;
	}

	public String getTranstateflag() {
		return transtateflag;
	}

	public void setTranstateflag(String transtateflag) {
		this.transtateflag = transtateflag;
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

	public String getTransitType() {
		return transitType;
	}

	public void setTransitType(String transitType) {
		this.transitType = transitType;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getState() {
    	return state;
    }

	public void setState(String state) {
    	this.state = state;
    }

	public String getWeight() {
    	return weight;
    }

	public void setWeight(String weight) {
    	this.weight = weight;
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

	public String getTariff() {
    	return tariff;
    }

	public void setTariff(String tariff) {
    	this.tariff = tariff;
    }

	public String getTransitNo() {
		return transitNo;
	}

	public void setTransitNo(String transitNo) {
		this.transitNo = transitNo;
	}

	public String getTransitCompany() {
		return transitCompany;
	}

	public void setTransitCompany(String transitCompany) {
		this.transitCompany = transitCompany;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public TranshipmentCommodity[] getCommodity() {
		return commodity;
	}

	public void setCommodity(TranshipmentCommodity[] commodity) {
		this.commodity = commodity;
	}

	public String getParcelValue() {
		return parcelValue;
	}

	public void setParcelValue(String parcelValue) {
		this.parcelValue = parcelValue;
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

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
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
	
}
