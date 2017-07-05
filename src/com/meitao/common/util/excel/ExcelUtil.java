package com.meitao.common.util.excel;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	/**
	 * 从输入流input中获取excel表格信息， 并使用reader解析器解析，返回一个list集合
	 * 
	 * @param reader
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> importExcel(ExcelReader<T> reader, InputStream input) throws Exception {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(input);
			Sheet sheet = workbook.getSheet(0);
			return reader.read(sheet);
		} catch (Throwable e) {
			throw new Exception(e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 自定义title和headers，通过write对象的write方法将数据datas输出到流out中去
	 * 
	 * @param write
	 * @param title
	 * @param headers
	 * @param datas
	 * @param out
	 * @throws Exception
	 */
	public static <T> void exportExcel(ExcelWrite<T> write, String title, final Map<String, String> headers,
	        final Collection<T> datas, final OutputStream out) throws Exception {
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(out);
			WritableSheet sheet = workbook.createSheet(title, 0);
			// 执行写数据
			write.write(headers, datas, sheet);
			// 将数据写入到outputstream中去
			workbook.write();
		} catch (Throwable e) {
			throw new Exception(e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Throwable e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 通过模板templetFile将datas，使用write对象的write方法输出out流中去。
	 * 
	 * @param write
	 * @param templetFile
	 * @param datas
	 * @param out
	 * @throws Exception
	 */
	public static <T> void exportExcel(ExcelWrite<T> write, File templetFile, Collection<T> datas, OutputStream out)
	        throws Exception {
		Workbook wb = null;
		WritableWorkbook writableWb = null;

		try {
			wb = Workbook.getWorkbook(templetFile);
			writableWb = Workbook.createWorkbook(out, wb, new WorkbookSettings());
			WritableSheet sheet = writableWb.getSheet(0);
			if (sheet == null) {
				sheet = writableWb.createSheet("first one", 0);
			}
			write.write(null, datas, sheet);
			writableWb.write();
		} catch (Throwable e) {
			if(out!=null)
			{
				out.flush();
				out.close();
			}
			throw new Exception(e);
		} finally {
			if (writableWb != null) {
				try {
					writableWb.close();
				} catch (Throwable e) {
					// ignore
				}
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (Throwable e) {
					// ignore
				}
			}
			
			/*if(out!=null)
			{
				
				out.close();
			}*/
		}
	}
}
