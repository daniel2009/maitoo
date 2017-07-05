package com.meitao.dao;

import java.util.List;


import com.meitao.model.AutoSend;

public interface AutoSendDao {

	List<AutoSend> findAll() throws Exception;

	int insert(AutoSend autoSend) throws Exception;

	int update(AutoSend autoSend) throws Exception;

	int delete(AutoSend autoSend) throws Exception;

	AutoSend findLastLikeName(AutoSend autoSend) throws Exception;
}
