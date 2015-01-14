package rongding.framework.web.struts;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

public interface LogHandler {
	void success(ApplicationContext ctx, HttpServletRequest request, String message, int level);

	void fail(ApplicationContext ctx, HttpServletRequest request, Exception e, String message, int level);
}
