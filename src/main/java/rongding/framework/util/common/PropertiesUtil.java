package rongding.framework.util.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class PropertiesUtil {
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
	private static PropertiesUtil util = null;
	private static Map<String,Properties> props = null;
	private PropertiesUtil(){
	}
	
	public static PropertiesUtil getInstance() {
		if(util==null) {
			props = new HashMap<String, Properties>();
			util = new PropertiesUtil();
		}
		return util;
	}
	
	public Properties load(String name) {
		return getProps("/"+name+".properties");
	}
	
	public Properties getProps(String propName) {
		Properties properties=props.get(propName);
		if(properties!=null) {
			return properties;
		} else {
			logger.info(propName+" not exist!");
			Properties prop = new Properties();
			try {
				logger.info("getProps-->"+props);
				prop.load(PropertiesUtil.class.getResourceAsStream(propName));
				props.put(propName, prop);
				return prop;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public  <T> T convert(String name,Class<T> clazz) {
		try {
			Properties properties=load(name);
			if (properties==null) {
				throw new RuntimeException("no this properties file:"+name);
			}
			T dest=clazz.newInstance();
			BeanUtils.populate(dest, properties);
			return dest;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
}
