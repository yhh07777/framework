package rongding.framework.util.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rongding.framework.util.lang.StringUtil;

/**
 * 该类实现了将一组对象转换为Excel表格，并且可以从Excel表格中读取到一组List对象中
 * 该类利用了BeanUtils框架中的反射完成
 * 使用该类的前提，在相应的实体对象上通过ExcelReources来完成相应的注解
 * @param <T>
 *
 */
public class ExcelUtil<T> {
	private ExcelUtil(){}
	private static ExcelUtil eu = new ExcelUtil();
	public static ExcelUtil getInstance() {
		return eu;
	}
	
	public static String initExportName(HttpServletRequest request,HttpServletResponse response,String exportName) {
		try {
			if(request.getHeader("user-agent").indexOf("MSIE") != -1) {   
				exportName = java.net.URLEncoder.encode(exportName,"utf-8") ; 
			} else {   
				exportName = new String(exportName.getBytes("utf-8"),"iso-8859-1"); 
			}   
			response.setHeader("Content-disposition", "attachment;filename=" + exportName);
			return exportName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void isLowerMethod() {
	}
	
	/**
	 * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到流
	 * @param datas 	模板中的替换的常量数据
	 * @param template 	模板路径
	 * @param os 		输出流
	 * @param objs 		对象列表
	 * @param clz 		对象的类型
	 * @param isClasspath 模板是否在classPath路径下
	 */
	public void exportStreamByTemplate(Map<String,String> datas,String template,OutputStream os,List objs,Class clz,boolean isClasspath) {
		ExcelTemplate et = handlerObj2ExcelByAnnotation(template, objs, clz, isClasspath);
		et.replaceFinalData(datas);
		et.wirteToStream(os);
	}
	/**
	 * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到一个具体的路径中
	 * @param datas 模板中的替换的常量数据
	 * @param template 模板路径
	 * @param os 输出路径
	 * @param objs 对象列表
	 * @param clz 对象的类型
	 * @param isClasspath 模板是否在classPath路径下
	 */
	public void exportFileByTemplate(Map<String,String> datas,String template,String outPath,List objs,Class clz,boolean isClasspath) {
		ExcelTemplate et = handlerObj2ExcelByAnnotation(template, objs, clz, isClasspath);
		et.replaceFinalData(datas);
		et.writeToFile(outPath);
	}
	/**
	 * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到流,基于Properties作为常量数据
	 * @param prop 基于Properties的常量数据模型
	 * @param template 模板路径
	 * @param os 输出流
	 * @param objs 对象列表
	 * @param clz 对象的类型
	 * @param isClasspath 模板是否在classPath路径下
	 */
	public void exportStreamByTemplate(Properties prop,String template,OutputStream os,List objs,Class clz,boolean isClasspath) {
		ExcelTemplate et = handlerObj2ExcelByAnnotation(template, objs, clz, isClasspath);
		et.replaceFinalData(prop);
		et.wirteToStream(os);
	}
	/**
	 * 将对象转换为Excel并且导出，该方法是基于模板的导出，导出到一个具体的路径中,基于Properties作为常量数据
	 * @param prop 基于Properties的常量数据模型
	 * @param template 模板路径
	 * @param os 输出路径
	 * @param objs 对象列表
	 * @param clz 对象的类型
	 * @param isClasspath 模板是否在classPath路径下
	 */
	public void exportFileByTemplate(Properties prop,String template,String outPath,List objs,Class clz) {
		ExcelTemplate et = handlerObj2ExcelByMethod(template, null, null, objs);
		et.replaceFinalData(prop);
		et.writeToFile(outPath);
	}
	
	/**
	 * 通过指定的方法名称集合和模板导出excel
	 * @param datas
	 * @param template
	 * @param outPath
	 * @param objs
	 * @param clz
	 * @param isClasspath
	 */
	public void exportStreamByTemplateAndMethod(Map<String,String> datas,String template, OutputStream os,String[] titleList,String[] methodList, List objs) {
		ExcelTemplate et = handlerObj2ExcelByMethod(template, titleList, methodList, objs);
		et.replaceFinalData(datas);
		et.insertSer();				
		et.wirteToStream(os);
	}
	
	/**
	 * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于路径的导出
	 * @param outPath 导出路径
	 * @param objs 	  对象列表
	 * @param clz 	  对象类型
	 * @param isXssf  是否是2007版本
	 */
	public void exportObj2Excel(String outPath,List objs,Class clz,boolean isXssf) {
		Workbook wb = handleObj2Excel(objs, clz, isXssf);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			wb.write(fos);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if(fos!=null) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于流
	 * @param os 输出流
	 * @param objs 对象列表
	 * @param clz 对象类型
	 * @param isXssf 是否是2007版本
	 */
	public void exportObj2Excel(OutputStream os,List objs,Class clz,boolean isXssf) {
		try {
			Workbook wb = handleObj2Excel(objs, clz, isXssf);
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 从类路径读取相应的Excel文件到对象列表
	 * @param path 类路径下的path
	 * @param clz 对象类型
	 * @param readLine 开始行，注意是标题所在行
	 * @param tailLine 底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<Object> readExcel2ObjsByClasspath(String path,Class clz,int readLine,int tailLine) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(ExcelUtil.class.getResourceAsStream(path));
			return handlerExcel2Objs(wb, clz, readLine,tailLine);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public List<T> readExcel2ObjsByMethod(String path,Class clz,int readLine,int tailLine,String[] fieldList) {
		return readExcel2ObjsByMethod(new File(path), clz, readLine, tailLine, fieldList);
	}
	
	public  List<T> readExcel2ObjsByMethod(File file,Class clz,int readLine,int tailLine,String[] fieldList) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(file);
			return handlerExcel2ObjsByMethod(wb, clz, readLine,tailLine,fieldList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	public  List<T> readExcel2ObjsByMethod(File file,Class clz,String[] fieldList) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(file);
			return handlerExcel2ObjsByMethod(wb,clz,fieldList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 从文件路径读取相应的Excel文件到对象列表
	 * @param path 文件路径下的path
	 * @param clz 对象类型
	 * @param readLine 开始行，注意是标题所在行
	 * @param tailLine 底部有多少行，在读入对象时，会减去这些行
	 */
	public List<Object> readExcel2ObjsByPath(String path,Class clz,int readLine,int tailLine) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(new File(path));
			return handlerExcel2Objs(wb, clz, readLine,tailLine);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public List<Object> readExcel2ObjsByFile(File file,Class<?> clz) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(file);
			return handlerExcel2Objs(wb, clz, 0,0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 从类路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * @param path 路径
	 * @param clz 类型
	 * @return 对象列表
	 */
	public List<Object> readExcel2ObjsByClasspath(String path,Class clz) {
		return this.readExcel2ObjsByClasspath(path, clz, 0,0);
	}
	/**
	 * 从文件路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * @param path 路径
	 * @param clz 类型
	 * @return 对象列表
	 */
	public List<Object> readExcel2ObjsByPath(String path,Class clz) {
		return this.readExcel2ObjsByPath(path, clz,0,0);
	}
	
	private ExcelTemplate handlerObj2ExcelByMethod(String template, String[] titleList,String[] methodList, List objs) {
		ExcelTemplate et = ExcelTemplate.getInstance();
		try {
			et.readTemplate(template);
			
			//创建标题行
			et.createNewRow();		
			for (String title : titleList) {
				et.createCell(title);
				
			}
			//遍历对象集合输出值
			for(Object obj:objs) {
				et.createNewRow();
				for (String method : methodList) {
					et.createCell(BeanUtils.getProperty(obj,method));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return et;
	}
	
	private Workbook handleObj2Excel(List objs,Class clz,boolean isXssf) {
		Workbook wb = null;
		try {
			//如果是2007版本
			if(isXssf) {
				wb = new XSSFWorkbook();
			} else {
				wb = new HSSFWorkbook();
			}
			
			Sheet sheet = wb.createSheet();	//创建sheet对象
			Row r = sheet.createRow(0);		//创建第一行
			List<ExcelHeader> headers = getHeaderListByAnnotation(clz);
			//对标题进行排序
			Collections.sort(headers);
			//写标题
			for(int i=0;i<headers.size();i++) {
				r.createCell(i).setCellValue(headers.get(i).getTitle());
			}
			//写数据
			Object obj = null;
			//遍历所有列表对象
			for(int i=0;i<objs.size();i++) {
				r = sheet.createRow(i+1);
				obj = objs.get(i);
				for(int j=0;j<headers.size();j++) {
					r.createCell(j).setCellValue(BeanUtils.getProperty(obj, getMethodName(headers.get(j))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wb;
	}
	/**
	 * 处理对象转换为Excel
	 * @param template
	 * @param objs		对象集合
	 * @param clz		对象Class类
	 * @param isClasspath 是否通过classpath获取模版
	 */
	private ExcelTemplate handlerObj2ExcelByAnnotation (String template,List objs,Class clz,boolean isClasspath)  {
		ExcelTemplate et = ExcelTemplate.getInstance();
		try {
			et.readTemplate(template);
			
			List<ExcelHeader> headers = getHeaderListByAnnotation(clz);
			//对标题对象集合进行排序:实现了Comparable接口
			Collections.sort(headers);
			
			//创建标题行
			et.createNewRow();				
			for(ExcelHeader eh:headers) {
				et.createCell(eh.getTitle());
			}
			//输出值
			for(Object obj:objs) {
				et.createNewRow();
				for(ExcelHeader eh:headers) {
					et.createCell(BeanUtils.getProperty(obj,getMethodName(eh) ));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return et;
	}
	/**
	 * 根据标题获取相应的字段名称
	 * @param eh
	 */
	private String getMethodName(ExcelHeader eh) {
		//getAge-->Age
		String mn = eh.getMethodName().substring(3);
		//Age->age
		if (!isUpperCase(mn.charAt(1))) {
			mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
		}
		return mn;
	}
	
	private List<Object> handlerExcel2Objs(Workbook wb,Class clz,int readLine,int tailLine) {
		Sheet sheet = wb.getSheetAt(0);
		List<Object> objs = null;
		try {
			Row row = sheet.getRow(readLine);
			objs = new ArrayList<Object>();
			Map<Integer,String> maps = getHeaderMap(row, clz);
			if(maps==null||maps.size()<=0) throw new RuntimeException("要读取的Excel的格式不正确，检查是否设定了合适的行");
			for(int i=readLine+1;i<=sheet.getLastRowNum()-tailLine;i++) {
				row = sheet.getRow(i);
				Object obj = clz.newInstance();
				for(Cell c:row) {
					int ci = c.getColumnIndex();
					String mn = maps.get(ci).substring(3);
//					mn = mn.substring(0,1).toLowerCase()+mn.substring(1);
					
					String value=this.getCellValue(c);
					Object fieldValue=convertValue(clz, mn, value);
					BeanUtils.copyProperty(obj,mn,fieldValue);
				}
				objs.add(obj);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return objs;
	}
	
	private Object convertValue(Class clazz,String filedName,String value){
		Class c =FieldUtils.getField(clazz, filedName,true).getType();
		if (c==String.class) {
			return value;
		}else if (c==Integer.class || c==int.class) {
			BigDecimal bigDecimal=new BigDecimal(value);
			return bigDecimal.intValue();
		}else if (c==Double.class || c==double.class) {
			BigDecimal bigDecimal=new BigDecimal(value);
			return bigDecimal.doubleValue();
		}else if (c==Long.class || c==long.class) {
			BigDecimal bigDecimal=new BigDecimal(value);
			return bigDecimal.longValue();
		}else {
			throw new RuntimeException("未知类型:"+c);
		}
	}
	
	private boolean checkRowEmpty(Row row,int len) {
		for (int i = 0; i < len; i++) {
			Cell cell=row.getCell(i);
			if (cell!=null) {
				return false;
			}
		}
		return true;
	}
	
	private <T>List<T> handlerExcel2ObjsByMethod(Workbook wb,Class clz,String[] fieldList) {
		Sheet sheet = wb.getSheetAt(0);
		List<T> objs = null;
		try {
			Row row =null;
			objs = new ArrayList<T>();
			int lastRowNum=sheet.getLastRowNum();
			int index=0;
			for (index = lastRowNum; index>0 ; index--) {
				row=sheet.getRow(index);
				Cell lastCell=row.getCell(fieldList.length);
				if(lastCell!=null){
					String lastCellValue=this.getCellValue(lastCell);
					if (!StringUtil.isBlank(lastCellValue)&&StringUtils.startsWith(lastCellValue, "#")&&checkRowEmpty(row,fieldList.length)) {
						continue;
					}else {
						break;
					}
				}else {
					if (checkRowEmpty(row,fieldList.length)) {
						continue;
					}
					break;
				}
			}
			lastRowNum=index;
			for(int i=1;i<=lastRowNum;i++) {
				row = sheet.getRow(i);
				T obj = (T) clz.newInstance();
				for (int j = 0; j < fieldList.length; j++) {
					Cell c=row.getCell(j);
					int ci = c.getColumnIndex();
					String mn = fieldList[ci];
					Object fieldValue=convertValue(clz, mn, this.getCellValue(c));
					BeanUtils.copyProperty(obj,mn,fieldValue);
				}
				objs.add(obj);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return objs;
	}
	
	private <T>List<T> handlerExcel2ObjsByMethod(Workbook wb,Class clz,int readLine,int tailLine,String[] fieldList) {
		Sheet sheet = wb.getSheetAt(0);
		List<T> objs = null;
		try {
			Row row = sheet.getRow(readLine);
			objs = new ArrayList<T>();
			for(int i=readLine+1;i<=sheet.getLastRowNum()-tailLine;i++) {
				row = sheet.getRow(i);
				T obj = (T) clz.newInstance();
				for (int j = 0; j < fieldList.length; j++) {
					Cell c=row.getCell(j);
					int ci = c.getColumnIndex();
					String mn = fieldList[ci];
					Object fieldValue=convertValue(clz, mn, this.getCellValue(c));
					BeanUtils.copyProperty(obj,mn,fieldValue);
				}
				objs.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return objs;
	}
	
	private List<ExcelHeader> getHeaderListByAnnotation(Class clz) {
		//创建集合用于存放标题对象
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
		//获取所有声明的方法
		Method[] ms = clz.getDeclaredMethods();
		//遍历所有方法
		for(Method m:ms) {
			//获取方法名称
			String mn = m.getName();
			//如果是get方法
			if(mn.startsWith("get")) {
				//如果存在ExcelResources这个注解
				if(m.isAnnotationPresent(ExcelResources.class)) {
					ExcelResources er = m.getAnnotation(ExcelResources.class);
					headers.add(new ExcelHeader(er.title(),er.order(),mn));
				}
			}
		}
		return headers;
	}
	
	/**
	 * 获取标题的map
	 * @param titleRow 指定的行 
	 * @param clz
	 */
	private Map<Integer,String> getHeaderMap(Row titleRow,Class clz) {
		List<ExcelHeader> headers = getHeaderListByAnnotation(clz);	//获取标题行对象集合
		Map<Integer,String> maps = new HashMap<Integer, String>();	//key:第几列,value:相应的setter
		
		//遍历标题行所有单元格
		for(Cell c:titleRow) {
			String title = c.getStringCellValue();
			for(ExcelHeader eh:headers) {
				if(eh.getTitle().equals(title.trim())) {
					maps.put(c.getColumnIndex(), eh.getMethodName().replace("get","set"));
					break;
				}
			}
		}
		return maps;
	}
	
	private boolean isUpperCase(char c){
		if (c>='A' && c<='Z') {
			return true;
		}
		return false;
	}
	
	private String getCellValue(Cell c) {
		String o = null;
		switch (c.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			o = ""; break;
		case Cell.CELL_TYPE_BOOLEAN:
			o = String.valueOf(c.getBooleanCellValue()); break;
		case Cell.CELL_TYPE_FORMULA:
			o = String.valueOf(c.getCellFormula()); break;
		case Cell.CELL_TYPE_NUMERIC:
			 String value=null;
			 if (HSSFDateUtil.isCellDateFormatted(c)) {
			      Date date = c.getDateCellValue();
			      if (date != null) {
			          value = new SimpleDateFormat("yyyy-MM-dd").format(date);
			      } else {
			          value = "";
			      }
			  } else {
			      value = new DecimalFormat("0").format(c.getNumericCellValue());
			  }
			o = String.valueOf(value); break;
		case Cell.CELL_TYPE_STRING:
			o = c.getStringCellValue(); break;
		default:
			o = null;
			break;
		}
		return o;
	}
}
