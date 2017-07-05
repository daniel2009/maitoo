package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.News;



/**
 * 操作新闻的底层接口<br/>
 * 直接和数据库打交道
 * 
 */
public interface NewsDao {

	/**
	 * 插入一个新闻纪录到数据库中去。
	 * 
	 * @param news
	 *            要添加的新闻纪录
	 * @return 如果插入成功，则返回受影响的行数。否则返回0.
	 * @throws Exception
	 *             如果插入操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public int insertNews(News news) throws Exception;

	/**
	 * 修改一个新闻纪录的字段信息，可修改的字段包括：title,content,author,tag,img五个字段
	 * 
	 * @param news
	 *            要修改的新闻对象
	 * @return 如果修改成功，则返回受影响的行数。否则返回0.
	 * @throws DaoException
	 *             如果修改操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public int updateNews(News news) throws Exception;

	/**
	 * 根据id删除一条新闻纪录
	 * 
	 * @param id
	 *            要删除新闻纪录的id主键
	 * @return 如果删除成，则返回受影响的行数。否则返回0.
	 * @throws DaoException
	 *             如果删除操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public int deleteNews(String id) throws Exception;

	/**
	 * 根据id获取一条新闻纪录
	 * 
	 * @param id
	 *            新闻纪录id
	 * @return 如果数据库中有匹配的记录，则返回该记录对应的新闻对象。否则返回null。
	 * @throws DaoException
	 *             如果获取/查询操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public News retrieveNewsById(String id) throws Exception;

	/**
	 * 根据分页条件，获得一个新闻列表
	 * 
	 * @param key
	 *            关键字
	 * @param    column    要搜索的列
	 * @param index
	 *            跳过多少条记录
	 * @param size
	 *            每页记录数
	 * 
	 * @return 返回匹配的新闻记录列表，如果不存在，则返回null。
	 * @throws DaoException
	 *             如果获取/查询操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */

	public List<News> searchByKeys(@Param("key") String key,@Param("column") String column, @Param("index") int index, @Param("size") int size) throws Exception;

	/**
	 * 根据分页/查询条件获取满足条件的记录数<br/>
	 * 如果参数pageSplit为null,则返回所有记录数。
	 * 
	 * @param key
	 *            关键字
	 * 
	 * @return 满足条件的权限记录数，如果不存在，则返回0.
	 * @throws DaoException
	 *             如果获取/查询操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public int countByKey(@Param("key") String key,@Param("column") String column) throws Exception;
	
	/**
	 * 根据集删除多条新闻纪录
	 * 
	 * @param ids
	 *            要删除新闻纪录的ids主键
	 * @return 如果删除成，则返回受影响的行数。否则返回0.
	 * @throws DaoException
	 *             如果删除操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public int deleteNewsbyadmin(@Param("ids") List<String> ids) throws Exception;
	

}
