/**
 * 
 */
package com.supconit.kqfx.web.util;

import java.lang.reflect.Field;



import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ReflectionUtils;

/**
 * @author zongkai
 * 
 */
public class PropertyAnnotationBeanReader extends PropertyPlaceholderConfigurer
		implements BeanPostProcessor, InitializingBean {

	private java.util.Properties prop;

	@SuppressWarnings("unchecked")
	private Class[] enableClassList = { String.class };

	@SuppressWarnings("unchecked")
	public void setEnableClassList(Class[] enableClassList) {
		this.enableClassList = enableClassList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.beans.factory.config.BeanPostProcessor#
	 * postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Properties.class)) {
				if (filterType(field.getType().toString())) {
					Properties p = field.getAnnotation(Properties.class);
					try {
						ReflectionUtils.makeAccessible(field);
						field.set(bean, prop.getProperty(p.key()));
					} catch (Exception ex) {
						LogUtil.error(ex.getLocalizedMessage());
					}
				}
			}
		}

		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.beans.factory.config.BeanPostProcessor#
	 * postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {
		return arg0;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		prop = mergeProperties();
	}

	@SuppressWarnings("unchecked")
	private boolean filterType(String type) {
		if (type != null) {
			for (Class c : enableClassList) {
				if (c.toString().equals(type)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

}
