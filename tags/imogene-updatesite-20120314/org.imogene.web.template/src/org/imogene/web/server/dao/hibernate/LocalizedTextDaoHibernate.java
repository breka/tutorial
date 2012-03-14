package org.imogene.web.server.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.collection.PersistentSet;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.imogene.web.gwt.common.criteria.ImogJunction;
import org.imogene.web.gwt.common.entity.LocalizedText;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Manage persistence for LocalizedText
 */
public class LocalizedTextDaoHibernate extends HibernateDaoSupport
		implements
			LocalizedTextDao {

	public Logger logger = Logger.getLogger(LocalizedTextDaoHibernate.class
			.getName());

	@Override
	public void saveOrUpdate(LocalizedText bean) {
		bean.setUploadDate(new Date(System.currentTimeMillis()));
		getHibernateTemplate().merge(bean);
	}

	@Override
	public void saveOrUpdateShadow(LocalizedText bean) {
		getHibernateTemplate().merge(bean);

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(String fieldId) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(Restrictions.eq("fieldId", fieldId));
		return getHibernateTemplate().findByCriteria(dc);
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(ImogJunction criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(HibernateDaoUtil.addImogJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(int first, int max,
			String property, boolean asc) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		if (property == null)
			property = "lastModificationDate";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());
		return getHibernateTemplate().findByCriteria(dc, first, max);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(String property, boolean asc,
			ImogJunction criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		if (property == null)
			property = "lastModificationDate";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());
		dc.add(HibernateDaoUtil.addImogJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(int first, int max,
			String property, boolean asc, ImogJunction criterions) {

		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		if (property == null)
			property = "lastModificationDate";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());

		dc.add(HibernateDaoUtil.addImogJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc, first, max);
	}

	@Override
	public int countLocalizedText(ImogJunction criterions) {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.setProjection(Projections.rowCount());
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	@Override
	public int countLocalizedText() {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.setProjection(Projections.rowCount());
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	@Override
	public void delete(LocalizedText entity) {
		getHibernateTemplate().delete(entity);
	}

	@Override
	public LocalizedText getLocalizedText(String id) {
		return (LocalizedText) getHibernateTemplate().get(LocalizedText.class,
				id);
	}

	@Override
	public List<LocalizedText> listLocalizedText(List<String> ids) {
		List<LocalizedText> entities = new Vector<LocalizedText>();
		for (String id : ids) {
			LocalizedText entity = getLocalizedText(id);
			if (entity != null)
				entities.add(entity);
		}
		return entities;
	}

	@Override
	@SuppressWarnings("unchecked")
	public LocalizedText getLocalizedText(String id, ImogJunction criterions) {

		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(HibernateDaoUtil.addImogJunction(criterions));

		List<LocalizedText> result = getHibernateTemplate().findByCriteria(dc);
		if (result != null && result.size() == 1)
			return result.get(0);
		else
			return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listNonAffectedLocalizedText(String property,
			ImogJunction criterions) {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.isNull(property));
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listNonAffectedLocalizedText(int first, int max,
			String sortProperty, boolean asc, String property,
			ImogJunction criterions) {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.isNull(property));
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.setFirstResult(first);
		crit.setMaxResults(max);
		if (asc)
			crit.addOrder(Order.asc(sortProperty));
		else
			crit.addOrder(Order.desc(sortProperty));
		return crit.list();
	}

	@Override
	public int countNonAffectedLocalizedText(String property,
			ImogJunction criterions) {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.setProjection(Projections.rowCount());
		crit.add(Restrictions.isNull(property));
		if (criterions != null)
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listNonAffectedLocalizedTextReverse(int first,
			int max, String sortProperty, boolean asc, String property,
			ImogJunction criterions) {

		Criteria crit = getSession()
				.createCriteria(LocalizedText.class)
				.createAlias(property, "other", CriteriaSpecification.LEFT_JOIN)
				.add(Restrictions.isNull("other.id"))
				.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.setFirstResult(first);
		crit.setMaxResults(max);
		if (asc)
			crit.addOrder(Order.asc(sortProperty));
		else
			crit.addOrder(Order.desc(sortProperty));
		return crit.list();
	}

	@Override
	public int countNonAffectedLocalizedTextReverse(String property,
			ImogJunction criterions) {
		Criteria crit = getSession()
				.createCriteria(LocalizedText.class)
				.createAlias(property, "other", CriteriaSpecification.LEFT_JOIN)
				.add(Restrictions.isNull("other.id"));
		if (criterions != null)
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.setProjection(Projections.rowCount());
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listAffectedCardNProperty(String property,
			String id) {
		Criteria crit = getSession()
				.createCriteria(LocalizedText.class)
				.createAlias(property, "other",
						CriteriaSpecification.INNER_JOIN)
				.add(Restrictions.eq("other.id", id));
		return crit.list();
	}

	/**
	 * Refresh the bean from database
	 */
	public void refresh(LocalizedText entity) {
		getHibernateTemplate().refresh(entity);
	}

	/**
	 * Flush the session
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 *
	 */
	public void clear() {
		getHibernateTemplate().clear();
	}

	/* relation dependences */

	/**
	 * Convert an hibernate collection to a 
	 * collection based on a Vector class.
	 */
	private static <T> List<T> lazyCollection(Set<T> lazyCollection) {
		Vector<T> collection = new Vector<T>();
		for (T mt : lazyCollection) {
			collection.add(mt);
		}
		return collection;
	}

	@SuppressWarnings("unused")
	private void gileadFix(Object toFix) {
		if (toFix instanceof PersistentSet)
			((PersistentSet) toFix).dirty();
	}
}
