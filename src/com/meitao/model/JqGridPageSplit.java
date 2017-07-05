package com.meitao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JqGridPageSplit<T> implements Serializable {

	private static final long serialVersionUID = -4620356393012335556L;
	private int total;//总页数
	private int page;//当前页
	private int records;//查出的记录数
	//private int pageNow;

	
	
	private List<T> rows = new ArrayList<T>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		if (rows != null && !rows.isEmpty()) {
			for (T t : rows) {
				this.rows.add(t);
			}
		}
		
	
	}

	public void addRows(T row) {
		this.rows.add(row);
	}


	
}
