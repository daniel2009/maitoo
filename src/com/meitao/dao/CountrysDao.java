package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Countrys;

public interface CountrysDao {
	public Countrys getCountryById(String id) throws Exception;
	public int insertCountrys(Countrys country) throws Exception;

	public List<Countrys> getAllCountrys() throws Exception;

	public List<Countrys> selectCountrysbyIds(@Param("ids")List<String> ids) throws Exception;
	
}
