package org.easywork.orm.hibernate;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import rongding.framework.orm.hibernate.HqlFilter;

public class HqlFilterTest {
	@Test
	public void test1() throws Exception {
		HqlFilter hqlFilter=new HqlFilter();
		List<String> ids=Arrays.asList(new String[]{"1","2"});
		hqlFilter.addFilter("QUERY_t#id_S_IN", ids);
		System.out.println(hqlFilter.getWhereAndOrderHql());
		System.out.println(hqlFilter.getParams());
		
//		Map<String, String> params=new HashMap<String, String>();
//		params.put("p1", "1");
//		params.put("p2", "2");
//		System.out.println(params);
	}
}
