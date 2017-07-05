package com.meitao.dao;

import java.util.List;

import com.meitao.model.Authority_url;


public interface AuthorityDao {
	
	public List<Authority_url> selectAuthority_ul(String employees_id) throws Exception;
	public List<String> findeAuthority_urlName(String employees_id)throws Exception;
	public List<Authority_url> findAll() throws Exception;
	
}
