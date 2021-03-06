package com.meitao.model;

import java.io.Serializable;

public class FriendlyLink implements Serializable {

	private static final long serialVersionUID = 92475344377649615L;
	private String id;
	private String no;
	private String name;
	private String hostUrl;
	private String imgUrl;
	private String type;
	private String createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostUrl() {
    	return hostUrl;
    }

	public void setHostUrl(String hostUrl) {
    	this.hostUrl = hostUrl;
    }

	public String getImgUrl() {
    	return imgUrl;
    }

	public void setImgUrl(String imgUrl) {
    	this.imgUrl = imgUrl;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
