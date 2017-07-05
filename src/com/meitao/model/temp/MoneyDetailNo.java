package com.meitao.model.temp;
//财务记录统计表
import com.meitao.model.M_order;


public class MoneyDetailNo {
	private static final long serialVersionUID = -620242368816966L;
	private double allcustomerMoney=0;//总消费类总额
	private int allcustomerMoney_no=0;//数量
	private int allcustomerMoney_no_q=0;//问题数量
	
	
	private double allchangestateMoney=0;//总状态变更金额
	private int allchangestateMoney_no=0;//总状态变更_数量
	private int allchangestateMoney_no_q=0;//总状态变更_数量
	private double allCredit_usa=0;//总信用卡充值美元
	private double allCredit_usa_no=0;//总信用卡充值美元数量
	
	private double allCredit_rm=0;//总信用卡充值人币
	private int allCredit_rm_no=0;//总信用卡充值人币数量
	private int allCredit_rm_no_q=0;//总信用卡充值人币数量
	private double alpayNopay=0;//支付宝未充值金额
	private int alpayNopay_no=0;//支付宝未充值数量
	private int alpayNopay_no_q=0;//支付宝未充值数量
	private double alpaysuccesspay=0;//支付宝充值成功
	private int alpaysuccesspay_no=0;//支付宝充值成功数量
	private int alpaysuccesspay_no_q=0;//支付宝充值成功数量
	private double alpayfailedpay=0;//支付宝充值失败数量
	private int alpayfailedpay_no=0;//支付宝充值失败数量
	private int alpayfailedpay_no_q=0;//支付宝充值失败数量
	
	private int admin_nosure_no=0;//管理员充值未确认值
	private int admin_nosure_no_q=0;//管理员充值未确认值
	private double admin_nosure_usa=0;//管理员未确认美元金额
	private double admin_nosure_rm=0;//管理员未确认人民币金额
	
	private int admin_sure_no=0;//管理员充值已确认值
	private int admin_sure_no_q=0;//管理员充值已确认值
	private double admin_sure_usa=0;//管理员已确认美元金额
	private double admin_sure_rm=0;//管理员已确认人民币金额
	
	
	
