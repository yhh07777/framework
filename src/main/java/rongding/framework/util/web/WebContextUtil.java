package rongding.framework.util.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WebContextUtil implements ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(WebContextUtil.class);
	private static ApplicationContext context;

	private WebContextUtil() {
	}

	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 如果有多个Bean符合Class, 取出第一个.
	 */
	public static <T> T getBean(Class<?> clazz) {
		Map beanMaps = context.getBeansOfType(clazz);
		if (beanMaps != null && !beanMaps.isEmpty()) {
			return (T) beanMaps.values().iterator().next();
		} else {
			return null;
		}
	}

	public static ApplicationContext getContext() {
		synchronized (WebContextUtil.class) {
			while (context == null) {
				try {
					logger.debug("getc ApplicationContext, wait...");
					WebContextUtil.class.wait(60000);
					if (context == null) {
						logger.warn("Have been waiting for ApplicationContext to be set for 1 minute",new Exception());
					}
				} catch (InterruptedException ex) {
					logger.debug("getApplicationContext, wait interrupted");
				}
			}
			return context;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		synchronized (WebContextUtil.class) {
			context = applicationContext;
			logger.debug("setApplicationContext, notifyAll");
			WebContextUtil.class.notifyAll();
		}
	}
}
