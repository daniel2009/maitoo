package com.meitao.common.util;

import com.meitao.exception.ServiceException;

public class ExceptionUtil {
	public static ServiceException handle2ServiceException(String message) {
		return new ServiceException(message);
	}

	public static ServiceException handle2ServiceException(Throwable e) {
		return new ServiceException(e);
	}

	public static ServiceException handle2ServiceException(String message,
			Throwable e) {
		return new ServiceException(message, e);
	}
}
