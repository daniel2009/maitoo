package com.meitao.dao;

import java.util.List;

import com.meitao.model.FriendlyLink;


public interface FriendlyLinkDao {
	/**
	 * 插入友情链接
	 * 
	 * @param link
	 *            链接对象
	 * @return
	 * @throws Exception
	 */
	public int insertFriendlyLink(FriendlyLink link) throws Exception;

	/**
	 * 删除友情链接
	 * 
	 * @param ids
	 *            链接id
	 * @return
	 * @throws Exception
	 */
	public int deleteFriendlyLink(List<String> ids) throws Exception;

	/**
	 * 修改友情链接
	 * 
	 * @param link
	 *            修改链接对象
	 * @return
	 * @throws Exception
	 */
	public int updateFriendlyLink(FriendlyLink link) throws Exception;

	/**
	 * 根据id获取友情链接失败
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FriendlyLink retrieveLinkById(String id) throws Exception;

	/**
	 * 根据key跳过index条记录后，返回出size条记录
	 * 
	 * @param key
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<FriendlyLink> searchFriendlyLinkByKey(String key, int index, int size) throws Exception;

	/**
	 * 获取符合key的总的记录数
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int countByKey(String key) throws Exception;
}
