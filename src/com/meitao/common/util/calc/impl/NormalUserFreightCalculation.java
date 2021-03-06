package com.meitao.common.util.calc.impl;

import java.math.BigDecimal;

import com.meitao.common.constants.OrderFreightConstant;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.calc.OrderFreightCalculation;
import com.meitao.model.Order;


/**
 * 普通会员的运费计算
 * 
 * @author Gerry
 * 
 */
public class NormalUserFreightCalculation implements OrderFreightCalculation {

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
		if (weight <= OrderFreightConstant.NORMAL_USER_ORDER_FIRST_WEIGHT) {
			return new BigDecimal(OrderFreightConstant.NORMAL_USER_ORDER_FIRST_MONEY);
		} else {
			return new BigDecimal(OrderFreightConstant.NORMAL_USER_ORDER_FIRST_MONEY).add(new BigDecimal(
			        OrderFreightConstant.NORMAL_USER_RENEWAL_PRICE).multiply(new BigDecimal(
			        2 * (weight - OrderFreightConstant.NORMAL_USER_ORDER_FIRST_WEIGHT))
			        .setScale(0, BigDecimal.ROUND_UP)));
		}
	}

/*	public static void main(String[] args) {
		NormalUserFreightCalculation c = new NormalUserFreightCalculation();
		double weight = 1;
		System.out.println(c.getWeightMoney(weight));
		weight = 2.01;
		System.out.println(c.getWeightMoney(weight));
		weight = 1.49;
		System.out.println(c.getWeightMoney(weight));
		weight = 1.9;
		System.out.println(c.getWeightMoney(weight));
		weight = 2;
		System.out.println(c.getWeightMoney(weight));
		
		BigDecimal decimal = new BigDecimal(1.0000001);
		System.out.println(decimal.setScale(0, BigDecimal.ROUND_UP));
	}*/
}
