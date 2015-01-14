package rongding.framework.util.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class ReflectionUtil {
	public static final int LOWWER=0;
	public static final int UPPER=1;
	public static final int HUMP=2;
	 
	
	/**
	 * bean对象转换为map,自动过滤null
	 * @param bean
	 * @param map
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			Class<? extends Object> clazz = bean.getClass();
			Field[] fields = bean.getClass().getDeclaredFields();	// 获得属性
			for (Field field : fields) {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
				Method getMethod = pd.getReadMethod();				// 获得get方法
				Object value = getMethod.invoke(bean);				// 执行get方法返回一个Object
				if (value!=null) {
					map.put(field.getName(), value);
				}
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
    
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map map) {
    	return convertMap(type, map, HUMP);
    }
    
    public static Object convertMap(Class type, Map map,int flag) {
    	try {
    		BeanInfo beanInfo = Introspector.getBeanInfo(type);
            Object obj = type.newInstance(); 

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = null;
                if (flag==LOWWER) {
                	propertyName = descriptor.getName().toLowerCase();
				}else if (flag==UPPER) {
					propertyName = descriptor.getName().toUpperCase();
				} else if (flag==HUMP) {
					propertyName = descriptor.getName();
				}
                if (map.containsKey(propertyName)) {
                	BeanUtils.copyProperty(obj, propertyName, map.get(propertyName));
                }
            }
            return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * 通过属性名称获得属性值
	 * 
	 * @param object
	 * @param propertyName
	 * @return
	 */
	public static Object getValue(Object object, String propertyName) {
		try {
			Class<? extends Object> clazz = object.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
			Method getMethod = pd.getReadMethod();// 获得get方法
			Object o = getMethod.invoke(object);// 执行get方法返回一个Object
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * 根据对象获得所有字段的值
	 * 
	 * @param object
	 */
	public static void method(Object object) {
		try {
			Class<? extends Object> clazz = object.getClass();
			Field[] fields = object.getClass().getDeclaredFields();// 获得属性
			for (Field field : fields) {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object o = getMethod.invoke(object);// 执行get方法返回一个Object
				System.out.println(o);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
