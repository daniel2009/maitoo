package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.SpiderOrder;

public interface SpiderOrderService {

	ResponseObject<SpiderOrder> search(String url) throws ServiceException;
}
