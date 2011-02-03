package org.imogene.notifier.server.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.imogene.notifier.server.common.NotificationTemplate;
import org.imogene.notifier.server.dao.NotificationDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class NotificationDaoImpl extends HibernateDaoSupport implements
		NotificationDao {


	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationTemplate> getNotification(String type, String ope) {

		DetachedCriteria dc = DetachedCriteria
				.forClass(NotificationTemplate.class);
		dc.add(Restrictions.ilike("operation", ope));
		dc.add(Restrictions.ilike("sourceCard", type));
		return (List<NotificationTemplate>) getHibernateTemplate()
				.findByCriteria(dc);

	}	

}
