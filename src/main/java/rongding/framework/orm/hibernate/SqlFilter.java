package rongding.framework.orm.hibernate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import rongding.framework.util.lang.MapUtil;
import rongding.framework.util.lang.StringUtil;

public class SqlFilter {
	private static final Logger logger = Logger.getLogger(SqlFilter.class);
	public static final int WHERE=0x1;
	public static final int SPECIAL=0x2;
	public static final int ORDERBY=0x4;
	public static final int WHERE_SPECIAL=0x3;
	public static final int WHERE_ORDERBY=0x5;
	public static final int WHERE_SPECIAL_ORDERBY=0x7;
	private HttpServletRequest request;
	private Map<String, Object> params = new HashMap<String, Object>();
	private String sort;			
	private String order = "asc";	
	private boolean orderByFlag=false;
	private boolean whereFlag=false;
	private StringBuffer sqlSelect=new StringBuffer();
	private StringBuffer sqlWhere=new StringBuffer();
	private StringBuffer sqlOrderBy=new StringBuffer();
	private boolean special=false;
	private Map<String, String> replaceMap;
	
	public SqlFilter(HttpServletRequest request, boolean special) {
		this.special = special;
		this.request = request;
		addFilter(request);
	}
	
	public SqlFilter(HttpServletRequest request,int flag) {
		initFlag(flag);
		this.request = request;
		addFilter(request);
	}
	
	public SqlFilter(HttpServletRequest request,String sql,int flag) {
		this.sqlSelect.append(sql);
		initFlag(flag);
		this.request = request;
		addFilter(request);
	}
	
	private void initFlag(Integer flag) {
		String s=new DecimalFormat("000").format(Integer.parseInt(Integer.toBinaryString(flag)));
		if (s.charAt(2)=='1') {
			whereFlag=true;
		}
		if (s.charAt(1)=='1') {
			special=true;
		}
		if (s.charAt(0)=='1') {
			orderByFlag=true;
		}
	}

	public SqlFilter(StringBuffer sqlSelect, boolean special) {
		this.sqlSelect = sqlSelect;
		this.special = special;
	}

	public SqlFilter(HttpServletRequest request, StringBuffer sqlSelect, boolean special) {
		this.sqlSelect = sqlSelect;
		this.special = special;
		this.request = request;
		addFilter(request);
	}

	public SqlFilter() {
	}
	
	public SqlFilter(String sql) {
		this.sqlSelect.append(sql);
	}
	
	public SqlFilter(HttpServletRequest request, String sql) {
		this.sqlSelect.append(sql);
		this.request = request;
		addFilter(request);
	}

	public SqlFilter(HttpServletRequest request) {
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
		this.sort = sort;
		this.order = order;
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

	public Map<String, Object> getParams() {
		return params;
	}

	private boolean checkWhere() {
		if (whereFlag) {
			return true;
		}
		boolean flag=false;
		if (special) {
			int index=sqlSelect.indexOf("}");
			String newSql=sqlSelect.substring(index+1);
			flag=sqlWhere.indexOf("where") < 0 && newSql.indexOf("where")<0;
		}else {
			flag=sqlWhere.indexOf("where") < 0 && sqlSelect.indexOf("where")<0;
		}
		if (flag) {
			sqlWhere.append(" where 1=1 ");
			whereFlag=true;
		}
		return flag;
	}
	
	public void addFilter(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			addFilter(name, value);
		}
	}
	
	public void addOrFilter(String name, String value) {
		if (name != null && !StringUtils.isBlank(value)) {
			if (name.startsWith("SQL")) {											
				String[] filterParams = StringUtils.split(name, "|");					
				if (filterParams.length == 4) {
					addOrFilter(filterParams, value);
				}
			}
		}
	}
	
	public void addOrFilter(String[] filterParams, String value) {
		String columnType = filterParams[2];								
		String columnName = unpackColumnName(filterParams[1]);
		String operator = filterParams[3];			

		checkWhere();
		
		String[] columnNames=StringUtils.split(columnName,",");
		StringBuffer sb=new StringBuffer(" and (");
		for (String c : columnNames) {
			String placeholder = UUID.randomUUID().toString().replace("-", "");
			sb.append( c + " " + getSqlOperator(operator) + " :p" + placeholder + " ");
			params.put("p" + placeholder, getObjValue(columnType, operator, value));
			sb.append(" or ");
		}
		sb.delete(sb.length()-3, sb.length());
		sb.append(") ");
		sqlWhere.append(sb.toString());	
	}
	
