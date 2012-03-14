package org.imogene.common.dao.criteria;

public class CriteriaConstants {
	
	public static final String FIELDVALUE_SEPARATOR = ";";
	
	public static final String STRING_OPERATOR_CONTAINS = "contains";
	public static final String STRING_OPERATOR_STARTWITH = "startsWith";
	public static final String STRING_OPERATOR_EQUAL = "equalString";
	public static final String STRING_OPERATOR_DIFF = "diffString";
	public static final String STRING_OPERATOR_INF = "infString";
	public static final String STRING_OPERATOR_SUP = "supString";
	public static final String DATE_OPERATOR_BEFORE = "before";
	public static final String DATE_OPERATOR_AFTER = "after";
	public static final String DATE_OPERATOR_EQUAL = "equalDate";
	public static final String INT_OPERATOR_EQUAL = "equalInt";
	public static final String INT_OPERATOR_SUP = "supInt";
	public static final String INT_OPERATOR_INF = "infInt";
	public static final String FLOAT_OPERATOR_EQUAL = "equalFloat";
	public static final String FLOAT_OPERATOR_SUP = "supFloat";
	public static final String FLOAT_OPERATOR_INF = "infFloat";
	public static final String RELATIONFIELD_OPERATOR_EQUAL = "equalRelationField";
	public static final String RELATIONFIELD_OPERATOR_EQUAL_NULL = "equalNull";
	public static final String BOOLEAN_OPERATOR_EQUAL = "equalBoolean";	
	public static final String MULTIENUM_OPERATOR_EQUAL = "equalMultiEnum";	
	public static final String OPERATOR_ISNULL = "isNull";	
	public static final String OPERATOR_UNDEF = "undef";
	
	
	/**
	 * For user interface only, for db query, converted into a before and after
	 */
	public static final String DATE_OPERATOR_BETWEEN = "between";
	/**
	 * For user interface only, for db query, converted into a before and after
	 */	
	public static final String INT_OPERATOR_BETWEEN = "betweenInt";
	/**
	 * For user interface only, for db query, converted into a before and after
	 */	
	public static final String FLOAT_OPERATOR_BETWEEN = "betweenFloat";

}
