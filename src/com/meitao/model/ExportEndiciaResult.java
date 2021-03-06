/**
 *新闻实体类
 *张敬琦
 *2015-01-30 
 */

package com.meitao.model;

import java.io.Serializable;

import java.util.List;

public class ExportEndiciaResult implements Serializable {

	private static final long serialVersionUID = 1567012088660323595L;
	private String totalmoney;
	private String errorMessage;
	private List<String> images;
	private String trackingNumber;
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}

	
	

}
