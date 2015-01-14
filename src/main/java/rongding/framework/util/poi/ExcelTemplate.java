package rongding.framework.util.poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 该类实现了基于模板的导出
 * 如果要导出序号，需要在excel中定义一个标识为sernums
 * 如果要替换信息，需要传入一个Map，这个map中存储着要替换信息的值，在excel中通过#来开头
 * 要从哪一行那一列开始替换需要定义一个标识为datas
 * 如果要设定相应的样式，可以在该行使用styles完成设定，此时所有此行都使用该样式
 * 如果使用defaultStyls作为表示，表示默认样式，如果没有defaultStyles使用datas行作为默认样式
 */
public class ExcelTemplate {
	/** 数据行标识 :代表从这一行开始填充数据*/
	public final static String DATA_LINE = "datas";
	/** 默认样式标识 */
	public final static String DEFAULT_STYLE = "defaultStyles";
	/** 行样式标识:表示该列需要使用指定的样式 */
	public final static String STYLE = "styles";
	/** 插入序号样式标识 */
	public final static String SER_NUM = "sernums";
	private static ExcelTemplate et = new ExcelTemplate();
	private Workbook wb;
	private Sheet sheet;
	/** 数据的初始化列数 */
	private int initColIndex;
	/** 数据的初始化行数 */
	private int initRowIndex;
	/** 当前列数 */
	private int curColIndex;
	/** 当前行数 */
	private int curRowIndex;
	/** 当前行对象 */
	private Row curRow;
	/** 最后一行的数据 */
	private int lastRowIndex;
	/** 默认样式 */
	private CellStyle defaultStyle;
	/** 默认行高:不包括在样式中 */
	private float rowHeight;
	/** 存储某一方所对于的样式:<列号,样式> */
	private Map<Integer,CellStyle> styles;
	/** 序号的列 */
	private int serColIndex;
	private Properties props;
	
	public static ExcelTemplate getInstance() {
		if (et.props==null) {
			et.props=new Properties();
			et.props.put("ignoreColumnWidthArray", new Integer[]{0});
			et.props.put("defaultColumnWidth", 4096);
		}
		return et;
	}
	
	public static ExcelTemplate getInstance(Properties props) {
		if (props!=null) {
			et.props=props;
		}
		return et;
	}
	
