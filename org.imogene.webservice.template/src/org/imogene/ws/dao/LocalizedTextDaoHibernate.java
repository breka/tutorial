package org.imogene.ws.dao;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.imogene.ws.criteria.HibernateDaoUtil;
import org.imogene.ws.criteria.MedooJunction;
import org.imogene.ws.entity.LocalizedText;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * Manage persistence for LocalizedText
 */
public class LocalizedTextDaoHibernate extends HibernateDaoSupport
		implements
			LocalizedTextDao {

	public Logger logger = Logger.getLogger("medoo.dao");
	
	

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(String fieldId) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(Restrictions.eq("fieldId", fieldId));
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	public void saveOrUpdate(LocalizedText bean, boolean isNew) {
		bean.setUploadDate(new Date(System.currentTimeMillis()));
		getHibernateTemplate().merge(bean);

	}

	@Override
	public void saveOrUpdateShadow(LocalizedText bean, boolean isNew) {
		getHibernateTemplate().merge(bean);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(MedooJunction criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(HibernateDaoUtil.addMedooJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText() {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
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
			MedooJunction criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		if (property == null)
			property = "lastModificationDate";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());
		dc.add(HibernateDaoUtil.addMedooJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LocalizedText> listLocalizedText(int first, int max,
			String property, boolean asc, MedooJunction criterions) {

		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		if (property == null)
			property = "lastModificationDate";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());

		dc.add(HibernateDaoUtil.addMedooJunction(criterions));
		return getHibernateTemplate().findByCriteria(dc, first, max);
	}

	@Override
	public int countLocalizedText(MedooJunction criterions) {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.setProjection(Projections.rowCount());
		crit.add(HibernateDaoUtil.addMedooJunction(criterions));
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
		return (LocalizedText) getHibernateTemplate().get(LocalizedText.class, id);
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
	public LocalizedText getLocalizedText(String id, MedooJunction criterions) {

		DetachedCriteria dc = DetachedCriteria.forClass(LocalizedText.class);
		dc.add(HibernateDaoUtil.addMedooJunction(criterions));

		List<LocalizedText> result = getHibernateTemplate().findByCriteria(dc);
		if (result != null && result.size() == 1)
			return result.get(0);
		else
			return null;
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

}
