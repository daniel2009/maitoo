package com.meitao.dao;




import com.meitao.model.Endicial_arg;


public interface EndicialArgDao {

	int insert(Endicial_arg arg) throws Exception;
	Endicial_arg getone() throws Exception;
	
	int modifybyid(Endicial_arg arg) throws Exception;
	
}
