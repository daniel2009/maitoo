package com.meitao.model;

public class ProductRecord implements java.io.Serializable{
	private static final long serialVersionUID = -2772376244641839827L;
	private String id;
	private String productId;
	private String userId;
	private String quantity;
	private String date;
	private String state;//记录管理员添加转运与否
	private String remark;
	private String empRemark;
	private String rate;
	private String comment;
	private String commentDate;
	
	private Product product;
	private String userName;
	private String userPhone;
	
	//收件人信息,用于传递到transhipment
	private String cName;
	private String cPhone;
	private String cAddress;
	private String cZipCode;
	private String cCardId;
	private String cIdCardPic;
	private String cIdCardOtherPic;
	private String cIdCardTogetherPic;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getEmpRemark() {
		return empRemark;
	}
	public void setEmpRemark(String empRemark) {
		this.empRemark = empRemark;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getcPhone() {
		return cPhone;
	}
	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}
	public String getcAddress() {
		return cAddress;
	}
	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}
	public String getcZipCode() {
		return cZipCode;
	}
	public void setcZipCode(String cZipCode) {
		this.cZipCode = cZipCode;
	}
	public String getcCardId() {
		return cCardId;
	}
	public void setcCardId(String cCardId) {
		this.cCardId = cCardId;
	}
	public String getcIdCardPic() {
		return cIdCardPic;
	}
	public void setcIdCardPic(String cIdCardPic) {
		this.cIdCardPic = cIdCardPic;
	}
	public String getcIdCardOtherPic() {
		return cIdCardOtherPic;
	}
	public void setcIdCardOtherPic(String cIdCardOtherPic) {
		this.cIdCardOtherPic = cIdCardOtherPic;
	}
	public String getcIdCardTogetherPic() {
		return cIdCardTogetherPic;
	}
	public void setcIdCardTogetherPic(String cIdCardTogetherPic) {
		this.cIdCardTogetherPic = cIdCardTogetherPic;
	}
	
}
