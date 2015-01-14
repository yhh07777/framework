package rongding.framework.orm.sqlcontext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class SqlContext {
	private static final Logger logger = Logger.getLogger(SqlContext.class);
	private static SqlContext instance = null;
	private Map<String, String> sqlMap = null;
	private Vector properties = null;

	private SqlContext() {
		sqlMap = new HashMap<String, String>();
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new SqlContext();
		}
	}

	public static String getSql(Class clazz, String methodName) {
		return getSql(clazz.getSimpleName() + "." + methodName);
	}

	public static String getSql(String key) {
		Map<String, String> map = getInstance().sqlMap;
		String sql = map.get(key);
		if (sql == null) {
			parseKey(map, key);
		}
		return map.get(key);
	}

	private static synchronized void parseKey(Map<String, String> map, String key) {
		int index = key.lastIndexOf('.');
		String parentPath = key.substring(0, index);
		String methodName = key.substring(index + 1);
		String path = "sql/" + parentPath + "/" + methodName + ".sql";
//		URL urlpath = SqlContext.class.getResource(path);
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		path = url.toString();
		if (path.startsWith("file")) {
			path = path.substring(5);
		}
		path.replace("/", File.separator);
		logger.debug("path=" + path);
		File file=new File(path);
		if (!file.exists()) {
			throw new RuntimeException("sql file not exist:"+path);
		}
		try {
			String sql = FileUtils.readFileToString(file);
			map.put(key, sql);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getPackagePath(String filename) {
		String result = null;
		URL urlpath = Thread.currentThread().getContextClassLoader().getResource("");
		String path = urlpath.toString();
		if (path.startsWith("file")) {
			path = path.substring(5);
		}
		path.replace("/", File.separator);
		result = path + filename;
		return result;
	}

	public static SqlContext getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	public Vector getProperties() {
		return properties;
	}

	public void updateProperties() {
		SqlContext shadow = new SqlContext();
		properties = shadow.getProperties();

	}
}