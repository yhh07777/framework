package rongding.framework.util.lang;

import java.math.BigDecimal;
import java.util.Map;

import rongding.framework.util.time.DateUtil;

/**
 * 
 * @author zxf
 *
 */
public class MapUtil {
	public static boolean isEmpty (Map<String, Object> map) {
		return map==null?true:map.size()==0?true:false;
	}
	
	public static double convertDouble (Map<String, Object> map,String key) {
		Object obj=map.get(key);
		double value=0.0;
		if (obj==null) {
			map.put(key, 0.0);
			return value;
		}
		if (obj.getClass()==BigDecimal.class) {
			BigDecimal b=(BigDecimal) map.get(key);
			value=b.doubleValue();
			map.put(key, value);
		}else if (obj.getClass()==Double.class||obj.getClass()==double.class) {
			value=(Double) map.get(key);
			map.put(key, value);
		}
		return value;
	}
	
	public static String getString (Map<String, Object> map,String name) {
		if (name==null) {
			return null;
		}else {
			Object value=map.get(name);
			return value==null?"":value.toString();
		}
	}
	
	public static void convertDouble (Map<String, Object> map,String... keys) {
		for (String k : keys) {
			convertDouble(map,k);
		}
	}
	
	public static String str2Datetime (Map<String, Object> map,String datetime) {
		String s=(String) map.get(datetime);
		if (!StringUtil.isBlank(s)) {
			s=DateUtil.convertDatetime(s);
		}
		map.put(datetime, s);
		return s;
	}
	
	public static String str2Date (Map<String, Object> map,String date) {
		String s=(String) map.get(date);
		if (!StringUtil.isBlank(s)&&s.length()>=14) {
			s=DateUtil.convertDate(s);
		}
		map.put(date, s);
		return s;
	}
	
	public static String str2Time (Map<String, Object> map,String time) {
		String s=(String) map.get(time);
		if (!StringUtil.isBlank(s)&&s.length()>=14) {
			s=DateUtil.convertTime(s);
		}
		map.put(time, s);
		return s;
	}
	
	public static String str2Datetime (Map<String, Object> map,String date,String time,String key) {
		date=(String) map.get(date);
		time=(String) map.get(time);
		String s="";
		if (!StringUtil.isBlank(date) && !StringUtil.isBlank(time) &&date.length()>=8&&time.length()>=6) {
			s=DateUtil.convertDateTime(date, time);
		}
		map.put(key, s);
		return s;
	}
	
	public static String convertField (Map<String, Object> map,String[] fields,String[] values,String key) {
		String value = StringUtil.convertString(map.get(key));
		String s="";
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].equals(value)) {
				s=values[i];
			}
		}
		map.put(key, s);
		return s;
	}
}
