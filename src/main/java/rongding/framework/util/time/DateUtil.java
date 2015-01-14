package rongding.framework.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/** yyyy-MM-dd HH:mm:ss */
	public static final String DEFAULT_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
	/** yyyyMMddHHmmss */
	public static final String SIMPLE_FORMAT_NORMAL = "yyyyMMddHHmmss";
	/** yyyy/MM/dd HH:mm:ss */
	public static final String DEFAULT_FORMAT_EXT = "yyyy/MM/dd HH:mm:ss";
	/** dd-MM-yyyy HH:mm:ss */
	public static final String DD_MM_YYYY_HH_MM_SS_NORMAL = "dd-MM-yyyy HH:mm:ss";
	/** dd/MM/yyyy HH:mm:ss */
	public static final String DD_MM_YYYY_HH_MM_SS_EXT = "dd/MM/yyyy HH:mm:ss";
	/** yyyy-MM-dd */
	public static final String YYYY_MM_DD_NORMAL = "yyyy-MM-dd";
	/** yyyy/MM/dd */
	public static final String YYYY_MM_DD_EXT = "yyyy/MM/dd";
	/** dd-MM-yyyy */
	public static final String DD_MM_YYYY_NORMAL = "dd-MM-yyyy";
	/** dd/MM/yyyy */
	public static final String DD_MM_YYYY_EXT = "dd/MM/yyyy";
	/** dd/MM/yyyy */
	public static final String HH_MM_SS_NORMAL = "HH:mm:ss";
	/** dd/MM/yyyy */
	public static final String HH_MM_SS_NORMAL_EXT = "HHmmss";
	
	public static String getDatetime() {
		return getDate(DEFAULT_FORMAT_NORMAL);
	}
	public static String getDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_NORMAL);
		return formatter.format(date);
	}
	public static String getDatetime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT_NORMAL);
		return formatter.format(date);
	}
	
	public static String getDate(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}
	
	public static String date2Str(Date date, String pattern) {
		if (date==null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}
	
	public static String date2Str(Date date) {
		return date2Str(date,DEFAULT_FORMAT_NORMAL);
	}
	
	/**
	 * 计算日期
	 * @param date  时间对象
	 * @param count 以天为单位:正的表示往后，负的表示往前
	 * @param format 日期格式
	 * @return
	 */
	public static String calDate(Date date,int count,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date2 = calDays(date, count);
		return formatter.format(date2);
	}
	
	public static String calDate(int count,String format){
		return calDate(new Date(),count,format);
	}
	
	public static String calDate(int count){
		return calDate(count,YYYY_MM_DD_NORMAL);
	}
	
	public static String calDate(String date,int count){
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_NORMAL);
			Date date1 = formatter.parse(date);
			Date date2 = calDays(date1, count);
			String dat = formatter.format(date2);
			return dat;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Date calDays(Date date, int days) {
		return cal(date, days, Calendar.DATE);
	}

	public static Date calYears(Date date, int years) {
		return cal(date, years, Calendar.YEAR);
	}

	private static Date cal(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 将日期字符串和时间字符串组合返回时间对象
	 * yyyyMMddHHmmss -> yyyy-MM-dd HH:mm:ss
	 * @param date 日期字符串
	 * @param time 时间字符串
	 * @return
	 */
	public static Date stringtoDate(String date, String time){
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT_NORMAL);
		try {
			return formatter.parse(convertDateTime(date,time));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将日期字符串和时间字符串组合返回时间字符串
	 * yyyyMMddHHmmss -> yyyy-MM-dd HH:mm:ss
	 * @param date 日期字符串
	 * @param time 时间字符串
	 * @return
	 */
	public static String convertDatetime(String datetime){
		return datetime.substring(0, 4) + "-"
				+ datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " "
				+ datetime.substring(8, 10) + ":" + datetime.substring(10, 12) + ":"
				+ datetime.substring(12, 14);
	}
	
	/**
	 * 将日期字符串和时间字符串组合返回时间字符串
	 * date:yyyyMMdd time:HHmmss -> yyyy-MM-dd HH:mm:ss
	 * @param date 日期字符串
	 * @param time 时间字符串
	 * @return
	 */
	public static String convertDateTime(String date, String time){
		return date.substring(0, 4) + "-"
				+ date.substring(4, 6) + "-" + date.substring(6, 8) + " "
				+ time.substring(0, 2) + ":" + time.substring(2, 4) + ":"
				+ time.substring(4, 6);
	}
	
	public static String convertDate(String date){
		return date.substring(0, 4) + "-"+ date.substring(4, 6) + "-" + date.substring(6, 8);
	}
	
	public static String convertTime(String time){
		return time.substring(0, 2) + ":" + time.substring(2, 4) + ":"+ time.substring(4, 6);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss -> yyyyMMddHHmmss
	 */
	public static String stringtoDatetime(String str){
		String date = (str.substring(0, 4) + str.substring(5, 7)
				+ str.substring(8, 10) + str.substring(11, 13)
				+ str.substring(14, 16) + str.substring(17, 19));
		return date;
	}
}
