/**
 *新闻实体类
 *张敬琦
 *2015-01-30 
 */

package com.meitao.model;

import java.io.Serializable;


public class News implements Serializable {

	private static final long serialVersionUID = 1567638188660323595L;
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String title; // 50
	private String content; // 4000
	private String releaseTime;
	private String author;//发布的作者
	private String authorid;//发布作者所属id
	private String releasetime;//发布时间
	private String modifytime;//修改时间
	private String viewcontent;
	private String tag;
	private String img;
	private String remark;
	private String pic1;
	private String picture;



	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getViewcontent() {
		return viewcontent;
	}

	public void setViewcontent(String viewcontent) {
		this.viewcontent = viewcontent;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}



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

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + Integer.parseInt(id);
		//result = prime * result + ((img == null) ? 0 : img.hashCode());
		result = prime * result
				+ ((releaseTime == null) ? 0 : releaseTime.hashCode());
		//result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		//result = prime * result + viewCount;
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
		News other = (News) obj;
//		if (author == null) {
//			if (other.author != null)
//				return false;
//		} else if (!author.equals(other.author))
//			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id != other.id)
			return false;
//		if (img == null) {
//			if (other.img != null)
//				return false;
//		} else if (!img.equals(other.img))
			//return false;
		if (releaseTime == null) {
			if (other.releaseTime != null)
				return false;
		} else if (!releaseTime.equals(other.releaseTime))
			return false;
//		if (tag == null) {
//			if (other.tag != null)
//				return false;
//		} else if (!tag.equals(other.tag))
//			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
//		if (viewCount != other.viewCount)
//			return false;
		return true;
	}

	@Override
	public String toString() {
//		return "News [author=" + author + ", content=" + content + ", id=" + id
//				+ ", img=" + img + ", releaseTime=" + releaseTime + ", tag="
//				+ tag + ", title=" + title + ", viewCount=" + viewCount + "]";
//		return "News [author=" + author + ", content=" + content + ", id=" + id
//		+ ", releaseTime=" + releaseTime  + ", title=" + title  + "]";
		return "News [content=" + content + ", id=" + id
		+ ", releaseTime=" + releaseTime  + ", title=" + title  + "]";
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
}
