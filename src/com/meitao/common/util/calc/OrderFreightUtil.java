package com.meitao.common.util.calc;

import java.util.HashMap;
import java.util.Map;

import com.meitao.common.constants.Constant;
import com.meitao.common.util.calc.impl.NormalUserFreightCalculation;
import com.meitao.common.util.calc.impl.VIPUserFreightCalculation;
import com.meitao.model.Order;


public class OrderFreightUtil {
	public static Map<String, OrderFreightCalculation> calculator = new HashMap<String, OrderFreightCalculation>();

	static {
		// 初始化对应用户的运费计算器
		OrderFreightCalculation calc1 = new NormalUserFreightCalculation();
		OrderFreightCalculation calc2 = new VIPUserFreightCalculation();
		calculator.put(Constant.USER_TYPE_NORMAL, calc1);
		//calculator.put(Constant.USER_TYPE_STORE, calc1);
		//calculator.put(Constant.USER_TYPE_VIP, calc2);
		calculator.put(Constant.USER_TYPE_VIP2, calc2);
		calculator.put(Constant.USER_TYPE_VIP3, calc2);
	}

	/**
	 * 计算Order的运费
	 * 
	 * @param order
	 *            要支付的运单对象
	 * @param type
	 *            用户类型
	 * @return
	 */
	public static double calculationOrderFreight(Order order, String type) {
		if (order == null) {
			return 0.0d;
		}

		// 会员类型是门市会员
		//if (Constant.USER_TYPE_STORE.equals(type)) {
		//	return calculator.get(Constant.USER_TYPE_STORE).calcFreight(order);
		//}

		// 会员类型是vip会员
		//if (Constant.USER_TYPE_VIP.equals(type)) {
		//	return calculator.get(Constant.USER_TYPE_VIP).calcFreight(order);
		//}

		// 会员类型是vip2会员
		if (Constant.USER_TYPE_VIP2.equals(type)) {
			return calculator.get(Constant.USER_TYPE_VIP2).calcFreight(order);
		}

		// 会员类型是vip3会员
		if (Constant.USER_TYPE_VIP3.equals(type)) {
			return calculator.get(Constant.USER_TYPE_VIP3).calcFreight(order);
		}
		// 按照普通会员的计算方式
		return calculator.get(Constant.USER_TYPE_NORMAL).calcFreight(order);
	}
}
