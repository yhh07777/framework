package rongding.framework.orm.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

public interface BaseDao<T> {

	Session getCurrentSession();
	
	Serializable save(T o);
	
	void batchSave(List<T> entitys);

	void delete(T o);

	void update(T o);

	void saveOrUpdate(T o);

	T load(Class<T> c, Serializable id);
	
	T get(Class<T> c, Serializable id);

	T getByHql(String hql);

	T getByHql(String hql, Map<String, Object> params);

	List<T> find(String hql);

	List<T> find(String hql, Map<String, Object> params);

	List<T> find(String hql, int page, int rows);

	List<T> find(String hql, Map<String, Object> params, int page, int rows);

	Long count(String hql);

	Long count(String hql, Map<String, Object> params);
	
	int executeHql(String hql);

	int executeHql(String hql, Map<String, Object> params);

	//==============sql=====================
	int executeSql(String sql);

	int executeSql(String sql, Object... params);

	int executeSql(String sql, Map<String, Object> params);
	
	Map getBySql(String sql, Object... params);
	
	Map getBySql(String sql,Map<String, Object> params);
	
//	Map<String, Object> loadMapBySql(String sql, Object... params);
//
//	Map<String, Object> loadMapBySqlParams(String sql, Map<String, Object> params);
	
	List listBySql(String sql, Map<String, Object> params);
	
	List findBySql(String sql);

	List findBySql(String sql, int page, int rows);

	List findBySql(String sql, Map<String, Object> params);

	List findBySql(String sql, Map<String, Object> params, int page, int rows);
	
	BigDecimal countBySql(String sql);

	BigDecimal countBySql(String sql, Map<String, Object> params);

	BigDecimal countBySql(String sql, Object... params);

}
