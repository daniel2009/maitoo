package com.meitao.model.temp;

import com.meitao.model.Order;

public class ExportOrder extends Order {
    private static final long serialVersionUID = 4070079893099785653L;
    private String [] comm_type;//导入状态时，是否导出
	public String[] getComm_type() {
		return comm_type;
	}
	public void setComm_type(String[] comm_type) {
		this.comm_type = comm_type;
	}
}
