package rongding.framework.util.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

public class WriteExcel {
	private static Logger logger = Logger.getLogger(WriteExcel.class);

	public static void excelout(String filename, String sheetName, String datetime, String titlename, String[] methodName, String[] cols, int[] width, List list, int r1, int r2, int r3, Map currsum, Map allsum, HttpServletResponse response) {
		try {
			response.setContentType("application/msexcel; charset=gb2312");
			filename = new String(filename.getBytes(), "ISO8859_1");
			response.setHeader("Content-disposition", "inline; filename=" + filename);
			OutputStream os = response.getOutputStream();
			String worksheet = sheetName;// 输出的excel文件工作表名;
			WritableWorkbook workbook;

			// 创建可写入的Excel工作薄
			System.out.println("begin");

			workbook = Workbook.createWorkbook(os);

			WritableSheet sheet = workbook.createSheet(worksheet, 0); // 添加第一个工作表
			/*******************************************************************
			 * 创建单元格类型; *
			 * *****************************************************************************
			 */
			// 此字体为绿色加粗,字体大小为9磅;
			WritableFont wfc = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
			// 此字体为黑色加粗,字体大小为9磅;
			WritableFont wfc1 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			// 此字体为黑色加粗,字体大小为11磅;
			WritableFont wfc2 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

			// 创建单元格类实例;
			WritableCellFormat wtcf;

			Label label;
			// 添加标题;
			sheet.mergeCells(0, 0, cols.length, 0);
			wtcf = new WritableCellFormat(wfc2);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wtcf.setAlignment(Alignment.CENTRE);
			label = new Label(0, 0, titlename, wtcf);
			sheet.addCell(label);

			/*
			 * 打印日期 添加带有formatting的DateFormat对象
			 */
			sheet.mergeCells(0, 1, cols.length, 1);
			wtcf = new WritableCellFormat(wfc);
			wtcf.setAlignment(Alignment.RIGHT);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			label = new Label(0, 1, datetime, wtcf);
			sheet.addCell(label);
			// 设置单元格式1;
			WritableCellFormat wcs = new WritableCellFormat();
			wcs.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs.setAlignment(Alignment.CENTRE);
			// 设置单元格式2;
			WritableCellFormat wcs1 = new WritableCellFormat(wfc1);
			wcs1.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs1.setAlignment(Alignment.CENTRE);
			// 添加字段名;
			label = new Label(0, 2, "序 号", wcs1); // put the title in row1
			sheet.addCell(label);
			for (int i = 1; i < cols.length + 1; i++) {
				// Label(列号,行号 ,内容 )
				label = new Label(i, 2, cols[i - 1], wcs1); // put the title in
				// row1
				sheet.addCell(label);
			}
			// 设置列宽
			for (int i = 0; i < width.length; i++) {
				sheet.setColumnView(i + 1, width[i]);
			}
			// 添加数据集;
			if (list != null && list.size() > 0) {
				int rows = 2;
				for (int j = 0; j < list.size(); j++) {
					Object obj = list.get(j);
					Class cla = obj.getClass();
					Method methods[] = cla.getDeclaredMethods();// 获取该对象的所有方法名;
					rows++;
					label = new Label(0, rows, String.valueOf(j + 1), wcs);
					sheet.addCell(label);
					for (int k = 1; k < cols.length + 1; k++) {					// 遍历所有的列
						boolean flag = false;
						for (int m = 0; m < methods.length; m++) {			// 遍历所有的方法
							if (methods[m].getName().equals("get" + methodName[k - 1])) {
								label = new Label(k, rows, (String.valueOf(methods[m].invoke(obj, null))), wcs);
								sheet.addCell(label);
								flag = true;
								break;
							}
						}
						if (!flag)
							throw new RuntimeException("没有找到对应方法" + methodName[k - 1]);
					}
				}
			}

			// 当前总计
			if (currsum != null) {
				sheet.mergeCells(0, list.size() + 3, r1, list.size() + 3);
				wtcf = new WritableCellFormat(wfc2);
				wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
				wtcf.setAlignment(Alignment.LEFT);
				label = new Label(0, list.size() + 3, "当前总计：", wtcf);
				sheet.addCell(label);
				if (r2 <= r3 && r3 <= cols.length) {
					for (int q = r2; q <= r3; q++) {
						if (currsum.get(String.valueOf(q)) != null) {
							label = new Label(q, list.size() + 3, (String) currsum.get(String.valueOf(q)), wcs);
							sheet.addCell(label);
							// System.out.println(q+":"+(String)currsum.get(String.valueOf(q)));
						} else {
							label = new Label(q, list.size() + 3, "", wcs);
							sheet.addCell(label);
							// System.out.println(q+":"+(String)currsum.get(String.valueOf(q)));
						}
					}
				}
			}
			// 截止累计
			if (allsum != null) {
				sheet.mergeCells(0, list.size() + 4, r1, list.size() + 4);
				wtcf = new WritableCellFormat(wfc2);
				wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
				wtcf.setAlignment(Alignment.LEFT);
				label = new Label(0, list.size() + 4, "截止累计：", wtcf);
				sheet.addCell(label);
				if (r2 <= r3 && r3 < cols.length) {
					for (int q = r2; q <= r3; q++) {
						if (allsum.get(String.valueOf(q)) != null) {
							label = new Label(q, list.size() + 4, (String) allsum.get(String.valueOf(q)), wcs);
							sheet.addCell(label);
						} else {
							label = new Label(q, list.size() + 4, "", wcs);
							sheet.addCell(label);
						}
					}
				}
			}
			workbook.write();
			os.flush();
			workbook.close();
			// os.write("成功导出!".getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	/***************************************************************************
	 * 参数说明: filename:存储文件名; sheetName:工作表名称 datetime:开始日期与结束日期;
	 * titlename:文件标题名; path:文件保存路径 methodName:BEAN类的方法名; cols:列名; width:列宽
	 * list:数据集,从数据库中获取并存储在BEAN中; r1:表示第一列有几列合并; r2:第1个需要进行统计的列的索引（从1开始）
	 * r3:最后1个需要进行统计的列的索引（从1开始）; currsum:存放当前统计结果的Map类型数据,key为索引值
	 * allsum:存放累计统计结果的Map类型数据，key为索引值
	 * ************************************************************************
	 */
	public static boolean excelFile(String fileName, String sheetName, String datetime, String titlename, String path, String[] methodName, String[] cols, int[] width, List list, int r1, int r2, int r3, Map currsum, Map allsum) {
		OutputStream os = null;
		WritableWorkbook workbook = null;
		boolean result = false;
		try {
			logger.info("创建文件");
			System.out.println("创建文件");
			String savePath = path + "/" + fileName;
			os = new FileOutputStream(savePath);
			workbook = Workbook.createWorkbook(os);
			String worksheet = sheetName;// 输出的excel文件工作表名;
			WritableSheet sheet = workbook.createSheet(worksheet, 0); // 添加第一个工作表
			/*******************************************************************
			 * 创建单元格类型; *
			 * *****************************************************************************
			 */
			// 此字体为绿色加粗,字体大小为9磅;
			WritableFont wfc = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
			// 此字体为黑色加粗,字体大小为9磅;
			WritableFont wfc1 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			// 此字体为黑色加粗,字体大小为11磅;
			WritableFont wfc2 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

			// 创建单元格类实例;
			WritableCellFormat wtcf;

			Label label;
			// 添加标题;
			sheet.mergeCells(0, 0, cols.length, 0);
			wtcf = new WritableCellFormat(wfc2);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wtcf.setAlignment(Alignment.CENTRE);
			label = new Label(0, 0, titlename, wtcf);
			sheet.addCell(label);

			/*
			 * 打印日期 添加带有formatting的DateFormat对象
			 */
			sheet.mergeCells(0, 1, cols.length, 1);
			wtcf = new WritableCellFormat(wfc);
			wtcf.setAlignment(Alignment.RIGHT);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			label = new Label(0, 1, datetime, wtcf);
			sheet.addCell(label);
			// 设置单元格式1;
			WritableCellFormat wcs = new WritableCellFormat();
			wcs.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs.setAlignment(Alignment.CENTRE);
			// 设置单元格式2;
			WritableCellFormat wcs1 = new WritableCellFormat(wfc1);
			wcs1.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs1.setAlignment(Alignment.CENTRE);
			// 添加字段名;
			label = new Label(0, 2, "序 号", wcs1); // put the title in row1
			sheet.addCell(label);
			for (int i = 1; i < cols.length + 1; i++) {
				// Label(列号,行号 ,内容 )
				label = new Label(i, 2, cols[i - 1], wcs1); // put the title in
				// row1
				sheet.addCell(label);
			}
			// 设置列宽
			for (int i = 0; i < width.length; i++) {
				sheet.setColumnView(i + 1, width[i]);
			}
			System.out.println("写数据");
			// 添加数据集;
			if (list != null && list.size() > 0) {
				int rows = 2;
				for (int j = 0; j < list.size(); j++) {
					Object obj = list.get(j);
					Class cla = obj.getClass();
					Method methods[] = cla.getDeclaredMethods();// 获取该对象的所有方法名;
					rows++;
					label = new Label(0, rows, String.valueOf(j + 1), wcs);
					sheet.addCell(label);
					for (int k = 1; k < cols.length + 1; k++) {
						for (int m = 0; m < methods.length; m++) {
							int n = m;
							boolean flag = false;
							while (true) {
								if ((k - 1) == 1) {
									if (methods[n].getName().equals("get" + methodName[k - 1])) {
										label = new Label(k, rows, (String.valueOf(methods[n].invoke(obj, null))), wcs);
										sheet.addCell(label);
										flag = true;
										break;
									}
									if (n == (methods.length - 1))
										n = 0;
									else
										n++;
								} else {
									if (methods[n].getName().equals("get" + methodName[k - 1])) {
										label = new Label(k, rows, (String.valueOf(methods[n].invoke(obj, null))), wcs);
										sheet.addCell(label);
										flag = true;
										break;
									}
									if (n == (methods.length - 1))
										n = 0;
									else
										n++;
								}
							}
							if (flag)
								break;
						}
					}
				}
			}
			/*
			 * 当前总计;
			 */
			if (currsum != null) {
				sheet.mergeCells(0, list.size() + 3, r1, list.size() + 3);
				wtcf = new WritableCellFormat(wfc2);
				wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
				wtcf.setAlignment(Alignment.LEFT);
				label = new Label(0, list.size() + 3, "当前总计：", wtcf);
				sheet.addCell(label);
				if (r2 <= r3 && r3 <= cols.length) {
					for (int q = r2; q <= r3; q++) {
						if (currsum.get(String.valueOf(q)) != null) {
							label = new Label(q, list.size() + 3, (String) currsum.get(String.valueOf(q)), wcs);
							sheet.addCell(label);
						} else {
							label = new Label(q, list.size() + 3, "", wcs);
							sheet.addCell(label);
						}
					}
				}
			}
			/*
			 * 截止累计;
			 */
			if (allsum != null) {
				sheet.mergeCells(0, list.size() + 4, r1, list.size() + 4);
				wtcf = new WritableCellFormat(wfc2);
				wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
				wtcf.setAlignment(Alignment.LEFT);
				label = new Label(0, list.size() + 4, "截止累计：", wtcf);
				sheet.addCell(label);
				if (r2 <= r3 && r3 < cols.length) {
					for (int q = r2; q <= r3; q++) {
						if (allsum.get(String.valueOf(q)) != null) {
							label = new Label(q, list.size() + 4, (String) allsum.get(String.valueOf(q)), wcs);
							sheet.addCell(label);
						} else {
							label = new Label(q, list.size() + 4, "", wcs);
							sheet.addCell(label);
						}
					}
				}
			}
			result = true;
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.write();
					workbook.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		System.out.println("end");
		return result;
	}

	/***************************************************************************
	 * 参数说明: filename:存储文件名; sheetName:工作表名称 datetime:开始日期与结束日期;
	 * titlename:文件标题名; cols:固定列列名 colskey:固定列值存放在Map中的key width:列宽
	 * groupkey:动态列BEAN类在map中的key groupname:组名 automethodName:动态列的BEAN类的方法名;
	 * autocols:动态列名; autowidth:动态列列宽 list:数据集,类型为Map<String,Object>;
	 * rowskey:存放行统计的结果的key值 Object:空值对象
	 * ************************************************************************
	 */
	public static void excelAutoCols(String filename, String sheetName, String datetime, String titlename, String[] cols, String[] colskey, int[] width, String[] groupkey, String[] groupname, String[] automethodName, String[] autocols, int[] autowidth, List<Map<String, Object>> list, String[] rowskey, Object object, HttpServletResponse response) {
		try {
			response.setContentType("application/msexcel; charset=gb2312");
			filename = new String(filename.getBytes(), "ISO8859_1");
			// response.setHeader("Content-disposition", "attachment; filename="
			// + filename);
			response.setHeader("Content-disposition", "inline; filename=" + filename);
			OutputStream os = response.getOutputStream();
			String worksheet = sheetName;// 输出的excel文件工作表名;
			WritableWorkbook workbook;

			// 创建可写入的Excel工作薄
			System.out.println("begin");

			workbook = Workbook.createWorkbook(os);

			WritableSheet sheet = workbook.createSheet(worksheet, 0); // 添加第一个工作表
			/*******************************************************************
			 * 创建单元格类型; *
			 * *****************************************************************************
			 */
			// 此字体为绿色加粗,字体大小为9磅;
			WritableFont wfc = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
			// 此字体为黑色加粗,字体大小为9磅;
			WritableFont wfc1 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			// 此字体为黑色加粗,字体大小为11磅;
			WritableFont wfc2 = new WritableFont(WritableFont.COURIER, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

			// 创建单元格类实例;
			WritableCellFormat wtcf;

			Label label;

			// 设置单元格式1;
			WritableCellFormat wcs = new WritableCellFormat();
			wcs.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs.setAlignment(Alignment.CENTRE);
			// 设置单元格式2;
			WritableCellFormat wcs1 = new WritableCellFormat(wfc1);
			wcs1.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcs1.setAlignment(Alignment.CENTRE);
			// 添加字段名;
			label = new Label(0, 3, "序 号", wcs1); // put the title in row1
			sheet.addCell(label);
			for (int i = 1; i < cols.length + 1; i++) {
				// Label(列号,行号 ,内容 )
				label = new Label(i, 3, cols[i - 1], wcs1); // put the title in
				// row1
				sheet.addCell(label);
			}
			// 设置列宽
			for (int i = 0; i < width.length; i++) {
				sheet.setColumnView(i + 1, width[i]);
			}
			// 动态列的小组名设置
			int autoi = cols.length + 1;
			int colsnum = autocols.length;
			for (int i = 0; i < groupname.length; i++) {
				sheet.mergeCells(autoi, 2, autoi + colsnum - 1, 2);
				label = new Label(autoi, 2, groupname[i], wcs1);
				sheet.addCell(label);
				// 动态小组列设置
				for (int j = 0; j < colsnum; j++) {
					label = new Label(autoi + j, 3, autocols[j], wcs1);
					sheet.setColumnView(autoi + j, autowidth[j]);
					sheet.addCell(label);
				}
				autoi = autoi + colsnum;
			}
			// 添加标题;
			sheet.mergeCells(0, 0, cols.length + autoi - colsnum, 0);
			wtcf = new WritableCellFormat(wfc2);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wtcf.setAlignment(Alignment.CENTRE);
			label = new Label(0, 0, titlename, wtcf);
			sheet.addCell(label);

			/*
			 * 打印日期 添加带有formatting的DateFormat对象
			 */
			sheet.mergeCells(0, 1, cols.length + autoi - colsnum, 1);
			wtcf = new WritableCellFormat(wfc);
			wtcf.setAlignment(Alignment.RIGHT);
			wtcf.setBorder(Border.ALL, BorderLineStyle.THIN);
			label = new Label(0, 1, datetime, wtcf);
			sheet.addCell(label);
			// 添加数据集;
			if (list != null && list.size() > 0) {
				int rows = 3;
				for (int j = 0; j < list.size(); j++) {
					rows++;
					label = new Label(0, rows, String.valueOf(j + 1), wcs);
					sheet.addCell(label);
					Map<String, Object> map = list.get(j);
					int n = 1;
					for (; n < cols.length + 1; n++) {
						// 从固定列key中获取key
						label = new Label(n, rows, (String.valueOf(map.get(colskey[n - 1]))), wcs);
						sheet.addCell(label);
					}
					// 动态列处理
					for (int i = 0; i < groupname.length; i++) {
						Class cla = null;
						Object obj = null;
						if (map.get(groupkey[i]) != null) {
							obj = map.get(groupkey[i]);
							cla = obj.getClass();
						} else {
							obj = object;
							cla = object.getClass();
						}
						Method methods[] = cla.getDeclaredMethods();// 获取该对象的所有方法名;
						for (int k = 1; k < autocols.length + 1; k++) {
							for (int m = 0; m < methods.length; m++) {
								int tempn = m;
								boolean flag = false;
								while (true) {
									if (methods[tempn].getName().equals("get" + automethodName[k - 1])) {
										label = new Label(n, rows, (String.valueOf(methods[tempn].invoke(obj, null))), wcs);
										sheet.addCell(label);
										flag = true;
										n++;
										break;
									}
									if (tempn == (methods.length - 1)) {
										// tempn = 0;
										// 所有方法一查询完
										n++;
										flag = true;
										break;
									} else {
										tempn++;
									}

								}
								if (flag)
									break;
							}
						}
					}
				}
			}

			workbook.write();
			os.flush();
			workbook.close();
			// os.write("成功导出!".getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}
}
