package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Cart;
import com.meitao.model.User;

public interface CartDao {

	int insert(Cart cart) throws Exception;
	Cart findById(Cart cart) throws Exception;
	
	int countByUser(@Param("user")User user) throws Exception;
	/**
	 * 不管productId在cart表中存不存在，都查询商品
	 * @param ids
	 * @param firstResult
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Cart> setByProductIdsIncludeSame(@Param("ids")String[] ids, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	
	List<Cart> findByUser(@Param("user")User user, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;
	int delete(Cart cart) throws Exception;
	int deleteByUser(User user) throws Exception;
}
