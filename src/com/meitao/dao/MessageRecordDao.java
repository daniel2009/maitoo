package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;



//import com.meitao.model.Message;
import com.meitao.model.MessageControl;
import com.meitao.model.MessageRecord;
import com.meitao.model.SmOrder;


public interface MessageRecordDao {
	public int insert(MessageRecord msg) throws Exception;


	public List<MessageRecord> searchKeysByadmin(@Param("csdate") String csdate,@Param("cedate") String cedate,@Param("type") String type,@Param("wordkey") String wordkey, @Param("index") int index, @Param("size") int size) throws Exception;


	public int countKeysbyadmin(@Param("csdate") String csdate,@Param("cedate") String cedate,@Param("type") String type,@Param("wordkey") String wordkey) throws Exception;
	
}

