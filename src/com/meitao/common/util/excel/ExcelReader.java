package com.meitao.common.util.excel;

import java.util.List;

import jxl.Sheet;

public abstract class ExcelReader<T> {
    /**
     * 解析sheet并返回一个list集合
     * 
     * @param sheet
     * @throws Exception
     */
    public List<T> read(Sheet sheet) throws Exception {
        return null;
    }
}
