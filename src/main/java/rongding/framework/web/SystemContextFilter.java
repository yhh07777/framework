package rongding.framework.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemContextFilter implements Filter {
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse res=(HttpServletResponse) response;
		req.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		int pageOffset = 0;
		try {
			String pageOffsetString=req.getParameter("pager.offset");
			if (pageOffsetString!=null) {
				pageOffset = Integer.parseInt(req.getParameter("pager.offset"));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		SystemContext.setPageSize(10);
		SystemContext.setPageOffset(pageOffset);
		chain.doFilter(req, res);
		SystemContext.removePageSize();
		SystemContext.removeCurrentPage();
	}

	public void init(FilterConfig conf) throws ServletException {
	}

}
