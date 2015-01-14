package rongding.framework.orm.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import rongding.framework.util.lang.StringUtil;

public class HiberAspect extends EmptyInterceptor {
	private static final Logger logger = Logger.getLogger(HiberAspect.class);
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			for (int index = 0; index < propertyNames.length; index++) {
				if ("createTime".equals(propertyNames[index])) {
					if (StringUtil.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		for (int index = 0; index < propertyNames.length; index++) {
			if ("updateTime".equals(propertyNames[index])) {
				currentState[index] = new Date();
				continue;
			}
		}
		return true;
	}
}
