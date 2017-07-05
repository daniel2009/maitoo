package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.News;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


/**
 * 提供新闻服务的接口
 */
public interface NewsService {

	/**
	 * 添加一个新闻纪录
	 * 
	 * @param news
	 *            要添加的新闻纪录
	 * @return 如果添加成功，则返回true;否则返回false.
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> saveNews(News news) throws ServiceException;

	/**
	 * 修改一条新闻纪录的信息
	 * 
	 * @param news
	 *            要修改的新闻记录信息对象
	 * @return 如果修改成功，则返回true；否则返回false。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> modifyNews(News news) throws ServiceException;

	/**
	 * 根据新闻id删除一条新闻纪录
	 * 
	 * @param id
	 *            要删除的新闻纪录id
	 * @return 如果删除成功，则返回true；否则返回false。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> deleteNews(String id) throws ServiceException;

	/**
	 * 根据新闻id获取新闻对象
	 * 
	 * @param id
	 *            要获取的新闻对象的id值
	 * @return 如果数据库中有对应的数据，则返回该数据。否则返回null。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<News> getNewsById(String id) throws ServiceException;

	/**
	 * 根据分页/查询对象pageSplit对象的值，查询数据纪录
	 * 
	 * @param key
	 *            关键字
	 * @param pageSize
	 *            每页显示数目
	 * @param pageNow
	 *            现在所在页数
	 * 
	 * @return 如果数据库中有满足条件的记录，则返回这些记录组成的list集合，否则返回null。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public <T> ResponseObject<PageSplit<T>> getNewsByKey(String key,String column, int pageSize, int pageNow) throws ServiceException;
	
	/**
	 * 根据新闻id删除多条新闻纪录
	 * 
	 * @param ids
	 *            要删除的新闻纪录id集
	 * @return 如果删除成功，则返回true；否则返回false。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> deleteNewsbyadmin(String[] ids) throws ServiceException;
}