	public void addFilter(String name,List<String> ids) {
		if (name != null && ids!=null&& ids.size()>0) {
			if (name.startsWith("SQL")) {											
				String[] filterParams = StringUtils.split(name, "|");					
				if (filterParams.length == 4) {
					String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
					String columnType = filterParams[2];
					String operator = filterParams[3];									
					
					if (StringUtils.startsWithIgnoreCase(columnType, "UN")) {
						String newColumType=columnType.substring(3);
						for (String n : StringUtils.split(columnName,",")) {
							addFilter("SQL|"+n+"|"+newColumType+"|"+operator, ids);
						}
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "RE")) {
						StringBuffer sb=new StringBuffer(" (");
						String placeholder = null;
						for (String id : ids) {
							placeholder = StringUtil.UUID(); 
							sb.append(":param" +placeholder+",");
							params.put("param" +placeholder,id);
						}
						sb.deleteCharAt(sb.length()-1);
						sb.append(")");
						String key=StringUtils.split(filterParams[0],"#")[1];
						addReplace(key, " and " + columnName + " " + getSqlOperator(operator) + sb.toString());
						return;
					}
					
					checkWhere();
					
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
					sqlWhere.append(" and " + columnName + " " + getSqlOperator(operator) + sb.toString());
				}
			}
		}
	}
	
	public void addFilter(String name,String[] ids) {
		addFilter(name, Arrays.asList(ids));
	}
	
	public void addFilter(String name, String value) {
		if (name != null && !StringUtils.isBlank(value)) {
			if (name.startsWith("SQL")) {											
				String[] filterParams = StringUtils.split(name, "|");					
				if (filterParams.length == 4) {
					String columnType = filterParams[2];								
					String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
					String operator = filterParams[3];			

					if (StringUtils.startsWithIgnoreCase(columnType, "AR")) {
						addFilter(name, value.split(","));
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "RE")) {
						addFilterByReplace(name, value);
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "UN")) {
						addFilterByUnion(name, value);
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "OR")) {
						filterParams[2]=unpack(columnType,"_");
						addOrFilter(filterParams, value);
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "OV")) {
						addOrFilterByValue(filterParams, value);
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "VN")) {
						addAndFilterByValue(filterParams, value);
						return;
					}
					
					if (StringUtils.startsWithIgnoreCase(columnType, "MUL")) {
						addFilter(name, StringUtils.split(value, "|"));
						return;
					}
					
					checkWhere();
					
					addParam(columnName, columnType, operator, value);
				}
			}
		}
	}
	
	private String unpackColumnName(String s) {
		return s.replaceAll("#", ".").replace("$", "||");	
	}
	
	private String unpack(String s) {
		return unpack(s,"_");
	}
	
	private String unpack(String s,String ch) {
		String[] strings=StringUtils.split(s,ch);
		if (strings==null||strings.length<2) {
			throw new RuntimeException("解包失败:s="+s+",ch="+ch);
		}
		return strings[1];
	}
	
	/**
	 * SQL|t1.id,t2.id|UN_S|EQ
	 * @param name
	 * @param value
	 */
	private void addFilterByUnion(String name, String value) {
		String[] filterParams = StringUtils.split(name, "|");	
		String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
		String columnType = filterParams[2];								
		String operator = filterParams[3];		
		String newColumType=columnType.substring(3);
		for (String n : StringUtils.split(columnName,",")) {
			addFilter("SQL|"+n+"|"+newColumType+"|"+operator, value);
		}
	}
	
	/**
	 * SQL|t1.id|VN_S|EQ
	 * @param name
	 * @param value
	 */
	private void addAndFilterByValue(String[] filterParams, String value) {
		String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
		String columnType = filterParams[2];								
		String operator = filterParams[3];		
		String newColumType=unpack(columnType);
		for (String v : StringUtils.split(value,",")) {
			addFilter("SQL|"+columnName+"|"+newColumType+"|"+operator, v);
		}
	}
	
	/**
	 * sqlFilter.addFilterByOrValue("SQL|t1.id|OV_S|EQ","11,22");
	 * @param name
	 * @param value
	 */
	private void addOrFilterByValue(String[] filterParams, String value) {
		String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
		String columnType = filterParams[2];								
		String operator = filterParams[3];		
		String newColumType=unpack(columnType);
		String[] values=StringUtils.split(value,",");
		StringBuffer sb=new StringBuffer(" and (");
		for (String v : values) {
			String placeholder = UUID.randomUUID().toString().replace("-", "");
			sb.append( columnName + " " + getSqlOperator(operator) + " :p" + placeholder + " ");
			params.put("p" + placeholder, getObjValue(newColumType, operator, v));
			sb.append(" or ");
		}
		sb.delete(sb.length()-3, sb.length());
		sb.append(") ");
		sqlWhere.append(sb.toString());	
	}

	/**
	 * SQL#area_id|t.id|RE_S|EQ
	 * @param name
	 * @param value
	 */
	private void addFilterByReplace(String name,String value) {
		String[] filterParams = StringUtils.split(name, "|");	
		String columnName = filterParams[1].replaceAll("#", ".").replace("$", "||");			
		String columnType = filterParams[2];								
		String operator = filterParams[3];	
		String[] newColumnTypes=StringUtils.split(columnType,"_");
		String newColumType=newColumnTypes[1];
		String key=StringUtils.split(filterParams[0],"#")[1];
		addReplace(key, " and " + columnName + " " + getSqlOperator(operator) + " :" + key + " ");
		params.put(key, getObjValue(newColumType, operator, value));
	}

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
				return DateUtils.parseDate(value, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd","yyyyMMdd" });
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
	
	private String generateSql() {
		String returnSql=sqlSelect+sqlWhere.toString()+sqlOrderBy.toString();
		returnSql=handleReplaceArr(returnSql);
		if (special) {
			char[] arr=returnSql.toString().toCharArray();
			for (int i = 0; i < arr.length; i++) {
				if (arr[i]=='{'||arr[i]=='}') {
					arr[i]=' ';
				}
			}
			returnSql=new String(arr);
		}
		
		logger.info("sql-->"+returnSql);
		logger.info("params-->"+params);
		return returnSql;
	}
	
	public String getCountSql() {
		String newSql=sqlSelect.toString()+sqlWhere.toString()+sqlOrderBy.toString();
		newSql=handleReplaceArr(newSql);
		if (special) {
			int start=newSql.indexOf('{');
			int end=newSql.indexOf('}');
			if (start==-1||end==-1) {
				throw new RuntimeException("can't find '{' or '}'");
			}
			return  new String( newSql.substring(0, start)+" count(*) " +newSql.substring(end+1) ); 
		}
		return "select count(*) "+newSql.substring(StringUtil.indexIgnoreCase(newSql, "from"));
	}
	
	public String getSql() {
		if (orderByFlag) {
			return generateSql();
		}
		
		if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {	//如果排序字段不为空
			sqlOrderBy.append(" order by " + sort + " " + order + " ");			// 添加排序信息
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
					sqlOrderBy.append(" order by " + sort + " " + order + " ");	// 添加排序信息
				}
			}
		}
		orderByFlag=true;
		return generateSql();
	}
	
	private String handleReplaceArr(String returnSql){
		char[] arr=returnSql.toCharArray();
		StringBuffer sb=new StringBuffer();
		StringBuffer inner=new StringBuffer();
		boolean flag=false;
		for (int i = 0; i < arr.length; i++) {
			char c=arr[i];
			if ('$'==c) {
				if (i+1<=arr.length && arr[i+1]=='{') {
					inner=new StringBuffer();
					flag=true;
				}
				continue;
			}
			if (flag) {
				if ('}'==c) {
					String key=inner.toString();
					if (replaceMap!=null&&replaceMap.size()>0&&replaceMap.containsKey(key)) {
						String value=replaceMap.get(key);
						if (value==null) {
							throw new RuntimeException("key="+key+",value="+value+"不存在!");
						}
						sb.append(value);
					}
					flag=false;
				}else {
					if ('{'!=c) {
						inner.append(c);
					}
				}
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public String getSqlWhere() {
		return sqlWhere.toString();
	}

	public void setSqlSelect(String sqlSelect) {
		if (this.sqlSelect.length()==0) {
			this.sqlSelect.append(sqlSelect);
		}
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public StringBuffer getSqlSelect() {
		return sqlSelect;
	}

	public void setWhereFlag(boolean whereFlag) {
		this.whereFlag = whereFlag;
	}

	public StringBuffer getSqlOrderBy() {
		return sqlOrderBy;
	}

	public void setSqlOrderBy(String sqlOrderBy) {
		if (this.sqlOrderBy.length()==0) {
			this.sqlOrderBy.append(sqlOrderBy);
		}
		this.orderByFlag=true;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	
	private void addParam(String columnName, String columnType, String operator, String value) {
		String placeholder = UUID.randomUUID().toString().replace("-", ""); 
		sqlWhere.append(" and " + columnName + " " + getSqlOperator(operator) + " :param" + placeholder + " ");	
		params.put("param" + placeholder, getObjValue(columnType, operator, value));				
	}
	
	public void addParameter(String name,Object value) {
		if (!StringUtil.isEmpty(value)) {
			params.put(name, value);				
		}
	}

	public void setReplaceMap(Map<String, String> replaceMap) {
		this.replaceMap = replaceMap;
	}
	public void addReplace(String key,String value) {
		if (this.replaceMap==null) {
			this.replaceMap=new HashMap<String,String>();
		}
		this.replaceMap.put(key, value);
	}
	
	public void addReplace(String key,Map<String, Object> params,String sql) {
		if (MapUtil.isEmpty(params)) {
			return;
		}
		
		if (this.replaceMap==null) {
			this.replaceMap=new HashMap<String,String>();
		}
		this.replaceMap.put(key, sql);
		
//		this.params.putAll(params);
		for (String k : params.keySet()) {
			Object v=params.get(k);
			if (v==null) {
				throw new RuntimeException("param:"+k+" is null!");
			}
			this.params.put(k, v);
		}
	}
}