	public ExcelTemplate readTemplate(String path) {
		try {
			wb = WorkbookFactory.create(new File(path));
			initTemplate();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板格式有错，！请检查",e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板不存在！请检查",e);
		}
		return this;
	}
	
	public ExcelTemplate readTemplateByClasspath(String path) {
		try {
			wb = WorkbookFactory.create(ExcelTemplate.class.getResourceAsStream(path));
			initTemplate();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板格式有错，！请检查");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板不存在！请检查");
		}
		return this;
	}
	/**
	 * 从某个路径来读取模板
	 * @param path
	 */
	public ExcelTemplate readTemplateByPath(String path) {
		try {
			wb = WorkbookFactory.create(new File(path));
			initTemplate();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板格式有错，！请检查");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取模板不存在！请检查");
		}
		return this;
	}
	
	/**
	 * 将文件写到相应的路径下
	 * @param filepath
	 */
	public void writeToFile(String filepath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("写入的文件不存在",e);
		} catch (IOException e) {
			throw new RuntimeException("写入数据失败:"+e);
		} finally {
			try {
				if(fos!=null) fos.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭输出流失败:"+e);
			}
		}
	}
	/**
	 * 将文件写到某个输出流中
	 * @param os
	 */
	public void wirteToStream(OutputStream os) {
		try {
			wb.write(os);
		} catch (IOException e) {
			throw new RuntimeException("写入流失败:"+e);
		}
	}
	
	/**
	 * 创建相应的元素，基于String类型
	 */
	public void createCell(String value) {
		Cell c = curRow.createCell(curColIndex);	//创建一个单元格
		setCellStyle(c);							//设置样式							
		c.setCellValue(value);  					//设置单元格的值
		curColIndex++;								//当前列加1
	}
	public void createCell(int value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue((int)value);
		curColIndex++;
	}
	public void createCell(Date value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}
	public void createCell(double value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}
	public void createCell(boolean value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}
	
	public void createCell(Calendar value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}
	/**
	 * 创建新行，在使用时只要添加完一行，需要调用该方法创建
	 */
	public void createNewRow() {
		//如果最后一行大于当前行 && 当前行不等于初始行(第一行不需要移动)
		if(lastRowIndex>curRowIndex&&curRowIndex!=initRowIndex) {
			//先往下移动一行再创建新行
			sheet.shiftRows(curRowIndex, lastRowIndex, 1,true,true);
			lastRowIndex++;
		}
		curRow = sheet.createRow(curRowIndex);
		curRow.setHeightInPoints(rowHeight);
		curRowIndex++;
		curColIndex = initColIndex;
	}
	
	/**
	 * 插入序号，会自动找相应的序号标示的位置完成插入
	 */
	public void insertSer() {
		int index = 1;
		Row row = null;
		Cell c = null;
		//遍历从初始行到当前行
		for(int i=initRowIndex;i<curRowIndex;i++) {
			//获取指定行对象
			row = sheet.getRow(i);
			//创建单元格对象
			c = row.createCell(serColIndex);
			//设置单元格样式
			setCellStyle(c);
			//设置单元格的值
			c.setCellValue(index++);
		}
	}
	/**
	 * 根据map替换相应的常量，通过Map中的值来替换#开头的值
	 * @param datas
	 */
	public void replaceFinalData(Map<String,String> datas) {
		//如果没有设置常量直接返回
		if(datas==null) return;
		for(Row row:sheet) {
			for(Cell c:row) {
				if(c.getCellType()!=Cell.CELL_TYPE_STRING) continue;
				//获取该单元格的值
				String str = c.getStringCellValue().trim();
				//如果以#开头表示是常量
				if(str.startsWith("#")) {
					if(datas.containsKey(str.substring(1))) {
						c.setCellValue(datas.get(str.substring(1)));
					}
				}
			}
		}
	}
	/**
	 * 基于Properties的替换，依然也是替换#开始的
	 * @param prop
	 */
	public void replaceFinalData(Properties prop) {
		if(prop==null) return;
		for(Row row:sheet) {
			for(Cell c:row) {
				if(c.getCellType()!=Cell.CELL_TYPE_STRING) continue;
				String str = c.getStringCellValue().trim();
				if(str.startsWith("#")) {
					if(prop.containsKey(str.substring(1))) {
						c.setCellValue(prop.getProperty(str.substring(1)));
					}
				}
			}
		}
	}
	
	private ExcelTemplate(){
	}
	/**
	 * 设置某个元素的样式
	 */
	private void setCellStyle(Cell c) {
		if(styles==null) return;						//如果样式为空直接返回
		if(styles.containsKey(curColIndex)) {
			c.setCellStyle(styles.get(curColIndex));
		} else {
			c.setCellStyle(defaultStyle);
		}
	}
	/**
	 * 初始化模板
	 */
	private void initTemplate() {
		sheet = wb.getSheetAt(0);				//创建第一个sheet
		initConfigData();						//初始化模板配置信息
		lastRowIndex = sheet.getLastRowNum();	//获取最后一行
		curRow = sheet.createRow(curRowIndex);	//根据当前行索引创建当前行对象
	}
	/**
	 * 初始化数据信息
	 */
	private void initConfigData() {
		boolean findData = false;
		boolean findSer = false;
		for(Row row:sheet) {
			if(findData) break;
			for(Cell c:row) {
				if(c.getCellType()!=Cell.CELL_TYPE_STRING) continue;
				String str = c.getStringCellValue().trim();
				//如果找到序号标识
				if(str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex();	//设置序号列
					findSer = true;						//设置序号标识为true
				}
				//如果找到数据标识
				if(str.equals(DATA_LINE)) {
					initColIndex = c.getColumnIndex();	//获取初始化列
					initRowIndex = row.getRowNum();		//获取初始化行	
					curColIndex = initColIndex;			//设置当前列为初始化列
					curRowIndex = initRowIndex;			//设置当前行为初始化行
					findData = true;					//表示找到数据标识
					defaultStyle = c.getCellStyle();	//获取默认样式
					rowHeight = row.getHeightInPoints();
					
					initStyles();
					
					initColumnWidth();
//					sheet.autoSizeColumn((short)0);
					break;
				}
			}
		}
		//如果未找到序号列标识则执行序号列初始化方法
		if(!findSer) {
			initSer();
		}
	}
	/**
	 * 初始化序号位置
	 */
	private void initSer() {
		for(Row row:sheet) {
			for(Cell c:row) {
				if(c.getCellType()!=Cell.CELL_TYPE_STRING) continue;
				String str = c.getStringCellValue().trim();
				if(str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex();
				}
			}
		}
	}
	/**
	 * 初始化样式信息
	 */
	private void initStyles() {
		styles = new HashMap<Integer, CellStyle>();
		for(Row row:sheet) {
			for(Cell c:row) {
				if(c.getCellType()!=Cell.CELL_TYPE_STRING) continue;
				String str = c.getStringCellValue().trim();
				//如果是默认样式
				if(str.equals(DEFAULT_STYLE)) {
					defaultStyle = c.getCellStyle();		//就让默认样式等于当前单元格样式
				}
				if(str.equals(STYLE)) {
					styles.put(c.getColumnIndex(), c.getCellStyle());
				}
			}
		}
	}
	
	private void initColumnWidth() {
		int defaultColumnWidth=Integer.parseInt(props.getProperty("defaultColumnWidth", "17"));
		Integer[] ignoreColumnWidthArray=(Integer[]) props.get("ignoreColumnWidthArray");
		Map<Integer, Integer> columnMap=new HashMap<Integer, Integer>();
		for (int i = 0; i < ignoreColumnWidthArray.length; i++) {
			columnMap.put(i, sheet.getColumnWidth(i));
		}
		sheet.setDefaultColumnWidth(defaultColumnWidth);
		for (Integer key : columnMap.keySet()) {
			sheet.setColumnWidth(key, columnMap.get(key));
		}
	}

}
