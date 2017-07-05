package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Account;
import com.meitao.model.Credit;


public interface AccountDao {
	public int insertCredit(Credit credit) throws Exception;

	public int updateCredit(Credit credit) throws Exception;

	public Credit getCreditById(String id) throws Exception;

	public List<Credit> getCreditsByUserId(@Param("userId") String userId) throws Exception;

	public int deleteCreditByUserIds(List<String> list) throws Exception;

	public int insertOrUpdateAccount(Account account) throws Exception;

	public int modifyAccount(Account account) throws Exception;

	public int deleteAccountByUserIds(List<String> list) throws Exception;

	public Account getAccountByUserId(@Param("userId") String userId) throws Exception;

	public int rechargeRmb(@Param("userId")String userId, @Param("amount")String amount, @Param("modifyDate")String modifyDate) throws Exception;

	public int checkIfScanPay(@Param("userId")String userId, @Param("modifyDate")String modifyDate) throws Exception;

}
