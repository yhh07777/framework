package rongding.framework.util.web;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;

public class ParamUtil {
	public static String serize(Object model) {
		StringBuilder sb = new StringBuilder();
		try {
			Class<?> objClass = model.getClass();
			Field[] fields = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].isAccessible()) {
					fields[i].setAccessible(true);
				}
				if (fields[i].get(model)!=null && fields[i].get(model)!="") {
					sb.append("&").append(fields[i].getName())
						.append("=")
						.append(fields[i].get(model));
				}
			}
			sb.replace(0, 1, "?");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("序列化对象错误",e);
		}
		return sb.toString();
	}
	
	public static String addPageParam(String str,String param ) {
		return str+"&pageid="+param;
	}
	
	public static void addParamModel(Map<String, Object> params,Object model) {
		try {
			Class<?> objClass = model.getClass();
			Field[] fields = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].isAccessible()) {
					fields[i].setAccessible(true);
				}
				
				Object value=fields[i].get(model);
				if (value!=null) {
					if (!value.getClass().isPrimitive() && value.getClass()!=String.class && !(value instanceof  Enum)) {
//						throw new RuntimeException(value.getClass()+"不是基本类型或者字符串!");
						continue;
					}
					params.put(fields[i].getName(), URLEncoder.encode(String.valueOf(value),"UTF-8"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("转换页面参数出错!",e);
		}
	}
	
}
