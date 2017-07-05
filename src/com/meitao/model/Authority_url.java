/**
 * 权限url 实体类
 * 张敬琦
 * 2015-01-27
 * 
 */

package com.meitao.model;

import java.io.Serializable;


public class Authority_url implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8031699207761782766L;
	private String authority_id;
	

	public String name;
	public String menu_id;
	
	
	public Authority_url() {
    }
	
	public Authority_url( String authority_id,String name,String meau_id) {
		this.authority_id = authority_id;
		this.name = name;
		this.menu_id=meau_id;
	}
	
	
	public String getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(String authority_id) {
		this.authority_id = authority_id;
	}
	public String getName() {
		return name;
	}
//	public int getAuthoriy_id() {
//		return authoriy_id;
//	}
//
//
//
//	public void setAuthoriy_id(int authoriyId) {
//		authoriy_id = authoriyId;
//	}



	public void setName(String name) {
		this.name = name;
	}
	public String getAuthoriy_id() {
		return authority_id;
	}



	public void setAuthoriy_id(String authoriyId) {
		authority_id = authoriyId;
	}



	public String getMeau_id() {
		return menu_id;
	}
	public void setMeau_id(String meauId) {
		menu_id = meauId;
	}
//	public void setAuthority_url(String authoriy_id, String meau_id,String name) {
//		this.authoriy_id=authoriy_id;
//		this.meau_id=meau_id;
//		this.name=name;
//	}
	
	 @Override
	    public String toString() {
	        return "Authority_url [authoriy_id=" + authority_id + ", name=" + name + ", meau_id=" + menu_id + "]";
	    }

}
