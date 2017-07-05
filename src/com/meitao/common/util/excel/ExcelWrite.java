package com.meitao.common.util.excel;

import java.util.Collection;
import java.util.Map;

import jxl.write.WritableSheet;

public abstract class ExcelWrite<T> {
    /**
     * 自定义输出
     * 
     * @param headers
     *            自定义的头部信息
     * @param datas
     *            数据
     * @param sheet
     *            sheet对象
     * @throws Exception
     *             write的时候产生exception
     */
    public void write(Map<String, String> headers, Collection<T> datas, WritableSheet sheet) throws Exception {
    }
}
