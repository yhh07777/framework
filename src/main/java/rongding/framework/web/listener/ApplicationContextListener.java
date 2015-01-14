package rongding.framework.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApplicationContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext= sce.getServletContext();
		servletContext.setAttribute("ctx", servletContext.getContextPath());
		WebApplicationContext webApplicationContext=WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		webApplicationContext.getBean("systemService");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
