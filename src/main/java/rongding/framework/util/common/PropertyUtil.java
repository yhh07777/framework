package rongding.framework.util.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtil {
	private String properiesName = "";
	private static PropertyUtil util;
	private static Properties properties;

	public PropertyUtil(String properiesName) {
		this.properiesName = properiesName;
	}

	public static PropertyUtil getInstance(String properiesName) {
		if (util == null) {
			properties = PropertiesUtil.getInstance().load(properiesName);
			util = new PropertyUtil(properiesName);
		}
		return util;
	}

	public String readProperty(String key) {
		String value = "";
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			Properties p = new Properties();
			p.load(is);
			value = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Double getDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	public Integer getInteger(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public Long getLong(String key) {
		return Long.parseLong(getProperty(key));
	}

	public Double readDouble(String key) {
		return Double.parseDouble(readProperty(key));
	}

	public Integer readInteger(String key) {
		return Integer.parseInt(readProperty(key));
	}

	public Long readLong(String key) {
		return Long.parseLong(readProperty(key));
	}

	public Properties getProperties() {
		if (properties != null) {
			return properties;
		}

		Properties p = new Properties();
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			p.load(is);
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return p;
	}

	public void writeProperty(String key, String value) {
		InputStream is = null;
		OutputStream os = null;
		Properties p = new Properties();
		try {
			is = new FileInputStream(properiesName);
			p.load(is);
			os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());
			p.setProperty(key, value);
			p.store(os, key);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
