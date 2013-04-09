package org.imogene.web.server.dao.hibernate;

import java.text.ParseException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.imogene.web.gwt.common.criteria.BasicCriteria;
import org.imogene.web.gwt.common.criteria.CriteriaConstants;
import org.imogene.web.gwt.common.criteria.ImogCriterion;
import org.imogene.web.gwt.common.criteria.ImogDisjunction;
import org.imogene.web.gwt.common.criteria.ImogJunction;
import org.imogene.web.server.util.DateUtil;


/**
 * This class permits to convert the Imogene criterions 
 * to the Hibernate criterions and the inverse.
 * @author Medes-IMPS
 */
public class HibernateDaoUtil {

	/**
	 * Add an imogene junction
	 * @param junction the imogene junction
	 * @return An Hibernate criterion
	 */
	public static Criterion addImogJunction(ImogJunction junction){
		Junction criterion = null;
		if(junction.getType().equals(ImogDisjunction.TYPE)){
			criterion = Restrictions.disjunction();			
		}
		else{
			criterion = Restrictions.conjunction();						
		}
		for(ImogCriterion crit:junction.getCriterions()){
			if(crit instanceof BasicCriteria){
				addCriterion(criterion, (BasicCriteria)crit);
			}
			else{
				criterion.add(addImogJunction((ImogJunction)crit));
			}
		}
		return criterion;
	}			
		
	
	/**
	 * Add a Hibernate criterion based on the imogene criteria 
	 * @param junction  The hibernate junction
	 * @param criteria the imogene criteria to add
	 */
	public static void addCriterion(Junction junction, BasicCriteria criteria){
		String property = criteria.getField();
		String operator = criteria.getOperation();
		String value = criteria.getValue();
		try {
			if (operator.equals(CriteriaConstants.STRING_OPERATOR_CONTAINS)) {
				junction.add(Restrictions.ilike(property, "%" + value + "%"));  				
			} else if (operator.equals(CriteriaConstants.STRING_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, value));  				
			} else if (operator.equals(CriteriaConstants.STRING_OPERATOR_STARTWITH)) {
				junction.add(Restrictions.ilike(property, value + "%"));  				
			} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_BEFORE)) {
				//value = value + " 23:59";
				junction.add(Restrictions.le(property, DateUtil.parseDate(value)));  										
			} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_AFTER)) {
				junction.add(Restrictions.ge(property, DateUtil.parseDate(value)));  										
			} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, DateUtil.parseDate(value)));  							
			} else if (operator.equals(CriteriaConstants.INT_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, Integer.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.INT_OPERATOR_SUP)) {
				junction.add(Restrictions.ge(property, Integer.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.INT_OPERATOR_INF)) {
				junction.add(Restrictions.le(property, Integer.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, Float.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_SUP)) {
				junction.add(Restrictions.ge(property, Float.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_INF)) {
				junction.add(Restrictions.le(property, Float.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, value));  				
			} else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL)) {
				junction.add(Restrictions.isNull(property));  				
			} else if (operator.equals(CriteriaConstants.BOOLEAN_OPERATOR_EQUAL)) {
				junction.add(Restrictions.eq(property, Boolean.valueOf(value)));  				
			} else if (operator.equals(CriteriaConstants.OPERATOR_ISNULL)) {
				junction.add(Restrictions.isNull(property));
			} else if (operator.equals(CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY)) {
				Junction orJunction = Restrictions.disjunction();
				orJunction.add(Restrictions.isNull(property));
				orJunction.add(Restrictions.eq(property, ""));
				junction.add(orJunction);
			} else if (operator.equals(CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ALL)) {
				junction.add(Restrictions.eq(property, value));
			} else if (operator.equals(CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ONE_OF)) {				
				String[] values = value.split(";");
				if(values.length>0) {
					Junction disJunction = Restrictions.disjunction();
					for(String cur:values) {
						disJunction.add(Restrictions.ilike(property, cur + ";%"));
						disJunction.add(Restrictions.ilike(property, "%;" + cur + ";%"));
						disJunction.add(Restrictions.ilike(property, "%;" + cur));
						disJunction.add(Restrictions.eq(property, cur));
					}
					junction.add(disJunction);
				}
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}
					
	
}
