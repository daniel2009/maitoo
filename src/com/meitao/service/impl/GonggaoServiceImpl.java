/**
 * 公告Service实现方法
 * 张敬琦
 * 2015-01-30
 */


package com.meitao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.GonggaoDao;
import com.meitao.service.GonggaoService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.NewsUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Gonggao;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Service("gonggaoService")
public class GonggaoServiceImpl extends BasicService implements GonggaoService {
	@Autowired
	private GonggaoDao gonggaoDao;

	public ResponseObject<Object> saveGonggao(Gonggao gonggao) throws ServiceException {
		if (gonggao == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		// deal the special words
		//news.setViewCount(0);
		String date = DateUtil.date2String(new Date());// 修改时间
		gonggao.setReleasetime(date);	
		gonggao.setModifytime(date);

		try {
			int iresult = this.gonggaoDao.insertgonggao(gonggao);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE, "插入新闻失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> deleteGonggao(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			int iresult = this.gonggaoDao.deleteGonggao(id);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_DELETE_FAILURE, "删除新闻失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Gonggao> getGonggaoById(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Gonggao>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			ResponseObject<Gonggao> responseObj = new ResponseObject<Gonggao>();
			Gonggao tmp = this.gonggaoDao.retrieveGonggaoById(id);
			if (tmp != null) {
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(tmp);
			} else {
				responseObj.setCode(ResponseCode.NEWS_NOT_EXISTS);
				responseObj.setMessage("公告不存在");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> getGonggaoByKey(String key, int pageSize, int pageNow)
	        throws ServiceException {
		if (StringUtil.isEmpty(key)) {
			return new ResponseObject<PageSplit<T>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		} else {
			try {
				key = StringUtil.escapeStringOfSearchKey(key);
				int rowCount = 0;
				try {
					//rowCount = this.gonggaoDao.countByKey(key);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据关键字获取失败新闻数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<T> pageSplit = new PageSplit<T>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<Gonggao> result = this.gonggaoDao.searchGonggaoByKey(key, startIndex, pageSize);
						if (result != null && !result.isEmpty()) {
							for (Gonggao t : result) {
								pageSplit.addData((T) t);
							}
						}
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("根据关键字获取新闻纪录列表失败", e);
					}
					ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE, "现在还没有新闻");
				}
			} catch (ServiceException e) {
				throw e;
			}
		}
	}

	public ResponseObject<Object> modifyGonggao(Gonggao gonggao) throws ServiceException {
		if (gonggao == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			int iresult = this.gonggaoDao.updateGonggao(gonggao);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_MODIFY_FAILURE, "修改新闻记录失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> getgonggaoByKey(String key,String column,  int pageSize, int pageNow)
	        throws ServiceException {
		/*if (StringUtil.isEmpty(key)) {
			return new ResponseObject<PageSplit<T>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		} else {*/
			try {
				
				
				key = StringUtil.escapeStringOfSearchKey(key);
				
				column=NewsUtil.validatecolumn(column);
				int rowCount = 0;
				try {
					rowCount = this.gonggaoDao.countByKey(key,column);
					
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据关键字获取失败新闻数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<T> pageSplit = new PageSplit<T>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<Gonggao> result = this.gonggaoDao.searchByKeys(key,column, startIndex, pageSize);
						if (result != null && !result.isEmpty()) {
							for (Gonggao t : result) {
								pageSplit.addData((T) t);
							}
						}
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("根据关键字获取新闻纪录列表失败", e);
					}
					ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE, "现在还没有新闻");
				}
			} catch (ServiceException e) {
				throw e;
			}
		//}
	}
	
	public ResponseObject<Object> deletegonggaobyadmin(String[] ids) throws ServiceException {
		for(String id : ids)
		{
			if (StringUtil.isEmpty(id)) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
			}
		}

		try {
			
			int iresult = this.gonggaoDao.deletegonggaobyadmin(Arrays.asList(ids));
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_DELETE_FAILURE, "删除新闻失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
}
