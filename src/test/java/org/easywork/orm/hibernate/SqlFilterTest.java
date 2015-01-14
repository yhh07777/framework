package org.easywork.orm.hibernate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import rongding.framework.orm.hibernate.SqlFilter;
import rongding.framework.util.lang.StringUtil;

public class SqlFilterTest {
	// 直接添加替换字符串
	@Test
	public void test13() {
		SqlFilter sqlFilter = new SqlFilter("select * from dual ${AREA_ID}");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("AREA_ID", "11");
		sqlFilter.addReplace("AREA_ID", map," and area_id=:AREA_ID");
		System.out.println(sqlFilter.getSql());
		System.out.println(sqlFilter.getParams());
	}
	
	// 添加一个列表
	@Test
	public void test12() {
		SqlFilter sqlFilter = new SqlFilter("select * from dual ${AREA_ID}");
		sqlFilter.addFilter("SQL#AREA_ID|t1#id|RE_S|IN", Arrays.asList("1","2"));
		System.out.println(sqlFilter.getSql());
	}
	
	@Test
	public void test11() {
		SqlFilter sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter.addFilter("SQL|t1.id|VN_S|EQ", "111,22");
		System.out.println(sqlFilter.getSql());
	}
	
	@Test
	public void test10() {
		SqlFilter sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter.addFilter("SQL|t1.id|OV_S|EQ", "111,22");
		System.out.println(sqlFilter.getSql());
	}
	
	// and (t1.id='1' or and t2.id='2')
	@Test
	public void test9() {
		SqlFilter sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter.addOrFilter("SQL|t1.id,t2.id|S|EQ", "111");
		System.out.println(sqlFilter.getSql());
		
		sqlFilter=new SqlFilter("select * from dual ");
		sqlFilter.addFilter("SQL|t1.id,t2.id|OR_S|EQ", "111");
		System.out.println(sqlFilter.getSql());
	}
	
	@Test
	public void test0() {
//		int c=0x5;
//		String s1=new DecimalFormat("000").format(c);
//		System.out.println(s1);
//		
//		c=0x1;
//		s1=new DecimalFormat("000").format(c);
//		System.out.println(s1);
		
//		String s=new DecimalFormat("000").format(Integer.toBinaryString(c));
		System.out.println(Integer.parseInt(Integer.toBinaryString(0x05)));
		String s=new DecimalFormat("000").format(Integer.parseInt(Integer.toBinaryString(0x05)));
		System.out.println(s);
		if (s.charAt(0)=='1') {
			System.out.println("where");
		}
		if (s.charAt(1)=='1') {
			System.out.println("special");
		}
		if (s.charAt(2)=='1') {
			System.out.println("orderby");
		}
		
	}
	
	@Test
	public void test1() throws Exception {
		SqlFilter sqlFilter=new SqlFilter();
		List<String> ids=Arrays.asList(new String[]{"1","2"});
		sqlFilter.addFilter("SQL|t#id|MUL|IN", "1|2");
		
//		sqlFilter.addFilter("SQL|t#id|AR|IN", "1,2");
//		sqlFilter.addFilter("SQL_t#id_S_IN", ids);
//		sqlFilter.addFilter("SQL_t2#NAME_S_LK", "杭州");
		System.out.println(sqlFilter.getSql());
		System.out.println(sqlFilter.getParams());
	}
	
	//转换{}
	@Test
	public void test2() throws Exception {
		SqlFilter sqlFilter=new SqlFilter("select {t1.*,t2.NAME }from NET_MANAGE_TBL t1,T_AREA t2 where 1=1");
		sqlFilter.addFilter("SQL|t2#NAME|S|LK", "杭州");
		sqlFilter.addSortAndOrder("t1.CREATE_TIME", "desc");
		sqlFilter.setSpecial(true);
		System.out.println(sqlFilter.getSql());
		System.out.println("countSql-->"+sqlFilter.getCountSql());
		System.out.println("params-->"+sqlFilter.getParams());
	}
	
	@Test
	public void test3() {
		String sql="select count{t1.*,t2.NAME} from NET_MANAGE_TBL t1,T_AREA t2 where 1=1";
		int start=sql.indexOf('{');
		int end=sql.indexOf('}');
		System.out.println("start="+start);
		System.out.println("end="+end);
		String newSql = new String( sql.substring(0, start)+"(*)" +sql.substring(end+1) ); 
		System.out.println(newSql);
		
	}
	
