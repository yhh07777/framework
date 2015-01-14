package rongding.framework.web.struts;

import java.lang.reflect.Method;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import rongding.framework.web.struts.annotation.LogMessage;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class OperateLogInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(OperateLogInterceptor.class);
	private LogHandler logHandler;

	public String intercept(ActionInvocation invocation) {
		String result = null;
		HttpServletRequest request = ServletActionContext.getRequest();

		ServletContext sc = ServletActionContext.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
		Method method=null;
		
		BaseAction action = (BaseAction) invocation.getAction();
		try {
			method=action.getClass().getMethod(invocation.getProxy().getMethod(), null);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		LogMessage logMessage=method.getAnnotation(LogMessage.class);
		if (logMessage!=null) {
			try {
				result = invocation.invoke();
				if (method.isAnnotationPresent(LogMessage.class)) {
					logHandler.success(ctx, request,logMessage.message(),logMessage.level());
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				logHandler.fail(ctx, request,e ,logMessage.message(),logMessage.level());
				return Action.SUCCESS;
			}
		}else {
			try {
				result = invocation.invoke();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void setLogHandler(LogHandler logHandler) {
		this.logHandler = logHandler;
	}
}