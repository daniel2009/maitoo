package com.meitao.model;

public class MessageControl implements java.io.Serializable{
	private static final long serialVersionUID = -7000601258964150402L;
	private String id;
	private String flag;//保存类型的标志，用于区分短信类型
	private String content;//标志的内容
	private String createDate;//创建时间
	private String ModifyDate;//修改时间
	private String remark;//备注
	private String auto="0";//是否自动处理,1表示自动处理，其它表示不自动处理
	private String processman;//最新操作人
	
	public String getAuto() {
		return auto;
	}
	public void setAuto(String auto) {
		this.auto = auto;
	}
	public String getProcessman() {
		return processman;
	}
	public void setProcessman(String processman) {
		this.processman = processman;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return ModifyDate;
	}
	public void setModifyDate(String modifyDate) {
		ModifyDate = modifyDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
