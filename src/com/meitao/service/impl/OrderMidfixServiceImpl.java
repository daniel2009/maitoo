package com.meitao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.OrderMidfixDao;
import com.meitao.service.OrderMidfixService;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.OrderMidfix;
import com.meitao.model.ResponseObject;

@Service("orderMidfixService")
public class OrderMidfixServiceImpl implements OrderMidfixService {

	@Autowired
	private OrderMidfixDao orderMidfixDao;

	public OrderMidfix findOrderMidfixByState0() throws ServiceException {

		OrderMidfix orderMidfix = new OrderMidfix();
		try {
			orderMidfix = this.orderMidfixDao.findOrderMidfixByState("0");
			orderMidfix.setState("1");
			this.orderMidfixDao.updateOrderMidfix(orderMidfix);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException("提交运单异常，请重试", e);
		}
		return orderMidfix;
	}

	public ResponseObject<Object> modifyOrderMidfix(String orderMidfix,
			String state) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if ((orderMidfix == null) && (state == null)) {
			responseObj.setCode("603");
			responseObj.setMessage("参数无效");
		} else {

			try {
				OrderMidfix om = new OrderMidfix();
				om.setState(state);
				om.setOrderMidfix(orderMidfix);
				this.orderMidfixDao.updateOrderMidfix(om);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("提交运单异常，请重试", e);
			}
		}

		return responseObj;
	}
}