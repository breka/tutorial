package org.imogene.notif.web.gwt.server.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.imogene.notif.web.gwt.client.NotificationTemplate;
import org.imogene.notif.web.gwt.server.dao.NotificationDao;
import org.imogene.web.gwt.common.criteria.ImogJunction;
import org.imogene.web.server.dao.hibernate.HibernateDaoUtil;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class NotificationDaoImpl extends HibernateDaoSupport implements NotificationDao{

	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#countNotification()
	 */
	@Override
	public int countNotification() {
		Criteria crit = getSession().createCriteria(NotificationTemplate.class);
		crit.setProjection(Projections.rowCount());
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#countNotification(org.imogene.web.gwt.common.criteria.ImogJunction)
	 */
	@Override
	public int countNotification(ImogJunction criterion) {
		Criteria crit = getSession().createCriteria(NotificationTemplate.class);
		crit.setProjection(Projections.rowCount());
		crit.add(HibernateDaoUtil.addImogJunction(criterion));
		List<?> list = crit.list();
		return Integer.parseInt(list.get(0).toString());		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#delete(org.imogene.notif.web.gwt.client.NotificationTemplate)
	 */
	@Override
	public void delete(NotificationTemplate entity) {		
		getHibernateTemplate().delete(entity);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#getNotification(java.lang.String, org.imogene.web.gwt.common.criteria.ImogJunction)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationTemplate getNotification(String id,
			ImogJunction criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(NotificationTemplate.class);
		dc.add(HibernateDaoUtil.addImogJunction(criterions));
		List<NotificationTemplate> result = getHibernateTemplate().findByCriteria(dc);
		if (result != null && result.size() == 1)
			return result.get(0);
		else
			return null;		
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#getNotificationTemplate(java.lang.String)
	 */
	@Override
	public NotificationTemplate getNotification(String id) {		
		return (NotificationTemplate)getHibernateTemplate().load(NotificationTemplate.class, id);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#listNotification(int, int, java.lang.String, boolean, org.imogene.web.gwt.common.criteria.ImogJunction)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> listNotification(int first, int max,
			String property, boolean sortOrder, ImogJunction criterion) {
		DetachedCriteria dc = DetachedCriteria.forClass(NotificationTemplate.class);
		if (property == null)
			property = "name";
		if (!sortOrder)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());

		dc.add(HibernateDaoUtil.addImogJunction(criterion));
		return getHibernateTemplate().findByCriteria(dc, first, max);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#listNotification(int, int, java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> listNotification(int first, int max,
			String sortProperty, boolean sortOrder) {
		DetachedCriteria dc = DetachedCriteria.forClass(NotificationTemplate.class);
		if (sortProperty == null)
			sortProperty = "name";
		if (!sortOrder)
			dc.addOrder(Property.forName(sortProperty).desc());
		else
			dc.addOrder(Property.forName(sortProperty).asc());		
		return getHibernateTemplate().findByCriteria(dc, first, max);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#listNotification(org.imogene.web.gwt.common.criteria.ImogJunction)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> listNotification(ImogJunction criterion) {		
		DetachedCriteria dc = DetachedCriteria.forClass(NotificationTemplate.class);
		dc.add(HibernateDaoUtil.addImogJunction(criterion));
		return getHibernateTemplate().findByCriteria(dc);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#listNotification(java.lang.String, boolean, org.imogene.web.gwt.common.criteria.ImogJunction)
	 */	
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> listNotification(String property,
			boolean asc, ImogJunction criterion) {		
		DetachedCriteria dc = DetachedCriteria.forClass(NotificationTemplate.class);
		if (property == null)
			property = "name";
		if (!asc)
			dc.addOrder(Property.forName(property).desc());
		else
			dc.addOrder(Property.forName(property).asc());
		dc.add(HibernateDaoUtil.addImogJunction(criterion));
		return getHibernateTemplate().findByCriteria(dc);		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notif.web.gwt.server.dao.NotificationDao#saveOrUpdate(org.imogene.notif.web.gwt.client.NotificationTemplate, boolean)
	 */
	@Override
	public void saveOrUpdate(NotificationTemplate entity, boolean isNew) {				
		getHibernateTemplate().merge(entity);
	}		

}
