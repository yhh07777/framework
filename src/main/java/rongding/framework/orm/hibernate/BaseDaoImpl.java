package rongding.framework.orm.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDaoImpl<T> implements BaseDao<T> {
	@Autowired
	private SessionFactory sessionFactory;
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	private void initPageRows(Query q,int page,int rows){
		if (page!=-1&&rows!=-1) {
			 q.setFirstResult((page - 1) * rows).setMaxResults(rows);
		}
	}
	private void initParams(Query q, Object... params) {
		for (int i = 0; i < params.length; i++) {
			q.setParameter(i, params[i]);
		}
	}
	private void initParams(Query q,Map<String, Object> params){
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
	}
	private Query initQuery(String hql,Map<String, Object> params){
		Query q = getCurrentSession().createQuery(hql);
		initParams(q,params);
		return q;
	}
	private Query initQuery(String hql,Map<String, Object> params,int page,int rows){
		Query q=initQuery(hql, params);
		initPageRows(q,page,rows);
		return q;
	}
	
	private Query initSqlQuery(String sql,Map<String, Object> params){
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q;
	}
	
	private SQLQuery initSqlQuery(String sql,Map<String, Object> params,int page,int rows){
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		if (page!=-1&&rows!=-1) {
			 q.setFirstResult((page - 1) * rows).setMaxResults(rows);
		}
		return q;
	}

	@Override
	public Serializable save(T o) {
		return o!=null?getCurrentSession().save(o):null;
	}

	@Override
	public T get(Class<T> clazz, Serializable id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Override
	public T getByHql(String hql) {
		return (T) getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		return (T) initQuery(hql,params).uniqueResult();
	}

	@Override
	public void delete(T o) {
		if (o != null) {
			getCurrentSession().delete(o);
		}
	}

	@Override
	public void update(T o) {
		if (o != null) {
			getCurrentSession().update(o);
		}
	}

	@Override
	public void saveOrUpdate(T o) {
		if (o != null) {
			getCurrentSession().saveOrUpdate(o);
		}
	}
	
	@Override
	public int executeHql(String hql) {
		return getCurrentSession().createQuery(hql).executeUpdate();
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		return initQuery(hql, params).executeUpdate();
	}
	
	//======================hql==============================
	@Override
	public List<T> find(String hql) {
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = initQuery(hql, params);
		if (params.get(":ids")!=null) {
			q.setParameterList("ids", (Collection) params.get(":ids"));
		}
		return q.list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = initQuery(hql, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		Query q = getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		return (Long) getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		initParams(q, params);
		return (Long) q.uniqueResult();
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public List<Map> findBySql(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public int executeSql(String sql) {
		return getCurrentSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		return q.executeUpdate();
	}
	
	@Override
	public int executeSql(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		initParams(q,params);
		return q.executeUpdate();
	}

	@Override
	public BigDecimal countBySql(String sql) {
		Query q = getCurrentSession().createSQLQuery(sql);
		BigDecimal bigDecimal=(BigDecimal) q.uniqueResult();
		return bigDecimal;
	}
	
	@Override
	public BigDecimal countBySql(String sql, Object... params) {
		Query q = getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		BigDecimal bigDecimal=(BigDecimal) q.uniqueResult();
		return bigDecimal;
	}

	@Override
	public BigDecimal countBySql(String sql, Map<String, Object> params) {
		Query q = getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		BigDecimal bigDecimal=(BigDecimal) q.uniqueResult();
		return bigDecimal;
	}
	
	@Override
	public T load(Class<T> c, Serializable id) {
		return (T) getCurrentSession().load(c, id);
	}

	@Override
	public void batchSave(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			getCurrentSession().save(list.get(i));
			if (i != 0 && (i+1) % 20 == 0) {
				getCurrentSession().flush();
				getCurrentSession().clear();
			}
		}
	}

	@Override
	public List listBySql(String sql, Map<String, Object> params) {
		return initSqlQuery(sql, params).list();
	}
	
	@Override
	public List findBySql(String sql, int page, int rows) {
		SQLQuery q=initSqlQuery(sql, null, page, rows);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	public Map getBySql(String sql, Object... params) {
		Query q=getCurrentSession().createSQLQuery(sql);
		initParams(q, params);
		return null;
	}
	
	@Override
	public Map getBySql(String sql, Map<String, Object> params) {
		return  (Map) initSqlQuery(sql, params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}
	
	@Override
	public List findBySql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


}
