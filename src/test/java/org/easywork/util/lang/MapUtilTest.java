package org.easywork.util.lang;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import rongding.framework.util.lang.MapUtil;

public class MapUtilTest {
	@Test
	public void testConvertField() {
		String[] fields=new String[]{"1","2","3","4","5","6","7","8","9","A","B"};
		String[] values=new String[]{"已租车","已还车","冲正","丢卡","丢车","只有还车记录","租车异常","","已租车","手续费或租车日期时间异常","租车异常恢复","后续有租还车记录"};
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("key", "1");
		MapUtil.convertField(map, fields,values,"key");
		String key="key";
		String value=(String) map.get(key);
		assertThat(value, is("已租车"));
	}
}
