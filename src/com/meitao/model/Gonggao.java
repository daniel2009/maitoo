/**
 * 公告实体类
 * 张敬琦
 * 2015-01-30
 */

package com.meitao.model;

import java.io.Serializable;


public class Gonggao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7346904473247579412L;
	//private int id;
	private String id;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	private int viewCount;

	
	private String title; // 50
	private String content; // 4000
	private String releasetime;
	private String author;//发布的作者
	private String authorid;//发布作者所属id
	private String picture;

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public String getViewcontent() {
		return viewcontent;
	}

	public void setViewcontent(String viewcontent) {
		this.viewcontent = viewcontent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}



	private String modifytime;//修改时间
	private String viewcontent;
	private String tag;
	private String img;
	private String remark;

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}



	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + Integer.parseInt(id);;
		result = prime * result + ((img == null) ? 0 : img.hashCode());
		result = prime * result
				+ ((releasetime == null) ? 0 : releasetime.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + viewCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gonggao other = (Gonggao) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id != other.id)
			return false;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		if (releasetime == null) {
			if (other.releasetime != null)
				return false;
		} else if (!releasetime.equals(other.releasetime))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (viewCount != other.viewCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "News [author=" + author + ", content=" + content + ", id=" + id
				+ ", img=" + img + ", releaseTime=" + releasetime + ", tag="
				+ tag + ", title=" + title + ", viewCount=" + viewCount + "]";
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}
}
