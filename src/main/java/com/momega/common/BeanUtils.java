/**
 * 
 */
package com.momega.common;

/**
 * @author martin
 *
 */
public final class BeanUtils {

	@SuppressWarnings("unchecked")
	public static <T> T copyInstance(T source) {
		T target = (T) org.springframework.beans.BeanUtils.instantiate(source.getClass());
		org.springframework.beans.BeanUtils.copyProperties(source, target);
		return target;
	}

}
