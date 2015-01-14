package org.easywork.util.lang;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class StringUtilTest {
	private static final String SUB_PACKAGE_SCREEN__SUFFIX = ".*";
	private static final String SUB_PACKAGE_SCREEN__SUFFIX_RE = ".\\*";//替换使用
	
	@Test
	public void test1() {
//		getClasses("org.easywor.*");
		
		String pack="org.easywor.org.*easywork.*";
		String[] packArr = pack.split(SUB_PACKAGE_SCREEN__SUFFIX_RE);
		System.out.println(Arrays.toString(packArr));
//		if (pack.lastIndexOf(SUB_PACKAGE_SCREEN__SUFFIX) != -1) {	
//			String[] packArr = pack.split(SUB_PACKAGE_SCREEN__SUFFIX_RE);
//			System.out.println("packArr="+Arrays.toString(packArr));
//			if (packArr.length > 1) {
//				pack = packArr[0];
//				for (int i = 0; i < packArr.length; i++) {
//					packArr[i] = packArr[i].replace(SUB_PACKAGE_SCREEN__SUFFIX.substring(1), "");
//				}
//			} else {
//				pack = pack.replace(SUB_PACKAGE_SCREEN__SUFFIX, "");
//			}
//			System.out.println(pack);
//		}
	}
	
	public static Set<Class<?>> getClasses(String pack) {
		// 是否循环迭代
		boolean recursive = false;
		String[] packArr = {};

		if (pack.lastIndexOf(SUB_PACKAGE_SCREEN__SUFFIX) != -1) {	//是以.*结尾
			packArr = pack.split(SUB_PACKAGE_SCREEN__SUFFIX_RE);
			if (packArr.length > 1) {
				// 需要匹配中间的任意层包
				pack = packArr[0];
				for (int i = 0; i < packArr.length; i++) {
					packArr[i] = packArr[i].replace(SUB_PACKAGE_SCREEN__SUFFIX.substring(1), "");
				}
			} else {
				pack = pack.replace(SUB_PACKAGE_SCREEN__SUFFIX, "");
			}
			recursive = true;
		}

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

					// 以文件的方式扫描整个包下的文件 并添加到集合中
//					findAndAddClassesInPackageByFile(packageName, packArr, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
//					findAndAddClassesInPackageByJarFile(packageName, packArr, url, packageDirName, recursive, classes);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}
}
