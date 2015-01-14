package rongding.framework.orm.hibernate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import rongding.framework.util.lang.StringUtil;

/**
 * HQL过滤器，用于添加where条件和排序，过滤结果集
 * 
 * 添加规则使用addFilter方法
 * 
 * 举例：QUERY_t#id_S_EQ = 0 //最终连接出的HQL是 and t.id = :id id的值是0通过参数传递给Dao
 * 
 * 格式说明QUERY前缀就说明要添加过滤条件
 * 
 * t#id 就是t.id
 * 
 * S:String L:Long I:Integer D:Date ST:Short BD:BigDecimal FT:Float AR:ARRAY
 * 
 * EQ 是操作符
 * 
 * // EQ 相等 // NE 不等 // LT 小于 // GT 大于 // LE 小于等于 // GE 大于等于 // LK 模糊 // RLK 右模糊 // LLK 左模糊  // IN in语句
 */
public class HqlFilter {
	private static final Logger logger = Logger.getLogger(HqlFilter.class);
	private HttpServletRequest request;// 为了获取request里面传过来的动态参数
	private Map<String, Object> params = new HashMap<String, Object>();// 条件参数
	private StringBuffer hql = new StringBuffer();
	private String sort;			// 排序字段
	private String order = "asc";	// asc/desc

	public HqlFilter() {

	}

	public HqlFilter(HttpServletRequest request) {
		this.request = request;
		addFilter(request);
	}

	public void addSort(String sort) {
		this.sort = sort;
	}

	public void addOrder(String order) {
		this.order = order;
	}
	
	public void addSortAndOrder(String sort,String order) {
		addSort(sort);
		addOrder(order);
	}
	
	private String getSqlOperator(String operator) {
		if (StringUtils.equalsIgnoreCase(operator, "EQ")) {
			return " = ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "NE")) {
			return " != ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "LT")) {
			return " < ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "GT")) {
			return " > ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "LE")) {
			return " <= ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "GE")) {
			return " >= ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "LK") || StringUtils.equalsIgnoreCase(operator, "RLK") || StringUtils.equalsIgnoreCase(operator, "LLK")) {
			return " like ";
		}
		if (StringUtils.equalsIgnoreCase(operator, "IN") ) {
			return " in ";
		}
		return "";
	}

	public String getWhereHql() {
		return hql.toString();
	}

	public String getWhereAndOrderHql() {
		if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {	//如果排序字段不为空
			if (sort.indexOf(".") < 1) {									//如果不是以t.开头就加上
				sort = "t." + sort;
			}
			hql.append(" order by " + sort + " " + order + " ");			// 添加排序信息
		} else {
			if (request != null) {
				String s = request.getParameter("sort");					//获取排序字段
				String o = request.getParameter("order");					//获取排序顺序
				if (!StringUtils.isBlank(s)) {
					sort = s;
				}
				if (!StringUtils.isBlank(o)) {
					order = o;
				}
				if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
					if (sort.indexOf(".") < 1) {
						sort = "t." + sort;
					}
					hql.append(" order by " + sort + " " + order + " ");	// 添加排序信息
				}
			}
		}
		return hql.toString();
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void addFilter(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			addFilter(name, value);
		}
	}
	
	public void addFilter(String name,List<String> ids) {
		if (name != null && ids!=null&& ids.size()>0) {
			if (name.startsWith("QUERY_")) {											
				String[] filterParams = StringUtils.split(name, "_");					
				if (filterParams.length == 4) {
					String columnName = filterParams[1].replaceAll("#", ".");			
					String operator = filterParams[3];									

					if (hql.toString().indexOf(" where 1=1") < 0) {
						hql.append("  where 1=1 ");
					}
					
					StringBuffer sb=new StringBuffer(" (");
					String placeholder = null;
					for (String id : ids) {
						placeholder = StringUtil.UUID(); 
						params.put("param" +placeholder, id);
						placeholder=":param" +placeholder+",";
						sb.append(placeholder);
					}
					sb.deleteCharAt(sb.length()-1);
					sb.append(")");
					hql.append(" and " + columnName + " " + getSqlOperator(operator) + sb.toString());	// 拼HQL
				}
			}
		}
	}
	
	public void addFilter(String name, String value) {
		if (name != null && !StringUtils.isBlank(value)) {
			if (name.startsWith("QUERY_")) {											// 如果有需要过滤的字段
				String[] filterParams = StringUtils.split(name, "_");					// 以下划线分割
				if (filterParams.length == 4) {
					String columnName = filterParams[1].replaceAll("#", ".");			// 要过滤的字段名称
					String columnType = filterParams[2];								// 字段类型
					String operator = filterParams[3];									// SQL操作符
					String placeholder = UUID.randomUUID().toString().replace("-", ""); // 生成一个随机的参数名称

					if (hql.toString().indexOf(" where 1=1") < 0) {
						hql.append("  where 1=1 ");
					}

					hql.append(" and " + columnName + " " + getSqlOperator(operator) + " :param" + placeholder + " ");	// 拼HQL
					params.put("param" + placeholder, getObjValue(columnType, operator, value));						// 添加参数
				}
			}
		}
	}

	/**
	 * 将String值转换成Object，用于拼写HQL，替换操作符和值
	 * 
	 * S:String L:Long I:Integer D:Date ST:Short BD:BigDecimal FT:Float B:Boolean LI:List
	 * 
	 * @param columnType
	 * @param operator
	 * @param value
	 * @return
	 */
	private Object getObjValue(String columnType, String operator, String value) {
		if (StringUtils.equalsIgnoreCase(columnType, "S")) {
			if (StringUtils.equalsIgnoreCase(operator, "LK")) {
				value = "%%" + value + "%%";
			} else if (StringUtils.equalsIgnoreCase(operator, "RLK")) {
				value = value + "%%";
			} else if (StringUtils.equalsIgnoreCase(operator, "LLK")) {
				value = "%%" + value;
			}
			return value;
		}
		if (StringUtils.equalsIgnoreCase(columnType, "L")) {
			return Long.parseLong(value);
		}
		if (StringUtils.equalsIgnoreCase(columnType, "I")) {
			return Integer.parseInt(value);
		}
		if (StringUtils.equalsIgnoreCase(columnType, "D")) {
			try {
				return DateUtils.parseDate(value, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd" });
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.equalsIgnoreCase(columnType, "ST")) {
			return Short.parseShort(value);
		}
		if (StringUtils.equalsIgnoreCase(columnType, "BD")) {
			return BigDecimal.valueOf(Long.parseLong(value));
		}
		if (StringUtils.equalsIgnoreCase(columnType, "FT")) {
			return Float.parseFloat(value);
		}
		if (StringUtils.endsWithIgnoreCase(columnType, "B")) {
			return Boolean.parseBoolean(value);
		}
		if (StringUtils.endsWithIgnoreCase(columnType, "IN")) {
			return value;
		}
		return null;
	}
	
}
