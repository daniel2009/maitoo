package com.meitao.dao;

import java.util.List;



import com.meitao.model.SpencialChannelOrder;;;


public interface SpencialChannelOrderDao {

	public List<SpencialChannelOrder> getAll() throws Exception;

	public SpencialChannelOrder getSorderById(String id) throws Exception;
	public List<SpencialChannelOrder> getBySorderbyuserId(String userId,String state) throws Exception;
	public int insertSorder(SpencialChannelOrder order)  throws Exception;;
	public int modifyState(String id)  throws Exception;;
}
