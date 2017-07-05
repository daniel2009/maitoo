package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

//import com.meitao.model.Message;
import com.meitao.model.MessageControl;


public interface MessageControlDao {
	public int insert(MessageControl msg) throws Exception;

	public int updatebyid(MessageControl msg) throws Exception;
	public int updatebyflag(MessageControl msg) throws Exception;

	public MessageControl getonebyflag(@Param("flag") String flag) throws Exception;
	public MessageControl getonebyid(@Param("id") String id) throws Exception;
	public int updateContent(MessageControl msg) throws Exception;
	
}

