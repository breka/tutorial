package org.imogene.ws.util;

import org.imogene.ws.entity.MedooBean;

public interface ServerMainFieldsUtil {

	public String getDisplayValue(MedooBean bean);
	
	public String getEnumDisplayValue(Class<?> beanClass, String fieldName, String fieldValue);
}
