package com.meitao.model;

public class ProductType implements java.io.Serializable{
	private static final long serialVersionUID = 8092398917010623447L;
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