	public int getAllcustomerMoney_no_q() {
		return allcustomerMoney_no_q;
	}
	public void setAllcustomerMoney_no_q(int allcustomerMoney_no_q) {
		this.allcustomerMoney_no_q = allcustomerMoney_no_q;
	}
	public int getAllchangestateMoney_no_q() {
		return allchangestateMoney_no_q;
	}
	public void setAllchangestateMoney_no_q(int allchangestateMoney_no_q) {
		this.allchangestateMoney_no_q = allchangestateMoney_no_q;
	}
	public int getAllCredit_rm_no_q() {
		return allCredit_rm_no_q;
	}
	public void setAllCredit_rm_no_q(int allCredit_rm_no_q) {
		this.allCredit_rm_no_q = allCredit_rm_no_q;
	}
	public int getAlpayNopay_no_q() {
		return alpayNopay_no_q;
	}
	public void setAlpayNopay_no_q(int alpayNopay_no_q) {
		this.alpayNopay_no_q = alpayNopay_no_q;
	}
	public int getAlpaysuccesspay_no_q() {
		return alpaysuccesspay_no_q;
	}
	public void setAlpaysuccesspay_no_q(int alpaysuccesspay_no_q) {
		this.alpaysuccesspay_no_q = alpaysuccesspay_no_q;
	}
	public int getAlpayfailedpay_no_q() {
		return alpayfailedpay_no_q;
	}
	public void setAlpayfailedpay_no_q(int alpayfailedpay_no_q) {
		this.alpayfailedpay_no_q = alpayfailedpay_no_q;
	}
	public int getAdmin_nosure_no_q() {
		return admin_nosure_no_q;
	}
	public void setAdmin_nosure_no_q(int admin_nosure_no_q) {
		this.admin_nosure_no_q = admin_nosure_no_q;
	}
	public int getAdmin_sure_no_q() {
		return admin_sure_no_q;
	}
	public void setAdmin_sure_no_q(int admin_sure_no_q) {
		this.admin_sure_no_q = admin_sure_no_q;
	}
	public double getAllcustomerMoney() {
		return allcustomerMoney;
	}
	public void setAllcustomerMoney(double allcustomerMoney) {
		this.allcustomerMoney = allcustomerMoney;
	}
	public int getAllcustomerMoney_no() {
		return allcustomerMoney_no;
	}
	public void setAllcustomerMoney_no(int allcustomerMoney_no) {
		this.allcustomerMoney_no = allcustomerMoney_no;
	}
	public double getAllchangestateMoney() {
		return allchangestateMoney;
	}
	public void setAllchangestateMoney(double allchangestateMoney) {
		this.allchangestateMoney = allchangestateMoney;
	}
	public int getAllchangestateMoney_no() {
		return allchangestateMoney_no;
	}
	public void setAllchangestateMoney_no(int allchangestateMoney_no) {
		this.allchangestateMoney_no = allchangestateMoney_no;
	}
	public double getAllCredit_usa() {
		return allCredit_usa;
	}
	public void setAllCredit_usa(double allCredit_usa) {
		this.allCredit_usa = allCredit_usa;
	}
	public double getAllCredit_usa_no() {
		return allCredit_usa_no;
	}
	public void setAllCredit_usa_no(double allCredit_usa_no) {
		this.allCredit_usa_no = allCredit_usa_no;
	}
	public double getAllCredit_rm() {
		return allCredit_rm;
	}
	public void setAllCredit_rm(double allCredit_rm) {
		this.allCredit_rm = allCredit_rm;
	}
	public int getAllCredit_rm_no() {
		return allCredit_rm_no;
	}
	public void setAllCredit_rm_no(int allCredit_rm_no) {
		this.allCredit_rm_no = allCredit_rm_no;
	}
	public double getAlpayNopay() {
		return alpayNopay;
	}
	public void setAlpayNopay(double alpayNopay) {
		this.alpayNopay = alpayNopay;
	}
	public int getAlpayNopay_no() {
		return alpayNopay_no;
	}
	public void setAlpayNopay_no(int alpayNopay_no) {
		this.alpayNopay_no = alpayNopay_no;
	}
	public double getAlpaysuccesspay() {
		return alpaysuccesspay;
	}
	public void setAlpaysuccesspay(double alpaysuccesspay) {
		this.alpaysuccesspay = alpaysuccesspay;
	}
	public int getAlpaysuccesspay_no() {
		return alpaysuccesspay_no;
	}
	public void setAlpaysuccesspay_no(int alpaysuccesspay_no) {
		this.alpaysuccesspay_no = alpaysuccesspay_no;
	}
	public double getAlpayfailedpay() {
		return alpayfailedpay;
	}
	public void setAlpayfailedpay(double alpayfailedpay) {
		this.alpayfailedpay = alpayfailedpay;
	}
	public int getAlpayfailedpay_no() {
		return alpayfailedpay_no;
	}
	public void setAlpayfailedpay_no(int alpayfailedpay_no) {
		this.alpayfailedpay_no = alpayfailedpay_no;
	}
	public int getAdmin_nosure_no() {
		return admin_nosure_no;
	}
	public void setAdmin_nosure_no(int admin_nosure_no) {
		this.admin_nosure_no = admin_nosure_no;
	}
	public double getAdmin_nosure_usa() {
		return admin_nosure_usa;
	}
	public void setAdmin_nosure_usa(double admin_nosure_usa) {
		this.admin_nosure_usa = admin_nosure_usa;
	}
	public double getAdmin_nosure_rm() {
		return admin_nosure_rm;
	}
	public void setAdmin_nosure_rm(double admin_nosure_rm) {
		this.admin_nosure_rm = admin_nosure_rm;
	}
	public int getAdmin_sure_no() {
		return admin_sure_no;
	}
	public void setAdmin_sure_no(int admin_sure_no) {
		this.admin_sure_no = admin_sure_no;
	}
	public double getAdmin_sure_usa() {
		return admin_sure_usa;
	}
	public void setAdmin_sure_usa(double admin_sure_usa) {
		this.admin_sure_usa = admin_sure_usa;
	}
	public double getAdmin_sure_rm() {
		return admin_sure_rm;
	}
	public void setAdmin_sure_rm(double admin_sure_rm) {
		this.admin_sure_rm = admin_sure_rm;
	}
	
	



}
