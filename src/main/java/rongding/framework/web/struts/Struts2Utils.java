package rongding.framework.web.struts;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import rongding.framework.util.web.JsonUtil;
import rongding.framework.web.ajax.Json;

public class Struts2Utils {
    private Struts2Utils(){};

	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;

	private static final String TEXT_TYPE = "text/plain";
	private static final String JSON_TYPE = "application/json";
	private static final String XML_TYPE = "text/xml";
	private static final String HTML_TYPE = "text/html";
	private static final String JS_TYPE = "text/javascript";

	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}


	public static void render(final String contentType, final String content, final String... headers) {
		try {
			//分析headers参数
			String encoding = ENCODING_DEFAULT;	//默认编码格式UTF-8
			boolean noCache = NOCACHE_DEFAULT;	//是否缓存
			
			//遍历所有头文件
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");
				
				
				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {//编码格式
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {//是否缓存
					noCache = Boolean.parseBoolean(headerValue);
				} else {
                    throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
                }
			}

			HttpServletResponse response = ServletActionContext.getResponse();

			//设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 直接输出文本.
	 */
	public static void renderText(final String text, final String... headers) {
		render(TEXT_TYPE, text, headers);
	}

	/**
	 * 直接输出HTML.
	 */
	public static void renderHtml(final String html, final String... headers) {
		render(HTML_TYPE, html, headers);
	}

	/**
	 * 直接输出XML.
	 */
	public static void renderXml(final String xml, final String... headers) {
		render(XML_TYPE, xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * @param object Java对象,将被转化为json字符串.
	 */
	public static void renderJson(final Object object, final String... headers) {
		String jsonString = JsonUtil.writeJson(object);
		render(JSON_TYPE, jsonString, headers);
	}
	
	/**
	 * 直接输出JSON.
	 * @param object Java对象,将被转化为json字符串.
	 */
	public static void renderJson(final Object object) {
		String jsonString = JsonUtil.writeJson(object);
		render(JSON_TYPE, jsonString, new String[]{});
	}

	/**
	 * 输出图片
	 * @param data
	 * @param type
	 */
    public static void renderImage(byte[] data,String type){
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType(type);
            OutputStream out = response.getOutputStream();
            out.write(data, 0, data.length);
            out.flush();
        } catch (IOException e) {
        	throw new RuntimeException("渲染图像失败",e);
        }
    }

	/**
	 * 直接输出支持跨域Mashup的JSONP.
	 *
	 * @param callbackName callback函数名.
	 * @param contentMap Map对象,将被转化为json字符串.
	 */
	public static void renderJsonp(final String callbackName, final Map contentMap, final String... headers) {
		String jsonParam = JsonUtil.writeJson(contentMap);
		StringBuilder result = new StringBuilder().append(callbackName).append("(").append(jsonParam).append(");");

		//渲染Content-Type为javascript的返回内容,输出结果为javascript语句, 如callback197("{content:'Hello World!!!'}");
		render(JS_TYPE, result.toString(), headers);
	}
	
	public static void ajaxSuccess(String msg,Object obj) {
		renderJson(new Json(true,msg,obj));
	}
	
	public static void ajaxSuccess(Object obj) {
		ajaxSuccess(null, obj);
	}
	public static void ajaxFail(String msg) {
		renderJson(new Json(false,msg,null));
	}
	
}