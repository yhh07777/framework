package rongding.framework.orm.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {

	Serializable save(T o);

	Map<String, Object> batchSave(List<T> entitys);

	void delete(T o);

	void update(T o);

	void saveOrUpdate(T o);

	int executeHql(String hql);

	int executeHql(String hql, Map<String, Object> params);
	
	T get(Serializable id);

	T load(Serializable id);

	T getByHql(String hql);

	T getByHql(String hql, Map<String, Object> params);

	T getByFilter(HqlFilter hqlFilter);

	List<T> find();

	List<T> find(String hql);

	List<T> find(String hql, Map<String, Object> params);

	List<T> find(int page, int rows);
	
	List<T> find(String hql, int page, int rows);

	List<T> find(String hql, Map<String, Object> params, int page, int rows);

	List<T> findByFilter(HqlFilter hqlFilter);

	List<T> findByFilter(HqlFilter hqlFilter, int page, int rows);

	Long countByFilter(HqlFilter hqlFilter);
	
	Long count();
	
	Long count(String hql);
	
	Long count(String hql, Map<String, Object> params);

	//========================sql=========================
	int executeSql(String sql);

	int executeSql(String sql, Map<String, Object> params);
	
	int executeSql(String sql, Object... params);
	
	/**
	 * 自动根据sql中以冒号方式传递的参数设置值
	 * @param sql
	 * @param params
	 * @return
	 */
	int batchExecuteSql(String sql, Map<String, Object> params);

	List listBySql(String sql);
	
	List listBySql(String sql,Map<String, Object> params);
	
	List findBySql(String sql);
	
	List findBySql(String sql, int page, int rows);

	List findBySql(String sql, Map<String, Object> params);

	List findBySql(String sql, Map<String, Object> params, int page, int rows);
	
	Map getBySql(String sql);
	
	Map getBySql(String sql, Object... params);

	Map getBySql(String sql, Map<String, Object> params);
	
	Long countBySql(String sql);
	
	Long countBySql(String sql, Object... params);

	Long countBySql(String sql, Map<String, Object> params);
	
	Grid grid(String hql, Map<String, Object> params, int page, int rows);
	
	//========================sqlFilter==============================
	Long countBySqlFilter(SqlFilter sqlFilter);
	
	List selectBySqlFilter(SqlFilter sqlFilter);

	List selectBySqlFilter(SqlFilter sqlFilter,int page, int rows);
	
	Grid grid(SqlFilter sqlFilter,int page, int rows);
	
	Double sumOfFileld(SqlFilter sqlFilter,String field,int page,int rows);
	
	Double sumOfFileld(SqlFilter sqlFilter,String field);
	
}
