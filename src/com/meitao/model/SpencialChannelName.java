package com.meitao.model;

import java.io.Serializable;

public class SpencialChannelName implements Serializable {

	private static final long serialVersionUID = 6137201026424971606L;
	private String id;
	private String name;//渠道名称
	
	private String state;      		 //状态

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
