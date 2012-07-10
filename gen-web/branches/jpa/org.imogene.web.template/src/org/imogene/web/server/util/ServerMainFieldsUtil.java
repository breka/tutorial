package org.imogene.web.server.util;

import org.imogene.common.entity.ImogBean;

public interface ServerMainFieldsUtil {

	public String getDisplayValue(ImogBean bean);

	public String getEnumDisplayValue(Class<?> beanClass, String fieldName,
			String fieldValue);
}
