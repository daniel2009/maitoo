package com.meitao.common.util;

import com.meitao.model.PageSplit;

public class PageSplitUtil {
	public static <T> void setPage(PageSplit<T> pageSplit, int pageIndex, int pageSize, int totalResult){
		pageSize = Math.max(pageSize, 1);
		int pageCount = totalResult / pageSize + (totalResult % pageSize == 0 ? 0 : 1);
		pageIndex = Math.max(1, pageIndex);
		pageIndex = Math.min(pageIndex, pageCount);
		
		pageSplit.setPageCount(pageCount);
		pageSplit.setPageNow(pageIndex);
		pageSplit.setPageSize(pageSize);
		pageSplit.setRowCount(totalResult);
		pageSplit.setPageSize(pageSize);
	}
	public static <T> int setPageAndGetFirstResult(PageSplit<T> pageSplit, int pageIndex, int pageSize, int totalResult){
		PageSplitUtil.setPage(pageSplit, pageIndex, pageSize, totalResult);
		int firstResult = (pageSplit.getPageNow() - 1) * pageSplit.getPageSize();
		return firstResult;
	}
}
