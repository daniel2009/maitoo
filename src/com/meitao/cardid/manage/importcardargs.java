package com.meitao.cardid.manage;

public class importcardargs {

	private String allfileurl;//文件的绝对路径
	private String filename;//文件的名称
	private String cardname;//身份证的名称
	private String saveurl;//保存的路径
	private String cardid;//身份证id号
	private String result;//操作结果
	private boolean flag;//记录成功失败标志
	public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}
	public String getAllfileurl() {
		return allfileurl;
	}
	public void setAllfileurl(String allfileurl) {
		this.allfileurl = allfileurl;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
