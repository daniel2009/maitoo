package com.meitao.common.util;

import com.meitao.model.Commodity;

public class CommodityUtil {


	public static boolean validateModifyCommodity(Commodity commodity) {
		boolean isEmpty = StringUtil.isEmpty(commodity.getId()) || StringUtil.isEmpty(commodity.getName())
			        || StringUtil.isEmpty(commodity.getPrice()) || StringUtil.isEmpty(commodity.getMsPrice());
		return !isEmpty;
	}

	
}
