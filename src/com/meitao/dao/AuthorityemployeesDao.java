package com.meitao.dao;
import java.util.List;

import com.meitao.model.Authority;



public interface AuthorityemployeesDao {
	
	
	
	//插入员工权限信息
	public int insertAuthority(List<Authority> authoritys) throws Exception;
	//根据员工ID删除对应的权限
	public int dellAuthorityEmployeeById( String id)throws Exception;
	//修改员工权限
	public int modifyAuthority( List<Authority> authority,String id) throws Exception;
	
}
