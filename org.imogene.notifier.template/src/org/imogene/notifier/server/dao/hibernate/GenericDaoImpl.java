package org.imogene.notifier.server.dao.hibernate;

import org.imogene.notifier.server.dao.GenericDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class GenericDaoImpl extends HibernateDaoSupport implements GenericDao {	
	
	public Object load(Class<?> clazz, String id){
		return getHibernateTemplate().load(clazz, id);
	}

}
