package org.imogene.ws.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class GenericDaoHibernate extends HibernateDaoSupport implements GenericDao {

	public Object loadEntity(Class<?> entityClass, String id) {		
		return getHibernateTemplate().get(entityClass, id);
	}

	public void saveOrUpdate(Object entity) {		
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public List<?> listBeans(Class<?> entityClass) {		
		return getHibernateTemplate().loadAll(entityClass);
	}
	
	

}
