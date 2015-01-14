package rongding.framework.util.lang;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import rongding.framework.util.time.DateUtil;

/**
 * String工具类
 */
public class StringUtil {

	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	
	public static String trim(String str) { 
		return str==null?"":str.trim();
	}
	public static boolean isBlank(String str) { 
		return str==null?true:"".equals(str.trim());
	}

	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		if (object.equals("")) {
			return true;
		}
		if (object.equals("null")) {
			return true;
		}
		return false;
	}

	public static String UUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
		return temp;
	}
	
	public static String TIME_ID(String id) {
		if (!isBlank(id)) {
			return id;
		}
		return DateUtil.getDate("yyMMddHHmmssssss");
	}
	
	public static String UUID(String id) {
		if (!StringUtils.isBlank(id)) {
			return id;
		}
		return StringUtil.UUID();
	}
	
	/**
	 * 截取从字符串开头到指定字符位置
	 * @param str
	 * @param ch
	 * @return
	 */
	public static String trunc(String str,String ch) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		int index=str.indexOf(ch);
		if (index>0) {
			return str.substring(0,index);
		}
		return str;
	}
	
	public static int indexIgnoreCase(String str,String s) {
		if (StringUtils.isBlank(str)) {
			return -1;
		}
		return str.toUpperCase().indexOf(s.toUpperCase());
	}
	
	/**
	 * 将一个对象转换为字符串
	 * @param obj
	 * @return
	 */
	public static String convertString(Object obj) {
		return obj==null?"":obj.toString();
	}
	
	public static String getMapValue(Map<String, Object> map,String key) {
		return convertString(map.get(key));
	}
	
	public static boolean compareString(String s1,String s2){
		int i1=Integer.parseInt(s1);
		int i2=Integer.parseInt(s2);
		return i1-i2>0;
	}
	
	public static boolean isBlankExist(String... strs){
		for (String s : strs) {
			if (isBlank(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAllEmpty(String... strs){
		for (String s : strs) {
			if (!isBlank(s)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAllEmpty(Object... strs){
		for (Object s : strs) {
			if (!isEmpty(s)) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(trunc("111111.0","."));
		System.out.println(indexIgnoreCase("select * FROM ","from"));
	}
	
	public static String toUtf8String(String s) { 
        StringBuffer sb = new StringBuffer(); 
        for (int i = 0; i < s.length(); i++) { 
            char c = s.charAt(i); 
            if (c >= 0 && c <= 255) { 
                sb.append(c); 
            } else { 
                byte[] b; 
                try { 
                    b = Character.toString(c).getBytes("utf-8"); 
                } catch (Exception ex) { 
                    System.out.println("将文件名中的汉字转为UTF8编码的串时错误，输入的字符串为：" + s); 
                    b = new byte[0]; 
                } 
                for (int j = 0; j < b.length; j++) { 
                    int k = b[j]; 
                    if (k < 0) 
                        k += 256; 
                    sb.append("%" + Integer.toHexString(k).toUpperCase()); 
                } 
            } 
        } 
        return sb.toString(); 
    } 
}
