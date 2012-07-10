package org.imogene.lib.common.criteria;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.imogene.lib.common.util.DateUtil;

public class DaoUtil {

	public static <T> Predicate toPredicate(ImogCriterion criterion, CriteriaBuilder builder, Root<T> root) {
		if (criterion instanceof BasicCriteria) {
			Predicate predicate = convert((BasicCriteria) criterion, builder, root);
			if (predicate != null) {
				return predicate;
			}
		} else if (criterion instanceof ImogJunction) {
			Predicate junction = null;
			if (criterion instanceof ImogDisjunction) {
				junction = builder.disjunction();
			} else {
				junction = builder.conjunction();
			}
			for (ImogCriterion c : ((ImogJunction) criterion).getCriterions()) {
				addCriterion(junction, c, builder, root);
			}
			return junction;
		}
		return builder.conjunction();
	}

	public static <T> void addCriterion(Predicate predicate, ImogCriterion criterion, CriteriaBuilder builder,
			Root<T> root) {
		if (criterion == null) {
			return;
		}
		if (criterion instanceof BasicCriteria) {
			Predicate p = convert((BasicCriteria) criterion, builder, root);
			if (p != null) {
				predicate.getExpressions().add(p);
			}
		} else if (criterion instanceof ImogJunction) {
			Predicate junction = builder.conjunction();
			if (criterion instanceof ImogDisjunction) {
				junction = builder.disjunction();
			}
			for (ImogCriterion c : ((ImogJunction) criterion).getCriterions()) {
				addCriterion(junction, c, builder, root);
			}
			predicate.getExpressions().add(junction);
		}
	}

	private static <T> Predicate convert(BasicCriteria criteria, CriteriaBuilder builder, Root<T> root) {
		String property = criteria.getField();
		String operator = criteria.getOperation();
		String value = criteria.getValue();
		if (operator.equals(CriteriaConstants.STRING_OPERATOR_CONTAINS)) {
			return builder.like(root.<String> get(property), "%" + value + "%");
		} else if (operator.equals(CriteriaConstants.STRING_OPERATOR_EQUAL)) {
			return builder.equal(root.<String> get(property), value);
		} else if (operator.equals(CriteriaConstants.STRING_OPERATOR_STARTWITH)) {
			return builder.like(root.<String> get(property), value + "%");
		} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_BEFORE)) {
			try {
				return builder.<Date> lessThanOrEqualTo(root.<Date> get(property), DateUtil.parseDate(value));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_AFTER)) {
			try {
				return builder.<Date> greaterThanOrEqualTo(root.<Date> get(property), DateUtil.parseDate(value));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (operator.equals(CriteriaConstants.DATE_OPERATOR_EQUAL)) {
			try {
				return builder.equal(root.<Date> get(property), DateUtil.parseDate(value));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (operator.equals(CriteriaConstants.INT_OPERATOR_EQUAL)) {
			return builder.equal(root.<Integer> get(property), Integer.valueOf(value));
		} else if (operator.equals(CriteriaConstants.INT_OPERATOR_SUP)) {
			return builder.ge(root.<Integer> get(property), Integer.valueOf(value));
		} else if (operator.equals(CriteriaConstants.INT_OPERATOR_INF)) {
			return builder.le(root.<Integer> get(property), Integer.valueOf(value));
		} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_EQUAL)) {
			return builder.equal(root.<Float> get(property), Float.valueOf(value));
		} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_SUP)) {
			return builder.ge(root.<Float> get(property), Float.valueOf(value));
		} else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_INF)) {
			return builder.le(root.<Float> get(property), Float.valueOf(value));
		} else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL)) {
			return builder.equal(root.<String> get(property), value);
		} else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL)) {
			return root.get(property).isNull();
		} else if (operator.equals(CriteriaConstants.BOOLEAN_OPERATOR_EQUAL)) {
			return builder.equal(root.<Boolean> get(value), Boolean.valueOf(value));
		} else if (operator.equals(CriteriaConstants.OPERATOR_ISNULL)) {
			return root.get(property).isNull();
		} else if (operator.equals(CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY)) {
			return builder.or(root.get(property).isNull(), builder.equal(root.get(property), ""));
		} else if (operator.equals(CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ALL)) {
			return builder.equal(root.<String> get(property), value);
		} else if (operator.equals(CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ONE_OF)) {
			String[] values = value.split(";");
			if (values.length > 0) {
				Predicate disjunction = builder.disjunction();
				final List<Expression<Boolean>> list = disjunction.getExpressions();
				for (String v : values) {
					list.add(builder.like(root.<String> get(property), v + ";%"));
					list.add(builder.like(root.<String> get(property), "%;" + v + ";%"));
					list.add(builder.like(root.<String> get(property), "%;" + v));
					list.add(builder.like(root.<String> get(property), v));
				}
				return disjunction;
			}
		} else if (operator.equals(CriteriaConstants.OPERATOR_ISNOTNULL)) {
			return root.get(property).isNotNull();
		}
		return null;
	}
}
