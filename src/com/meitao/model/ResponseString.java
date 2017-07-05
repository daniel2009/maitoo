package com.meitao.model;





public class ResponseString  {

	private static final long serialVersionUID = -7019752563083810104L;
	private String code;
	private String message;

	private String data;
	
	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	
	public String toString() {
		//在ie浏览器内，接收到的将自动加上<pre>标识符
		return "{\"code\":\"" + code + "\","+"\"message\":\""+message+"\","+"\"data\":\""+data+"\"}";
		//return "{code=" + code + ","+"message="+message+","+"data="+data+"}";
	}

	
	
}
