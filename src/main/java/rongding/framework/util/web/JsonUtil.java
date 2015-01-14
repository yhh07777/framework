package rongding.framework.util.web;

import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
	public static String writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties) {
		FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
		if (excludesProperties != null && excludesProperties.length > 0) {
			filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
		}
		if (includesProperties != null && includesProperties.length > 0) {
			filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
		}
		return JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
	}

	public static String writeText(Object object, String[] includesProperties, String[] excludesProperties) {
		FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
		if (excludesProperties != null && excludesProperties.length > 0) {
			filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
		}
		if (includesProperties != null && includesProperties.length > 0) {
			filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
		}
		return JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
	}

	public static String writeText(Object object) {
		return writeText(object, null, null);
	}

	public static String writeJson(Object json) {
		return writeJsonByFilter(json, null, null);
	}

	public static String writeJsonByIncludesProperties(Object object, String[] includesProperties) {
		return writeJsonByFilter(object, includesProperties, null);
	}

	public static String writeJsonByExcludesProperties(Object object, String[] excludesProperties) {
		return writeJsonByFilter(object, null, excludesProperties);
	}
}
