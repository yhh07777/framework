package rongding.framework.orm.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import rongding.framework.util.reflect.GenericsUtil;

public class BaseServiceImpl<T> implements BaseService<T>, InitializingBean {
	@Autowired
	private BaseDao<T> baseDao;
	private Class<T> clazz;
	private String className;

	@Override
	public void afterPropertiesSet() throws Exception {
		clazz = GenericsUtil.getSuperClassGenricType(getClass());
		className = clazz.getName();
	}

	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public Serializable save(T o) {
		return baseDao.save(o);
	}

	@Override
	public void delete(T o) {
		baseDao.delete(o);
	}

	@Override
	public void update(T o) {
		baseDao.update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		baseDao.saveOrUpdate(o);
	}
	
	@Override
	public int executeHql(String hql) {
		return baseDao.executeHql(hql);
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		return baseDao.executeHql(hql, params);
	}

	@Override
	public T get(Serializable id) {
		return baseDao.get(clazz, id);
	}

	@Override
	public T load(Serializable id) {
		return baseDao.load(clazz, id);
	}

	@Override
	public T getByHql(String hql) {
		return baseDao.getByHql(hql);
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		return baseDao.getByHql(hql, params);
	}

	@Override
	public T getByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from " + className + " t";
		return getByHql(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find() {
		return findByFilter(new HqlFilter());
	}

	@Override
	public List<T> find(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return baseDao.find(hql, params);
	}

	@Override
	public List<T> findByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		return baseDao.find(hql, page, rows);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		return baseDao.find(hql, params, page, rows);
	}

	@Override
	public List<T> find(int page, int rows) {
		return findByFilter(new HqlFilter(), page, rows);
	}

	@Override
	public List<T> findByFilter(HqlFilter hqlFilter, int page, int rows) {
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams(), page, rows);
	}
	
	@Override
	public Grid grid(String hql, Map<String, Object> params, int page, int rows) {
		Grid grid = new Grid();
		grid.setRows(find(hql, params, page, rows));
		grid.setTotal(count(hql, params));
		return grid;
	}

	@Override
	public Long count() {
		return countByFilter(new HqlFilter());
	}
	
	@Override
	public Long count(String hql) {
		return baseDao.count(hql);
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		return baseDao.count(hql, params);
	}

	@Override
	public Long countByFilter(HqlFilter hqlFilter) {
		String hql = "select count(distinct t) from " + className + " t";
		return count(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}

	//================================sql================================
	@Override
	public List findBySql(String sql) {
		return baseDao.findBySql(sql);
	}

	@Override
	public List findBySql(String sql, int page, int rows) {
		return baseDao.findBySql(sql, page, rows);
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params, int page, int rows) {
		return baseDao.findBySql(sql, params, page, rows);
	}

	@Override
	public int executeSql(String sql) {
		return baseDao.executeSql(sql);
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public int executeSql(String sql, Object... params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public Long countBySql(String sql) {
		return baseDao.countBySql(sql).longValue();
	}

	@Override
	public Long countBySql(String sql, Object... params) {
		return baseDao.countBySql(sql, params).longValue();
	}

	@Override
	public Long countBySql(String sql, Map<String, Object> params) {
		return baseDao.countBySql(sql, params).longValue();
	}

	@Override
	public Map<String, Object> batchSave(List<T> entitys) {
		baseDao.batchSave(entitys);
		return null;
	}
	
	@Override
	public Map getBySql(String sql, Object... params) {
		return baseDao.getBySql(sql, params);
	}

	@Override
	public Map getBySql(String sql, Map<String, Object> params) {
		return baseDao.getBySql(sql, params);
	}

	@Override
	public Map getBySql(String sql) {
		return baseDao.getBySql(sql);
	}

	// ======================sqlFilter==========================
	@Override
	public List selectBySqlFilter(SqlFilter sqlFilter, int page, int rows) {
		return findBySql(sqlFilter.getSql(), sqlFilter.getParams(), page, rows);
	}

	@Override
	public List selectBySqlFilter(SqlFilter sqlFilter) {
		return selectBySqlFilter(sqlFilter, -1, -1);
	}

	@Override
	public Long countBySqlFilter(SqlFilter sqlFilter) {
		return countBySql(sqlFilter.getCountSql(), sqlFilter.getParams());
	}

	@Override
	public List listBySql(String sql) {
		return baseDao.listBySql(sql, null);
	}

	@Override
	public List listBySql(String sql, Map<String, Object> params) {
		return baseDao.listBySql(sql, params);
	}
	
	@Override
	public List findBySql(String sql, Map<String, Object> params) {
		return baseDao.findBySql(sql, params);
	}

	@Override
	public Double sumOfFileld(SqlFilter sqlFilter, String field, int page, int rows) {
		String sql = "select _t.*,rownum as rn from (" + sqlFilter.getSql() + ")_t";
		sql = "select sum(" + field + ") from (" + sql + ")";
		if (page != -1 && rows != -1) {
			sql += " where rn<=" + page * rows;
		}
		BigDecimal sum = baseDao.countBySql(sql, sqlFilter.getParams());
		return sum == null ? 0.0 : sum.doubleValue();
	}

	@Override
	public Double sumOfFileld(SqlFilter sqlFilter, String field) {
		return sumOfFileld(sqlFilter, field, -1, -1);
	}

	@Override
	public Grid grid(SqlFilter sqlFilter, int page, int rows) {
		Grid grid = new Grid();
		grid.setTotal(countBySqlFilter(sqlFilter));
		List<Map<String, Object>> list = selectBySqlFilter(sqlFilter, page, rows);
		grid.setRows(list);
		return grid;
	}

	@Override
	public int batchExecuteSql(String sql, Map<String, Object> params) {
		Set<String> keys = new HashSet<String>();
		boolean flag = false;
		StringBuffer sb = null;
		int len = sql.length();
		for (int i = 0; i < len; i++) {
			char c = sql.charAt(i);
			if (flag) {
				sb.append(c);
			}
			if (c == ':') {
				flag = true;
				sb = new StringBuffer();
			}
			if (flag && c == ' ' || flag && i == len - 1 || flag && c == ',') {
				flag = false;
				keys.add(sb.toString());
				sb = new StringBuffer();
			}
		}
		Map<String, Object> newParams = new HashMap<String, Object>();
		for (String k : keys) {
			String t = k.trim();
			int l = k.length() - 1;
			if (k.charAt(l) == ',') {
				t = k.substring(0, l);
			}
			if (!params.containsKey(t)) {
				throw new RuntimeException("key=" + t + " is not set");
			}
			newParams.put(t, params.get(t));
		}
		return executeSql(sql, newParams);
	}
}
