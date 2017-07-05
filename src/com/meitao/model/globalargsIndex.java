/*
 * 用途：全局参数定义类
 * 建议人：kai
 * 创建时间：20151009
 * 修改时间1:
 * 修改原因1：
 * */

package com.meitao.model;

import java.io.Serializable;





public class globalargsIndex implements Serializable {
	private static final long serialVersionUID = -6319305612352440060L;
	private String id;//全局变量分类标志
	private String name;//分类名称
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
