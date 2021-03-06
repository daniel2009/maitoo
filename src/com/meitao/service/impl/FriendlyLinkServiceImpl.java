package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.FriendlyLinkDao;
import com.meitao.service.FriendlyLinkService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.FriendlyLink;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Service("friendlyLinkService")
public class FriendlyLinkServiceImpl extends BasicService implements FriendlyLinkService {
	@Autowired
	private FriendlyLinkDao friendlyLinkDao;

	public ResponseObject<Object> deleteLink(String[] ids) throws ServiceException {
		if (ids == null || ids.length == 0) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			int iresult = this.friendlyLinkDao.deleteFriendlyLink(Arrays.asList(ids));
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.LINK_DELETE_FAILURE, "根据id删除友情链接失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<FriendlyLink> getLinkById(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<FriendlyLink>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			ResponseObject<FriendlyLink> responseObj = new ResponseObject<FriendlyLink>();
			FriendlyLink link = this.friendlyLinkDao.retrieveLinkById(id);
			if (link != null) {
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(link);
			} else {
				responseObj.setCode(ResponseCode.LINK_NOT_EXISTS);
				responseObj.setMessage("没有对应id的友情链接");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<List<FriendlyLink>> getAllLink() throws ServiceException {
		try {
			List<FriendlyLink> data = new ArrayList<FriendlyLink>();
			List<FriendlyLink> result = this.friendlyLinkDao.searchFriendlyLinkByKey(null, 0, Integer.MAX_VALUE);
			if (result != null && !result.isEmpty()) {
				for (FriendlyLink t : result) {
					data.add(t);
				}
			}
			ResponseObject<List<FriendlyLink>> responseObj = new ResponseObject<List<FriendlyLink>>();
			responseObj.setCode(ResponseCode.SUCCESS_CODE);
			responseObj.setData(data);
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<PageSplit<FriendlyLink>> getLinkByKey(String key, int pageSize, int pageNow)
	        throws ServiceException {
		if (StringUtil.isEmpty(key)) {
			return new ResponseObject<PageSplit<FriendlyLink>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		} else {
			try {
				key = StringUtil.escapeStringOfSearchKey(key);
				int rowCount = 0;
				try {
					rowCount = this.friendlyLinkDao.countByKey(key);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据关键字获取失败友情链接数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<FriendlyLink> pageSplit = new PageSplit<FriendlyLink>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<FriendlyLink> result = this.friendlyLinkDao.searchFriendlyLinkByKey(key, startIndex,
						        pageSize);
						if (result != null && !result.isEmpty()) {
							for (FriendlyLink t : result) {
								pageSplit.addData(t);
							}
						}
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("根据关键字获取友情链接列表失败", e);
					}
					ResponseObject<PageSplit<FriendlyLink>> responseObj = new ResponseObject<PageSplit<FriendlyLink>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<FriendlyLink>>(ResponseCode.SUCCESS_CODE, "现在还没有友情链接");
				}
			} catch (ServiceException e) {
				throw e;
			}
		}
	}

	public ResponseObject<Object> modifyLink(FriendlyLink link) throws ServiceException {
		if (link == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
	        int iresult = this.friendlyLinkDao.updateFriendlyLink(link);
	        if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.LINK_MODIFY_FAILURE, "修改友情链接失败");
			}
        } catch (Exception e) {
        	throw ExceptionUtil.handle2ServiceException(e);
        }
	}

	public ResponseObject<Object> saveLink(FriendlyLink link) throws ServiceException {
		if (link == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			link.setCreateDate(DateUtil.date2String(new Date()));
			int iresult = this.friendlyLinkDao.insertFriendlyLink(link);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.LINK_INSERT_FAILURE, "插入友情链接失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

}
