package rongding.framework.web.struts;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import rongding.framework.util.lang.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

public class DownloadAction extends ActionSupport {
	private static final Logger logger = Logger.getLogger(DownloadAction.class);
	private String specifyPath;
	private String downloadPath;
	private String fileName;
	private String path;

	public void setFileName(String downLoad) {
//		try {
//			downLoad = downLoad.substring(downLoad.lastIndexOf("/") + 1);
//			fileName = new String(downLoad.getBytes(), "ISO8859-1");   
//			logger.info("filename="+fileName);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
			downLoad = downLoad.substring(downLoad.lastIndexOf("/") + 1);
			fileName = toUtf8String(ServletActionContext.getRequest(),downLoad);
			logger.info("filename="+fileName);
	}
	
	 /** 
     * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名. 
     *  
     * @param s 
     *            原文件名 
     * @return 重新编码后的文件名 
     */ 
    public static String toUtf8String(HttpServletRequest request, String s) { 
        String agent = request.getHeader("User-Agent"); 
        try { 
            boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1); 
            if (isFireFox) { 
                s = new String(s.getBytes("UTF-8"), "ISO8859-1"); 
            } else { 
                s = StringUtil.toUtf8String(s); 
                if ((agent != null && agent.indexOf("MSIE") != -1)) { 
                    if (s.length() > 150) { 
                    	s = URLEncoder.encode(s, "UTF-8");  
//                        s = new String(s.getBytes("UTF-8"), "ISO8859-1"); 
                    } 
                } 
            } 
        } catch (UnsupportedEncodingException e) { 
            e.printStackTrace(); 
        } 
        return s; 
    }

	public InputStream getInputStream() {
		if (specifyPath!=null) {
			return ServletActionContext.getServletContext().getResourceAsStream(specifyPath+path);
		}
		return ServletActionContext.getServletContext().getResourceAsStream(downloadPath+path);
	}

	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public void setSpecifyPath(String specifyPath) {
		this.specifyPath = specifyPath;
	}

}
