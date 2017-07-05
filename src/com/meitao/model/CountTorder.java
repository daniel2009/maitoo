//查找转运单类型数量
package com.meitao.model;

public class CountTorder implements java.io.Serializable{
	private static final long serialVersionUID = -707894526556150402L;
	private int t_totalQ=0;//总转运单数
	private int t_userybQ=0;//用户预报转运单数量
	private int t_wronginputQ=0;//录入失败转运单数量
	private int t_questionQ=0;//异常运单数量
	private int t_traninputQ=0;//转运入库数量
	private int t_tranoutputQ=0;//转运出库
	private int t_haveinputQ=0;//已经入库数量
	private int t_finishedQ=0;//已完成数量
	private int t_getbyselfQ=0;//自提数量
	public int getT_totalQ() {
		return t_totalQ;
	}
	public void setT_totalQ(int t_totalQ) {
		this.t_totalQ = t_totalQ;
	}
	public int getT_userybQ() {
		return t_userybQ;
	}
	public void setT_userybQ(int t_userybQ) {
		this.t_userybQ = t_userybQ;
	}
	public int getT_wronginputQ() {
		return t_wronginputQ;
	}
	public void setT_wronginputQ(int t_wronginputQ) {
		this.t_wronginputQ = t_wronginputQ;
	}
	public int getT_questionQ() {
		return t_questionQ;
	}
	public void setT_questionQ(int t_questionQ) {
		this.t_questionQ = t_questionQ;
	}
	public int getT_traninputQ() {
		return t_traninputQ;
	}
	public void setT_traninputQ(int t_traninputQ) {
		this.t_traninputQ = t_traninputQ;
	}
	public int getT_tranoutputQ() {
		return t_tranoutputQ;
	}
	public void setT_tranoutputQ(int t_tranoutputQ) {
		this.t_tranoutputQ = t_tranoutputQ;
	}
	public int getT_haveinputQ() {
		return t_haveinputQ;
	}
	public void setT_haveinputQ(int t_haveinputQ) {
		this.t_haveinputQ = t_haveinputQ;
	}
	public int getT_finishedQ() {
		return t_finishedQ;
	}
	public void setT_finishedQ(int t_finishedQ) {
		this.t_finishedQ = t_finishedQ;
	}
	public int getT_getbyselfQ() {
		return t_getbyselfQ;
	}
	public void setT_getbyselfQ(int t_getbyselfQ) {
		this.t_getbyselfQ = t_getbyselfQ;
	}
	
	
	
}
