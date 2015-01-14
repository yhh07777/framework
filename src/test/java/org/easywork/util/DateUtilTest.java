package org.easywork.util;

import org.junit.Test;

import rongding.framework.util.time.DateUtil;

public class DateUtilTest {
	@Test
	public void testCalDate() throws Exception {
		System.out.println(DateUtil.calDate(-7));
	}
	
	@Test
	public void testString2Date() throws Exception {
		
	}
	
	@Test
	public void test2() throws Exception {
		System.out.println(DateUtil.stringtoDatetime("2014-04-01 12:12:00"));
		System.out.println(DateUtil.getDate("yyMMddHHmmssssss"));
	}
}
