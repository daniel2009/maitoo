package com.meitao.service.impl;


import java.util.List;











import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.meitao.dao.TranPriceDao;

import com.meitao.service.TranPriceService;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;
import com.meitao.model.TranPrice;

@Service("tranPriceService")
public class TranPriceServiceImpl extends BasicService implements TranPriceService {
	@Autowired
	private TranPriceDao tranPriceDao;

	public ResponseObject<Object> getalltranprice() throws ServiceException {
	

		try {
			List<TranPrice> list = this.tranPriceDao.getAll();
			if (list !=null) {
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(list);
				return obj;
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE, "获取转运价格失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
}
