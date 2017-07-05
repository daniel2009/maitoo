package com.meitao.dao;

import java.util.List;



import com.meitao.model.SpencialChannelName;;


public interface SpencialChannelNameDao {

	public List<SpencialChannelName> getallgood() throws Exception;

	public SpencialChannelName getbyid(String id) throws Exception;
	
}
