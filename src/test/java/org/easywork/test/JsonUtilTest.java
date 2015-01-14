package org.easywork.test;

import org.junit.Test;

import rongding.framework.util.web.FastjsonFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtilTest {
	
	@Test
	public void testFastjson() throws Exception {
		FastjsonFilter filter = new FastjsonFilter(new String[]{"name"});// excludes优先于includes
		jsonObject json=new jsonObject();
		json.setSuccess(true);
		json.setName("hello");
		String str=JSON.toJSONString(json, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
		System.out.println(str);
	}
}
