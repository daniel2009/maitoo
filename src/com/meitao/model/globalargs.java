/*
 * 用途：全局参数定义类
 * 建议人：kai
 * 创建时间：20151009
 * 修改时间1:
 * 修改原因1：
 * */

package com.meitao.model;

import java.io.Serializable;





public class globalargs implements Serializable {
	private static final long serialVersionUID = -6319305699702440060L;
	private String id;//全局标志的号
	private String argflag;//全局标志的唯一标识,用户定义
	private String argcontent;//参数的内容
	private String argtype;//参数的类型,标识此参数是干什么的
	private String argremark;//参数备注，用于用户说明此参数的用途
	private String modifytime;//修改时间
	private String createtime;//创建时间
	private String index;
	private String name;//名称
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getModifytime() {
		return modifytime;
	}
	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArgflag() {
		return argflag;
	}
	public void setArgflag(String argflag) {
		this.argflag = argflag;
	}
	public String getArgcontent() {
		return argcontent;
	}
	public void setArgcontent(String argcontent) {
		this.argcontent = argcontent;
	}
	public String getArgtype() {
		return argtype;
	}
	public void setArgtype(String argtype) {
		this.argtype = argtype;
	}
	public String getArgremark() {
		return argremark;
	}
	public void setArgremark(String argremark) {
		this.argremark = argremark;
	}


}
