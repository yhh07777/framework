package rongding.framework.orm.jdbc;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	public static void rollBack(Connection con){
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, Object>> executeQuery(Connection con, String sql) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			return getListFromRs(rs);
		}finally{
			closeRs(rs);
			closePst(pst);
		}
	}
	
	public static List<Object> executeQuery(Connection con, String sql, Class<?> c) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			return getListFromRs(rs, c);
		}finally{
			closeRs(rs);
			closePst(pst);
		}
	}
	
	public static List<Map<String, Object>> getListFromRs(ResultSet rs) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		int i;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while(rs.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(i = 0; i < columns; i++){
				map.put(md.getColumnName(i + 1), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
			}
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> getListFromRsLowerCase(ResultSet rs) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		int i;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while(rs.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(i = 0; i < columns; i++){
				map.put(md.getColumnName(i + 1).toLowerCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
			}
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> getListFromRsUpperCase(ResultSet rs) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		int i;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while(rs.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(i = 0; i < columns; i++){
				map.put(md.getColumnName(i + 1).toUpperCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
			}
			list.add(map);
		}
		return list;
	}
	
	public static List<Object> getListFromRs(ResultSet rs, Class<?> c) throws SQLException{
		List<Object> list = new ArrayList<Object>();
		try {
			while(rs.next()){
				Object o = initObjectFromRs(rs, c);
				list.add(o);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Object getFirstObjectFromRs(ResultSet rs, Class<?> c) throws SQLException{
		Object o = null;
		try {
			o = initObjectFromRsIfExist(rs, c);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	private static Object getValueByType(ResultSet rs, int type, String name) throws SQLException{
		switch(type){
			case Types.NUMERIC:
				return rs.getLong(name);
			case Types.VARCHAR:
				return rs.getString(name);
			case Types.DATE:
				return rs.getDate(name);
			case Types.TIMESTAMP:
				return rs.getTimestamp(name);
			case Types.INTEGER:
				return rs.getInt(name);
			case Types.DOUBLE:
				return rs.getDouble(name);
			case Types.FLOAT:
				return rs.getFloat(name);
			case Types.BIGINT:
				return rs.getLong(name);
			default:
				return rs.getObject(name);
		}
	}
	
	private static boolean rsContainsFields(ResultSet rs, String fieldName) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		for(int i = 0; i < md.getColumnCount(); i++){
			if(md.getColumnName(i + 1).equalsIgnoreCase(fieldName)){
				return true;
			}
		}
		return false;
	}
	
	private static Object initObjectFromRs(ResultSet rs, Class<?> c) throws InstantiationException, SQLException, IllegalAccessException{
		Object o = c.newInstance();
		Method[] methods = o.getClass().getMethods();
		for(Method m: methods){
			if(m.getName().startsWith("set")){
				try {					
					m.invoke(o, getParamValueFromRs(rs, m));					
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + m.getName());
				} catch (InvocationTargetException e) {
					throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + m.getName());
				}
			}
		}
		return o;
	}
	
	private static Object initObjectFromRsIfExist(ResultSet rs, Class<?> c) throws SQLException, IllegalAccessException, InstantiationException{
		Object o = c.newInstance();
		Method[] methods = o.getClass().getMethods();
		String field;
		for(Method m: methods){
			field = m.getName().substring(3);
			if(m.getName().startsWith("set") && rsContainsFields(rs, field)){
				try {					
					m.invoke(o, getParamValueFromRs(rs, m));					
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + m.getName());
				} catch (InvocationTargetException e) {
					throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + m.getName());
				}
			}
		}
		return o;
	}
	
	private static Object getParamValueFromRs(ResultSet rs, Method m) throws SQLException
	{
		String fieldName = m.getName().substring(3);
		Type type = m.getGenericParameterTypes()[0];		
		return getValueFromRs(rs, fieldName, type);
	}
	
	private static Object getValueFromRs(ResultSet rs, String fieldName, Type t) throws SQLException{
		String type = t.toString();
		try{
			if(type.equals("int") || type.equals("class java.lang.Integer")){
				return rs.getInt(fieldName);
			}else if(type.equals("float") || type.equals("class java.lang.Float")){
				return rs.getFloat(fieldName);
			}else if(type.equals("double") || type.equals("class java.lang.Double")){
				return rs.getDouble(fieldName);
			}else if(type.equals("long") || type.equals("class java.lang.Long")){
				return rs.getLong(fieldName);
			}else if(type.equals("class java.lang.String")){
				return rs.getString(fieldName);
			}else if(type.equals("class java.sql.Timestamp")){
				return rs.getTimestamp(fieldName);
			}else if(type.equals("class java.sql.Date")){
				return rs.getDate(fieldName);
			}else if(type.equals("class java.sql.Time")){
				return rs.getTime(fieldName);
			}
		}catch(SQLException e){
			throw new SQLException("SQLException when get field:" + fieldName + "\n" + e);
		}
		throw new RuntimeException("getValueFromRsByField fail, field type is:" + type + ",field name is:" + fieldName);
	}
	
	public static void close(ResultSet rss,Statement pst,Connection con){
		DBUtil.closeRs(rss);
		DBUtil.closePst(pst);
		DBUtil.closeCon(con);
	}
	
	public static void closeRs(ResultSet... rss){
		for(ResultSet rs: rss){
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void closePst(Statement... psts){
		for(Statement pst: psts){
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void closeCon(Connection... cons){
		for(Connection con: cons){
			if(con != null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}