package com.meitao.model;

public class CountMorder implements java.io.Serializable{
	private static final long serialVersionUID = -700556126556150402L;
	private int totalQ=0;//总运单数
	private int onlineQ=0;//在线置单数
	private int abandonQ=0;//作废运单数
	private int questionQ=0;//异常件数
	private int nopayQ=0;//未付款数
	private int tranwaitpQ=0;//转运等待处理运单
	
	public int getTranwaitpQ() {
		return tranwaitpQ;
	}
	public void setTranwaitpQ(int tranwaitpQ) {
		this.tranwaitpQ = tranwaitpQ;
	}
	public int getTotalQ() {
		return totalQ;
	}
	public void setTotalQ(int totalQ) {
		this.totalQ = totalQ;
	}
	public int getOnlineQ() {
		return onlineQ;
	}
	public void setOnlineQ(int onlineQ) {
		this.onlineQ = onlineQ;
	}
	public int getAbandonQ() {
		return abandonQ;
	}
	public void setAbandonQ(int abandonQ) {
		this.abandonQ = abandonQ;
	}
	public int getQuestionQ() {
		return questionQ;
	}
	public void setQuestionQ(int questionQ) {
		this.questionQ = questionQ;
	}
	public int getNopayQ() {
		return nopayQ;
	}
	public void setNopayQ(int nopayQ) {
		this.nopayQ = nopayQ;
	}
	
	
	
}
