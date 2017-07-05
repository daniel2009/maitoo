package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.globalargs;
import com.meitao.model.globalargsIndex;


/**
 * 操作新闻的底层接口<br/>
 * 直接和数据库打交道
 * 
 */
public interface globalargsDao {

	/**
	 * 查找所有的参数。
	 * 
	 * @param 
	 *            
	 * @return 
	 * @throws Exception
	 *             如果插入操作出现异常，抛出此异常。<br/>
	 *             如果使用MYbatis等其他插件，则抛出插件自定义的异常。
	 */
	public List<globalargs> searchallofglobalargs() throws Exception;

	/**
	 * 根据id查找参数列表
	 * 
	 * 
	 */
	public globalargs getglobalargsById(String id) throws Exception;

	/**
	 * 删入一条全局参数
	 * 
	 *
	 */
	public int insertglobalargs(String argflag,String argcontent,String argtype,String argremark) throws Exception;

	public int insertglobalargsbyobject(globalargs globalargs) throws Exception;

	/**
	 * argflag--查找argflag标志的记录个数
	 * 
	 */
	public int countByargflag(String argflag) throws Exception;

	/**
根据key和列号查找内容

	 */

	public List<globalargs> searchByKeys(@Param("key") String key,@Param("column") String column, @Param("index") int index, @Param("size") int size) throws Exception;

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
	public int countByKeys(@Param("key") String key,@Param("column") String column) throws Exception;
	
	public int updateglobalargsbyflag(globalargs argflag) throws Exception;
	
	public int updateglobalargsbyflag1(globalargs argflag) throws Exception;
	
	
	public int deleteglobalargsbyadmin(@Param("ids") List<String> ids) throws Exception;
	public int deleteglobalargsbyadminid(@Param("id") String id)  throws Exception;;
	
	/**
	 * 根据flag获取对应的内容
	 * 
	 * 
	 */
	public String getcontentbyflag(String flag) throws Exception;
	
	public List<globalargsIndex>  getallindexs()  throws Exception;;//获取全局变量的标志所有值
	public List<globalargs> getallbyindex(@Param("index") String index)  throws Exception;;//根据索引标志获取全局变量
	
	

}
