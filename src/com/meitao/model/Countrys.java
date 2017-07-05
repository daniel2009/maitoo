package com.meitao.model;

import java.io.Serializable;

public class Countrys implements Serializable {

	private static final long serialVersionUID = 6137201026424971606L;
	private String id;
	private String country;//国家的英文名称 China
	
	private String countrycode;      		 //国家代号
	private String isocode;     	 //国家的简称
	private String chinaname;      //中国的确中文名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	public String getIsocode() {
		return isocode;
	}
	public void setIsocode(String isocode) {
		this.isocode = isocode;
	}
	public String getChinaname() {
		return chinaname;
	}
	public void setChinaname(String chinaname) {
		this.chinaname = chinaname;
	}
	
	
	
	
}