	// 日期字符串$-->||
	@Test
	public void test4() {
		String columnName = "SQL|to_date(t#TRAN_DATE$TRAN_TIME,'yyyy-MM-dd hh24:mi:ss')|D|GE".replaceAll("#", ".");		
		System.out.println(columnName);
		columnName=columnName.replace("$", "||");
		System.out.println(columnName);
		
		System.out.println("$$ddsfsdf".replace("$", "||"));
	}
	
	// 替代字符串
	@Test
	public void test5() {
		String sql="SELECT CASE WHEN to_number(a.sell_time) > to_number(b.return_time) THEN 1  ELSE 2  END as flag,a.sell_time，b.return_time FROM ("
				+"SELECT MAX(sell_time) AS sell_time FROM ("
				+"	SELECT sell_time FROM zcard_use_tbl WHERE tran_flag = '1' ${0}${1} UNION ALL SELECT sell_time FROM zcard_his_use_tbl WHERE tran_flag = '1' ${0}${1} ORDER BY sell_time)) a, ("
				+"SELECT MAX(return_time) AS return_time FROM ("
				+"	SELECT return_time FROM zcard_reback_tbl WHERE  tran_flag = '1' ${0}${1} UNION ALL SELECT return_time FROM zcard_his_reback_tbl WHERE  tran_flag = '1'  ${0}${1} ORDER BY return_time ) ) b";
		System.out.println("old-->"+sql);
		char[] arr=sql.toCharArray();
		StringBuffer sb=new StringBuffer();
		StringBuffer inner=new StringBuffer();
		List<String> replaceArr=new ArrayList<String>();
		int count=0;
		boolean flag=false;
		Map<String, Object> params=new HashMap<String, Object>();
		if (!StringUtil.isBlank("11")) {
			replaceArr.add(" and card_id=:card_id ");
			params.put("card_id", "card_id");
		}
		if (!StringUtil.isBlank("11")) {
			replaceArr.add(" and card_no=:card_no ");
			params.put("card_no", "card_no");
		}
		for (int i = 0; i < arr.length; i++) {
			char c=arr[i];
			if ('$'==c) {
				if (i+1<=arr.length && arr[i+1]=='{') {
					System.out.println("flag==true:"+i);
					inner=new StringBuffer();
					flag=true;
				}
				continue;
			}
			if (flag) {
				if ('}'==c) {
					System.out.println("flag==false:"+i);
					count=Integer.parseInt(inner.toString());
					System.out.println("==================replace==================");
					if (replaceArr!=null&&replaceArr.size()>0 &&count<replaceArr.size()) {
						System.out.println("replaceArr.get(count)="+replaceArr.get(count));
						sb.append(replaceArr.get(count));
					}
					System.out.println("inner="+inner);
					flag=false;
				}else {
					if ('{'!=c) {
						inner.append(c);
					}
				}
				continue;
			}
			sb.append(c);
		}
		System.out.println("new-->"+sb);
	}
	
	//排除掉|的影响
	@Test
	public void test6() {
		String value="1|2";
		String[] values=StringUtils.split(value,"|");
		System.out.println(Arrays.toString(values));
		
		value="1";
		System.out.println(Arrays.toString(StringUtils.split(value,"|") ));
	}
	
	//增加多个and
	@Test
	public void test7() {
		String columnName = "SQL|t.a1,t.a2|UN_S|GE".replaceAll("#", ".");		
		System.out.println(columnName);
		SqlFilter sqlFilter=new SqlFilter();
		sqlFilter.addFilter(columnName,"1");
		System.out.println(sqlFilter.getSqlWhere());
	}
	
	//替换字符串
	@Test
	public void test8() {
		String name = "SQL#card_id|t.id|RE_S|EQ";		
		SqlFilter sqlFilter=new SqlFilter("select * from dual ${card_id}");
//		sqlFilter.addFilter(columnName,"1");
//		sqlFilter.addReplace("card_id", "where 1=1");
		sqlFilter.addFilter(name, "xxx");
		
		System.out.println(sqlFilter.getSql());
	}
}
