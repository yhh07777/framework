package org.easywork.util.common;

import org.junit.Test;

import rongding.framework.util.common.PropertiesUtil;
import rongding.framework.util.common.PropertyUtil;

public class PropertiesUtilTest {
	@Test
	public void testConvert() throws Exception {
//		PropertiesObject object = PropertiesUtil.getInstance().convert("map",PropertiesObject.class);
//		System.out.println(object);
		
		Double pCenterlng=PropertyUtil.getInstance("map").getDouble("pCenterlng");
		System.out.println(pCenterlng);
	}
}
