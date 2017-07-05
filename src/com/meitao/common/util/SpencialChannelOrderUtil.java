package com.meitao.common.util;


import com.meitao.model.SpencialCommodity;

public class SpencialChannelOrderUtil {


	public static boolean validateModifyCommodity(SpencialCommodity commodity) {
		boolean isEmpty = StringUtil.isEmpty(commodity.getId()) || StringUtil.isEmpty(commodity.getName())
			        || StringUtil.isEmpty(commodity.getPrice()) || StringUtil.isEmpty(commodity.getMsPrice());
		return !isEmpty;
	}

	
}
