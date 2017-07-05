package com.meitao.model.temp;

import com.meitao.model.M_order;
import com.meitao.model.Warehouse;


public class ExportMorder {
	private static final long serialVersionUID = -6202420245816666L;
	private String orderId;//导入的单号
	
	private String exportmodel;//导出模版类型
	private String cardmodel;//匹配身份证方式
    private M_order order;
	private String orderResult;//获取运单信息
	private String cardResult;//匹配身份证结果
	private String companyName;//公司名称
	private String companyAddress;//公司地址
	private String downloadpic;//是否下载身份证图片，1表示下载，其它表示不下载
	private String getSeaNo;//获取海关单号，1表示获取，其它表示要获取
	private String getSeaNoResult;
	private String savezipurl;//保存身份证存储路径
	
	private Warehouse store;
	
	public Warehouse getStore() {
		return store;
	}
	public void setStore(Warehouse store) {
		this.store = store;
	}
	private double rate=0;//汇率
	
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getSavezipurl() {
		return savezipurl;
	}
	public void setSavezipurl(String savezipurl) {
		this.savezipurl = savezipurl;
	}
	public String getGetSeaNoResult() {
		return getSeaNoResult;
	}
	public void setGetSeaNoResult(String getSeaNoResult) {
		this.getSeaNoResult = getSeaNoResult;
	}
	public String getDownloadpic() {
		return downloadpic;
	}
	public void setDownloadpic(String downloadpic) {
		this.downloadpic = downloadpic;
	}
	public String getGetSeaNo() {
		return getSeaNo;
	}
	public void setGetSeaNo(String getSeaNo) {
		this.getSeaNo = getSeaNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getExportmodel() {
		return exportmodel;
	}
	public void setExportmodel(String exportmodel) {
		this.exportmodel = exportmodel;
	}
	public String getCardmodel() {
		return cardmodel;
	}
	public void setCardmodel(String cardmodel) {
		this.cardmodel = cardmodel;
	}
	public M_order getOrder() {
		return order;
	}
	public void setOrder(M_order order) {
		this.order = order;
	}
	public String getOrderResult() {
		return orderResult;
	}
	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}
	public String getCardResult() {
		return cardResult;
	}
	public void setCardResult(String cardResult) {
		this.cardResult = cardResult;
	}
    
	



}
