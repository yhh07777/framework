package org.easywork.orm.sqlcontext;

import java.net.URL;

import org.junit.Test;

import rongding.framework.orm.sqlcontext.SqlContext;

public class SqlContextTest {
	String sql;
	
	// 获取当前方法名称
	@Test
	public void test1() {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		System.out.println(url);
	}
	
	// 直接通过key获取sql
	@Test
	public void testGetSqlByKey() {
		long l1=System.currentTimeMillis();
		sql=SqlContext.getSql("TermQueryService.grid");
		long l2=System.currentTimeMillis();
		System.out.println(l2-l1);
		
		l1=System.currentTimeMillis();
		sql=SqlContext.getSql("TermQueryService.grid");
		l2=System.currentTimeMillis();
		System.out.println(l2-l1);
		
	}
	
}
