package org.imogene.web.server.util;

import org.imogene.common.criteria.BasicCriteria;
import org.imogene.common.criteria.CriteriaConstants;
import org.imogene.common.criteria.ImogJunction;

public class FilterFieldsHelper {
	public static final String EMPTY_FILTER = "undefined-filter";

	public static void addEmptyFilter(ImogJunction junction) {
		BasicCriteria criteria = new BasicCriteria();
		criteria.setOperation(CriteriaConstants.STRING_OPERATOR_EQUAL);
		criteria.setField("id");
		criteria.setValue(EMPTY_FILTER);
		junction.add(criteria);
	}
}
