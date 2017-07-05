package com.meitao.common.constants;

import java.math.BigDecimal;

public class OrderFreightConstant {
	public static final double NORMAL_USER_ORDER_FIRST_WEIGHT = 1; // 普通会员运单首重的重量
	public static final double VIP_USER_ORDER_FIRST_WEIGHT = 1; // vip会员运单首重的重量
	
	public static final double NORMAL_USER_ORDER_FIRST_MONEY = 65; // 普通会员首重money
	public static final double VIP_USER_ORDER_FIRST_MONEY = 60; // vip会员首重money
	
	public static final double NORMAL_USER_RENEWAL_PRICE = 30; // 普通会员续重单价/0.5kg
	public static final double VIP_USER_RENEWAL_PRICE = 25; // vip会员续重单价/0.5kg
	
	public static final double pounds1 = 33; // 第一阶段33磅
	public static final BigDecimal priceOfPounds1 = BigDecimal.valueOf(9); // 每一磅9美元
	public static final BigDecimal priceOfPounds2 = BigDecimal.valueOf(6); // 每一磅6美元
	public static final double usd2rmb = 6.1; // 美元转人民币
	public static final double rmb2usd = 0.16; // 人民币转美元
}
