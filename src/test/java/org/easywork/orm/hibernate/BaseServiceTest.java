package org.easywork.orm.hibernate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ognl.Ognl;
import ognl.OgnlException;

import org.junit.Test;

public class BaseServiceTest {
	@Test
	public void testBatchExecuteSql() {
//		String sql="select * from t_user where id=:id and username=:username";
		String sql="update t_area set id=:id, username=:username";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", "v1");
		params.put("username", 0);
		Set<String> keys=new HashSet<String>();
		boolean flag=false;
		StringBuffer sb=null;
		int len=sql.length();
		for (int i = 0; i < len; i++) {
			char c=sql.charAt(i);
			if (flag) {
				sb.append(c);
			}
			if (c==':') {
				flag=true;
				sb=new StringBuffer();
			}
			if (flag&&c==' ' || flag&&i==len-1 || flag&&c==',') {
				flag=false;
				keys.add(sb.toString());
				sb=new StringBuffer();
			}
		}
		Map<String, Object> newParams=new HashMap<String, Object>();
		for (String k : keys) {
			String t=k.trim();
			int l=k.length()-1;
			System.out.println(t);
			if (k.charAt(l)==',') {
				t=k.substring(0, l);
			}
			Object v=params.get(t);
			if (v==null) {
				throw new RuntimeException("key="+k+" is not set");
			}
			newParams.put(t, v);
		}
		System.out.println(newParams);
	}
	
	@Test
	public void test2() {
		String sql="update t_area set id=:id, username=:username";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", "v1");
		params.put("username", 0);
		try {
			installPlaceholderSqlParam(sql,params);
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		
	}
	
	private Map<String,Object> installPlaceholderSqlParam(String executeSql,Map sqlParamsMap) throws OgnlException{
		Map<String,Object> map = new HashMap<String,Object>();
		String regEx = ":[ tnx0Bfr]*[0-9a-z.A-Z]+"; // 表示以：开头，[0-9或者.或者A-Z大小都写]的任意字符，超过一个
		Pattern pat = Pattern.compile(regEx);
		Matcher m = pat.matcher(executeSql);
		while (m.find()) {
			System.out.println(" Match [" + m.group() +"] at positions " + m.start() + "-" + (m.end() - 1));
			String ognl_key = m.group().replace(":","").trim();
			map.put(ognl_key, Ognl.getValue(ognl_key, sqlParamsMap));
		}
		return map;
	}
}
