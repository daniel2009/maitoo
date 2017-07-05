package com.meitao.service;



import com.meitao.exception.ServiceException;

import com.meitao.model.SpencialChannelOrder;;

public interface SpencialChannelOrderService {

	//public ResponseObject<Object> Checkmoney(SpencialChannelOrder Sorder) throws ServiceException;

	public abstract double calculationSOrderFreight(SpencialChannelOrder Sorder) throws ServiceException;

}
