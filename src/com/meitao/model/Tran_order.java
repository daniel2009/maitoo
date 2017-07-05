//kai 20160620 客户提交预报时处理的数据

package com.meitao.model;

import java.io.Serializable;
import java.util.List;



public class Tran_order implements Serializable {

	private static final long serialVersionUID = -5732356812345958890L;

	List<String> torderIds;//选择提交的预报id,此id必须是同一个入库仓库的转运单号，不能为空
	List<M_order> morder;//运单数据，参考在线置单的提交方法
	
	public List<String> getTorderIds() {
		return torderIds;
	}
	public void setTorderIds(List<String> torderIds) {
		this.torderIds = torderIds;
	}
	public List<M_order> getMorder() {
		return morder;
	}
	public void setMorder(List<M_order> morder) {
		this.morder = morder;
	}

	
	
	

   
}
