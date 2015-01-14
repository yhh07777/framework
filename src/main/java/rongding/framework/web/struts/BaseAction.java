package rongding.framework.web.struts;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import rongding.framework.orm.hibernate.BaseService;
import rongding.framework.orm.hibernate.Grid;
import rongding.framework.orm.hibernate.HqlFilter;
import rongding.framework.orm.hibernate.SqlFilter;
import rongding.framework.orm.sqlcontext.SqlContext;
import rongding.framework.util.lang.StringUtil;
import rongding.framework.util.poi.ExcelUtil;
import rongding.framework.util.reflect.BeanUtils;
import rongding.framework.util.reflect.GenericsUtil;
import rongding.framework.util.time.DateUtil;
import rongding.framework.util.web.FastjsonFilter;
import rongding.framework.web.ajax.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("basePackage")
@Namespace("/")
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	private static final Logger logger = Logger.getLogger(BaseAction.class);
	protected static final String INDEX="index";
	
	protected int page = 1;				// 当前页
	protected int rows = 10;			// 每页显示记录数
	protected String sort;				// 排序字段
	protected String order = "asc";		// asc/desc
	protected String q;					// easyui的combo和其子类过滤时使用

	protected String ids;				// 主键集合，逗号分割
	protected T data;					// 数据模型(与前台表单name相同，name="data.xxx")

	protected BaseService<T> service;	// 业务逻辑

	protected File upload;
	protected String uploadFileName;
	protected String uploadContentType;
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	
	public String index() {
		return INDEX;
	}
	
	public void setService(BaseService<T> service) {
		this.service = service;
	}
	public  Object getSessionAttr(String name) {
		return getSession().getAttribute(name);
	}
	public  void setSessionAttr(String name,Object value) {
		getSession().setAttribute(name,value);
	}
	public  void setRequestAttr(String name,Object value) {
		getRequest().setAttribute(name,value);
	}
	public  Object getRequestAttr(String name) {
		return getRequest().getAttribute(name);
	}
	public String getRootPath() {
		return getSession().getServletContext().getRealPath("");
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
	public SqlFilter initSqlFilter(Class c,String sqlName,int flag) {
		return new SqlFilter(getRequest(),SqlContext.getSql(c ,sqlName), flag);
	}
	
	public SqlFilter initSqlFilter(Class c,String sqlName) {
		return new SqlFilter(getRequest(),SqlContext.getSql(c ,sqlName));
	}
	
	public SqlFilter initSqlFilter(Class c) {
		return new SqlFilter(getRequest(),SqlContext.getSql(c ,"grid"));
	}
	
	public String batchSave(String[] fieldList,String success,String fail) {
		List<T> list=ExcelUtil.getInstance().readExcel2ObjsByMethod(upload, data.getClass(), 0, 0, fieldList);
		Map<String, Object> importMap = service.batchSave(list);
		Grid grid=new Grid();
		grid.setRows((List) importMap.get("list"));
		List<Map<String, Object>> footer=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put(success, "成功记录数:<font color=green>"+importMap.get("success")+"</font>");
		map.put(fail,"失败记录数:<font color=red>"+importMap.get("fail")+"</font>");
		footer.add(map);
		footer.add(map);
		grid.setFooter(footer);
		setRequestAttr("grid",  JSON.toJSONString(grid, new FastjsonFilter()));
		setRequestAttr("jsUrl",  getRequest().getParameter("jsUrl"));
		return "batchResult"; 
	}
	
	public String batchSave1(String[] fieldList,String success,String fail) {
		List<T> list=ExcelUtil.getInstance().readExcel2ObjsByMethod(upload, data.getClass(),fieldList);
		Map<String, Object> importMap = service.batchSave(list);
		Grid grid=new Grid();
		grid.setRows((List) importMap.get("list"));
		List<Map<String, Object>> footer=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put(success, "成功记录数:<font color=green>"+importMap.get("success")+"</font>");
		map.put(fail,"失败记录数:<font color=red>"+importMap.get("fail")+"</font>");
		footer.add(map);
		footer.add(map);
		grid.setFooter(footer);
		setRequestAttr("grid",  JSON.toJSONString(grid, new FastjsonFilter()));
		setRequestAttr("jsUrl",  getRequest().getParameter("jsUrl"));
		return "batchResult"; 
	}
	
	public void excel(List<T> list,String[] title,String[] value,String templateName,String exportName,Map<String, String> map) {
		exportName=initExportName(exportName);
		try {
			OutputStream os=null;
			HttpServletResponse response=getResponse();
			response.setContentType("application/msexcel; charset=utf-8");
			ExcelUtil.initExportName(getRequest(), response, exportName);
			
			try {
				os=response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExcelUtil.getInstance().exportStreamByTemplateAndMethod(map, templateName, os, title, value, list);
		} catch (Exception e) {
			e.printStackTrace();
			Json json=new Json();
			json.fail("导出记录条数过多!");
		}
	}
	
	private String initExportName(String exportName){
		HttpServletRequest request=getRequest();
		String beginTime=request.getParameter("QUERY_t#createTime_D_GE");
		String endTime=request.getParameter("QUERY_t#createTime_D_LE");
		String time=null;
		if (StringUtil.isBlank(beginTime) && StringUtil.isBlank(endTime)) {
			time="";
		}else if (!StringUtil.isBlank(beginTime) && StringUtil.isBlank(endTime)) {
			time=DateUtil.stringtoDatetime(beginTime);
		}else if (StringUtil.isBlank(beginTime) && !StringUtil.isBlank(endTime)) {
			time=DateUtil.stringtoDatetime(endTime);
		}else {
			time=DateUtil.stringtoDatetime(beginTime)+"-"+DateUtil.stringtoDatetime(endTime);
		}
		return exportName+time+".xls";
	}

	public void excel(String[] title,String[] value,String templateName,String exportName,Map<String, String> map) {
		exportName=initExportName(exportName);
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		
		try {
			List<T> list=service.findByFilter(hqlFilter, 1, Integer.MAX_VALUE);
			OutputStream os=null;
			HttpServletResponse response=getResponse();
			
			ExcelUtil.initExportName(getRequest(), response, exportName);
//			response.setContentType("application/msexcel; charset=utf-8");
//			exportName=new String(exportName.getBytes("utf-8"),"ISO-8859-1");
//			response.setHeader("Content-disposition", "inline; filename=" + exportName);
			try {
				os=response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExcelUtil.getInstance().exportStreamByTemplateAndMethod(map, templateName, os, title, value, list);
		} catch (Exception e) {
			e.printStackTrace();
			Json json=new Json();
			json.fail("导出记录条数过多!");
		}
	}
	
	public void writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
			}
			logger.info("对象转JSON：要排除的属性[" + excludesProperties + "]要包含的属性[" + includesProperties + "]");
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			logger.info("转换后的JSON字符串：" + json);
			getResponse().setContentType("text/html;charset=utf-8");
			getResponse().getWriter().write(json);
			getResponse().getWriter().flush();
			getResponse().getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeText(Object object, String[] includesProperties, String[] excludesProperties) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
			}
			logger.info("对象转JSON：要排除的属性[" + excludesProperties + "]要包含的属性[" + includesProperties + "]");
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			logger.info("转换后的JSON字符串：" + json);
			getResponse().setContentType("text/plain;charset=utf-8");
			getResponse().getWriter().write(json);
			getResponse().getWriter().flush();
			getResponse().getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeText(Object object) {
		writeText(object, null, null);
	}
	
	public void writeJson(Object json) {
		writeJsonByFilter(json, null, null);
	}

	public void writeJsonByIncludesProperties(Object object, String[] includesProperties) {
		writeJsonByFilter(object, includesProperties, null);
	}

	public void writeJsonByExcludesProperties(Object object, String[] excludesProperties) {
		writeJsonByFilter(object, null, excludesProperties);
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	public void getById() {
		try {
			String id = org.apache.commons.beanutils.BeanUtils.getProperty(data, "id");
			logger.debug("查找对象的id="+id);
			if (!StringUtils.isBlank(id)) {
				writeJson(service.get(id));
			} else {
				Json j = new Json();
				j.setMsg("主键不可为空！");
				writeJson(j);
			}
		} catch (Exception e) {
			writeJson(new Json(false,e.getMessage()));
		} 
	}
	
	public void find() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		writeJson(service.findByFilter(hqlFilter, page, rows));
	}

	public void findAll() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		writeJson(service.findByFilter(hqlFilter));
	}
	
	public void selectAll() {
		try {
			SqlFilter sqlFilter=new SqlFilter(getRequest());
			writeJson(service.grid(sqlFilter, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			writeJson(new Json(false));
		}
	}
	
	protected List<Map<String, Object>> selectAllList(Class c,String sqlName) {
		String sql = SqlContext.getSql(c, sqlName);
		HttpServletRequest request=ServletActionContext.getRequest();
		SqlFilter sqlFilter = new SqlFilter(request, sql);
		return service.selectBySqlFilter(sqlFilter, 1, Integer.MAX_VALUE);
	}

	public void grid() {
		try {
			Grid grid = new Grid();
			HqlFilter hqlFilter = new HqlFilter(getRequest());
			grid.setTotal(service.countByFilter(hqlFilter));
			grid.setRows(service.findByFilter(hqlFilter, page, rows));
			writeJson(grid);
		} catch (Exception e) {
			e.printStackTrace();
			writeJson(new Json(false));
		}
	}
	
	public void gridAll() {
		Grid grid = new Grid();
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		List<T> l = service.findByFilter(hqlFilter);
		grid.setTotal((long) l.size());
		grid.setRows(l);
		writeJson(grid);
	}

	public void treeGrid() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		writeJson(service.findByFilter(hqlFilter));
	}

	public void save() {
		Json json = new Json();
		try {
			if (data != null) {
				service.save(data);
				json.setSuccess(true);
				json.setMsg("保存成功！");
			}
		} catch (Exception e) {
			json.fail(e.getMessage());
		}
		writeJson(json);
	}

	public void update() {
		Json json = new Json();
		String id = null;
		try {
			if (data != null) {
				id = (String) FieldUtils.readField(data, "id", true);
			}
			if (!StringUtils.isBlank(id)) {
				T t = service.get(id);
				BeanUtils.copyNotNullProperties(data, t, new String[] { "createTime" });
				service.update(t);
				json.setSuccess(true);
				json.setMsg("更新成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.fail(e.getMessage());
		}
		writeJson(json);
	}

	public void delete() {
		Json json = new Json();
		try {
			String id = org.apache.commons.beanutils.BeanUtils.getProperty(data, "id");
			if (id!=null) {
				T t = service.get(id);
				service.delete(t);
				json.setSuccess(true);
				json.setMsg("删除成功！");
			}
			writeJson(json);
		} catch (Exception e) {
			writeJson(new Json(false,"删除失败！"));
		} 
	}

	@Override
	public T getModel() {
		try {
			Class<T> c = GenericsUtil.getSuperClassGenricType(getClass());
			return (T) (data==null?data= c.newInstance():data);
		} catch (Exception e) {
			throw new RuntimeException("BaseAction->getModel error!",e);
		} 
	}

}
