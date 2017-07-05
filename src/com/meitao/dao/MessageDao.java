package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Message;


public interface MessageDao {
	public int insertMessage(Message msg) throws Exception;

	public List<Message> retrieveMessages(@Param("userId") String userId, @Param("index") int index,
	        @Param("size") int size) throws Exception;

	public List<Message> retrieveMessagesByParentId(@Param("id") String id) throws Exception;

	public List<Message> searchMessageByKey(@Param("key") String key, @Param("state") String state,
	        @Param("userId") String userId, @Param("index") int index, @Param("size") int size, @Param("groupid") String groupid) throws Exception;

	public int count(@Param("userId") String userId) throws Exception;

	public int countByKey(@Param("key") String key, @Param("state") String state, @Param("userId") String userId)
	        throws Exception;

	public int updateModifyDate(@Param("id") String id, @Param("date") String date, @Param("state") String state)
	        throws Exception;
}
