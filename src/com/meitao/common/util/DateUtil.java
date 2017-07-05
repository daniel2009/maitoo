package com.meitao.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.meitao.exception.ServiceException;


/**
 * 时间工具类
 */
public class DateUtil {

	/**
	 * 时间格式
	 */
	public static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将时间对象转换为定义格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(date);
	}

	/**
	 * 将时间格式转换为给定格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2String(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 将long型的时间time，转换为定义格式的时间字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String long2String(long time) {
		return date2String(new Date(time));
	}

	/**
	 * 将规定格式的时间字符串转换为date对象<br/>
	 * 如果参数时间date不符合定义的格式，那么会抛出一个serviceexception。
	 * 
	 * @param date
	 * @return
	 * @throws ServiceException
	 */
	public static Date string2Date(String date) throws ServiceException {
		try {
			return new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(date);
		} catch (ParseException e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	//计算两个时间之间的天数
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    } 

	public static int compareDate(Date dt1,Date dt2){
        if (dt1.getTime() > dt2.getTime()) {
            System.out.println("dt1 在dt2前");
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            System.out.println("dt1在dt2后");
            return -1;
        } else {//相等
            return 0;
        }
	}
	//获取下一天的方法
	public static Date getNextDate(Date date){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date date1 = new Date(calendar.getTimeInMillis());
			return date1;
		}
	
}
