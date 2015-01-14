package rongding.framework.util.web;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.collection.internal.PersistentSet;

import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * 主要用于过滤不需要序列化的属性，或者包含需要序列化的属性
 */
public class FastjsonFilter implements PropertyFilter {
	private final Set<String> includes = new HashSet<String>();
	private final Set<String> excludes = new HashSet<String>();

	public Set<String> getIncludes() {
		return includes;
	}

	public Set<String> getExcludes() {
		return excludes;
	}

	public FastjsonFilter() {
	}
	
	public FastjsonFilter(String[] excludes) {
		this.excludes.addAll(Arrays.asList(excludes));
	}
	
	public FastjsonFilter(String[] includes,String[] excludes) {
		this.includes.addAll(Arrays.asList(excludes));
		this.excludes.addAll(Arrays.asList(excludes));
	}
	
	@Override
	public boolean apply(Object source, String name, Object value) {
		if (value != null && (value instanceof PersistentSet)) {	// 避免hibernate对象循环引用，一切Set属性不予序列化
			return false;
		}
		if (excludes.contains(name)) {								// 如果排除集合中包含该属性名称
			return false;
		}
		if (excludes.contains(source.getClass().getSimpleName() + "." + name)) {
			return false;
		}
		if (includes.size() == 0 || includes.contains(name)) {		// 如果包含集合中没有元素 || 包含集合中包含该属性名称
			return true;
		}
		if (includes.contains(source.getClass().getSimpleName() + "." + name)) {
			return true;
		}
		return false;
	}

}
