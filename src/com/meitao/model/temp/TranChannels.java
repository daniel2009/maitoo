package com.meitao.model.temp;
import java.util.List;

//财务记录统计表
import com.meitao.model.Channel;
import com.meitao.model.CommudityAdmin;


public class TranChannels {
	private static final long serialVersionUID = -620021868815266L;
	//获取多个渠道打包返回
	private String number;//渠道的个数
	private List<Channel> channels;//保存渠道的内容
	private List<List<CommudityAdmin>> commudityList;//渠道上的商品更表
	
	public List<List<CommudityAdmin>> getCommudityList() {
		return commudityList;
	}
	public void setCommudityList(List<List<CommudityAdmin>> commudityList) {
		this.commudityList = commudityList;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<Channel> getChannels() {
		return channels;
	}
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

}
