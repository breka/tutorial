package org.imogene.web.server.dao.hibernate;

import java.util.List;

import org.imogene.web.server.dao.GenericDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class GenericDaoHibernate extends HibernateDaoSupport implements GenericDao {

	@Override
	public Object loadEntity(Class<?> entityClass, String id) {		
		return getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public void saveOrUpdate(Object entity) {		
		getHibernateTemplate().saveOrUpdate(entity);
	}

	@Override
	public List<?> listBeans(Class<?> entityClass) {		
		return getHibernateTemplate().loadAll(entityClass);
	}
	
	@Override	
	public void evictEntity(Object entity) {		
		getHibernateTemplate().evict(entity);
	}
	

}
