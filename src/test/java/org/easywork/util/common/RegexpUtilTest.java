package org.easywork.util.common;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import rongding.framework.util.common.RegexpUtil;


public class RegexpUtilTest {
	
	//0-9的1-8个数字
	@Test
	public void test1() {
		String regexp="^[0-9]{1,8}$";
		boolean flag=RegexpUtil.matches("s220", regexp);
		assertThat(flag, is(false));
		flag=RegexpUtil.matches("3220s", regexp);
		assertThat(flag, is(false));
		flag=RegexpUtil.matches("  32212220", regexp);
		assertThat(flag, is(false));
		flag=RegexpUtil.matches("2212220 ", regexp);
		assertThat(flag, is(false));
		flag=RegexpUtil.matches("2212220", regexp);
		assertThat(flag, is(true));
	}
	
	@Test
	public void test2() {
		boolean flag=RegexpUtil.matches("a014101401", "^[0-9]{1,8}$");
		assertThat(flag, is(true));
	}
}
