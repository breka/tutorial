package org.imogene.web.server.util;

import org.imogene.web.gwt.common.entity.ImogBean;

public interface ServerMainFieldsUtil {

	public String getDisplayValue(ImogBean bean);
	
	public String getEnumDisplayValue(Class<?> beanClass, String fieldName, String fieldValue);
}
