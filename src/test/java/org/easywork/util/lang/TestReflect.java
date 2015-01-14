package org.easywork.util.lang;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.easywork.util.poi.model.Student;
import org.easywork.util.poi.model.User;
import org.junit.Test;

import rongding.framework.util.poi.ExcelUtil;

public class TestReflect {
	private static final String PATH="D:/Workspace_sts3.5/framework/src/test/java/org/easywork/util/poi/excel/";
	
	@Test
	public void testBeanutil() {
		try {
			Class us = User.class;
			Object obj = us.newInstance();
			BeanUtils.copyProperty(obj, "username","张三");
			String str = BeanUtils.getProperty(obj,"username");
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testBeanutil1() {
		try {
			String method = "setUsername";
			Class us = User.class;
			Object obj = us.newInstance();
			Method m = us.getDeclaredMethod(method,String.class);
			m.invoke(obj, "张三");
			Method m2 = us.getDeclaredMethod("getUsername");
			System.out.println(m2.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	//根据类的方法利用反射调用
	@Test
	public void testExecuteMethodByReflect() {
		try {
			String method = "setUsername";
			Class us = User.class;
			Object obj = us.newInstance();
			Method m = us.getDeclaredMethod(method,String.class);
			m.invoke(obj, "张三");
			Method m2 = us.getDeclaredMethod("getUsername");
			System.out.println(m2.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRead01() {
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByPath("D:/zxf/temp/ss1.xls",Student.class);
		for(Object obj:stus) {
			Student stu = (Student)obj;
			System.out.println(stu);
		}
	}
	
	@Test
	public void testRead02() {
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByPath("D:/zxf/temp/tus.xls",User.class,1,2);
		for(Object obj:stus) {
			User stu = (User)obj;
			System.out.println(stu);
		}
	}
}
