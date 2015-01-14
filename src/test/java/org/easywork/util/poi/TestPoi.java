package org.easywork.util.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easywork.util.poi.model.Student;
import org.easywork.util.poi.model.User;
import org.junit.Test;

import rongding.framework.util.poi.ExcelTemplate;
import rongding.framework.util.poi.ExcelUtil;
import rongding.framework.util.time.DateUtil;

public class TestPoi {
	private static final String IMPORT_PATH="D:/Workspace_sts3.5/framework/src/test/java/org/easywork/util/poi/excel";
	private static final String EXPORT_PATH=IMPORT_PATH+"/export";
	
	@Test
	public void testExport01() {
		ExcelTemplate et = ExcelTemplate.getInstance().readTemplateByPath(IMPORT_PATH+"/user-template.xls");
		et.createNewRow();
		et.createCell("1111111");
		et.createCell("aaaaaaaaaaaa");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("222222");
		et.createCell("bbbbb");
		et.createCell("b");
		et.createCell("dbbb");
		et.createNewRow();
		et.createCell("3333333");
		et.createCell("cccccc");
		et.createCell("a1");
		et.createCell(12333);
		et.createNewRow();
		et.createCell("4444444");
		et.createCell("ddddd");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell(112);
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		et.createNewRow();
		et.createCell("555555");
		et.createCell("eeeeee");
		et.createCell("a1");
		et.createCell("a2a2");
		Map<String,String> datas = new HashMap<String,String>();
		datas.put("title","测试用户信息");
		datas.put("date","2012-06-02 12:33");
		datas.put("dep","昭通师专财务处");
		
		et.replaceFinalData(datas);	//替换常量
		et.insertSer();				//插入序号
		et.writeToFile(EXPORT_PATH+"/testExport01.xls");
	}
	
	@Test
	public void testObj2Xls01() {
		List<User> users = new ArrayList<User>();
		users.add(new User(1,"aaa","水水水",11));
		users.add(new User(2,"sdf","水水水",11));
		users.add(new User(3,"sdfde","水水水",11));
		users.add(new User(4,"aaa","水水水",11));
		users.add(new User(54,"aaa","水水水",11));
		users.add(new User(16,"aaa","水水水",11));
		ExcelUtil.getInstance().exportFileByTemplate(new HashMap<String,String>(),IMPORT_PATH+"/user-template.xls",EXPORT_PATH+"/testObj2Xls01.xls", users, User.class, true);
	}
	
	@Test
	public void testObj2Xls2() {
		List<Student> stus = new ArrayList<Student>();
		stus.add(new Student(1,"张三","1123123", "男"));
		stus.add(new Student(2,"张三","1123123", "男"));
		stus.add(new Student(3,"张三","1123123", "男"));
		stus.add(new Student(4,"张三","1123123", "男"));
		ExcelUtil.getInstance().exportObj2Excel(EXPORT_PATH+"/testObj2Xls2.xls",stus, Student.class, false);
	}
	
	@Test
	public void testExportStreamByTemplateAndMethod() throws Exception {
		List<Student> stus = new ArrayList<Student>();
		stus.add(new Student(1,"张三","1123123", "男"));
		stus.add(new Student(2,"张三","1123123", "男"));
		stus.add(new Student(3,"张三","1123123", "男"));
		stus.add(new Student(4,"张三","1123123", "男"));
		int i=0;
		for (Student s : stus) {
			s.setA1("xxxxxxxxxxxxxxxxxxx1"+i++);
			s.setA2("xxxxxxxxxxxxxxxxxxx2"+i++);
			s.setA3("xxxxxxxxxxxxxxxxxxx3"+i++);
			s.setDate(DateUtil.getDatetime());
		}
		
		OutputStream os=new FileOutputStream(EXPORT_PATH+"/testExportStreamByTemplateAndMethod.xls");
		String[] titleList=new String[]{"学生标识","学生序号","学生性别","学生姓名","a1","a2","a3","date"};
		String[] methodList=new String[]{"id","no","sex","name","a1","a2","a3","date"};
		Map<String, String> datas=new HashMap<String, String>();
		datas.put("title", "学生导出列表");
		datas.put("date", DateUtil.getDatetime());
		ExcelUtil.getInstance().exportStreamByTemplateAndMethod(datas, IMPORT_PATH+"/stu-template.xls", os, titleList, methodList, stus);
	}
	
	@Test
	public void testReadByFile() {
		File file=new File(EXPORT_PATH+"/testReadByFile.xls");
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByFile(file,Student.class);
		for(Object obj:stus) {
			Student stu = (Student)obj;
			System.out.println(stu);
		}
	}
	
	@Test
	public void testReadByMethod() {
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByMethod(EXPORT_PATH+"/testReadByMethod.xls",Student.class,0,0,new String[]{"id","no","name","sex"});
		for(Object obj:stus) {
			Student stu = (Student)obj;
			System.out.println(stu);
		}
	}
	
	@Test
	public void testReadByMethod1() {
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByMethod(new File(EXPORT_PATH+"/testReadStuByMethod.xls"),Student.class,new String[]{"id","no","name","sex"});
		for(Object obj:stus) {
			Student stu = (Student)obj;
			System.out.println(stu);
		}
	}
	
	@Test
	public void testReadExcel2ObjsByPath() {
		List<Object> stus = ExcelUtil.getInstance().readExcel2ObjsByPath(EXPORT_PATH+"d:/test/poi/tus.xls",User.class,1,2);
		for(Object obj:stus) {
			User stu = (User)obj;
			System.out.println(stu);
		}
	}
	
}
