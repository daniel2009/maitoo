package com.meitao.common.util.calc.impl;

import java.math.BigDecimal;

import com.meitao.common.constants.OrderFreightConstant;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.calc.OrderFreightCalculation;
import com.meitao.model.Order;


/**
 * VIP会员运费计算
 * 
 * @author Administrator
 * 
 */
public class VIPUserFreightCalculation implements OrderFreightCalculation {

	public double calcFreight(Order order) {
		double weight = StringUtil.string2Double(order.getWeight());
		double tariff = StringUtil.string2Double(order.getTariff());
		double or = StringUtil.string2Double(order.getOr());
		double other = StringUtil.string2Double(order.getOther());
		double premium = StringUtil.string2Double(order.getPremium());

		BigDecimal result = new BigDecimal(premium);
		result = result.add(BigDecimal.valueOf(other));
		result = result.add(BigDecimal.valueOf(or));
		result = result.add(BigDecimal.valueOf(tariff));
		result = result.add(getWeightMoney(weight));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取重量的费用
	 * 
	 * @param weight
	 * @return
	 */
	private BigDecimal getWeightMoney(double weight) {
		if (weight <= OrderFreightConstant.VIP_USER_ORDER_FIRST_WEIGHT) {
			return new BigDecimal(OrderFreightConstant.VIP_USER_ORDER_FIRST_MONEY);
		} else {
			return new BigDecimal(OrderFreightConstant.VIP_USER_ORDER_FIRST_MONEY).add(new BigDecimal(
			        OrderFreightConstant.VIP_USER_RENEWAL_PRICE).multiply(new BigDecimal(
			        2 * (weight - OrderFreightConstant.VIP_USER_ORDER_FIRST_WEIGHT)).setScale(0, BigDecimal.ROUND_UP)));
		}
	}

}
