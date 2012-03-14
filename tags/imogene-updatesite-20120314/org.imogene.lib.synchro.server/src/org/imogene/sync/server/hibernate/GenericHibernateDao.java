package org.imogene.sync.server.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.imogene.common.data.Synchronizable;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Medes-IMPS
 */
public class GenericHibernateDao extends HibernateDaoSupport {

	/**
	 * Save or update a bean.
	 * @param entity entity to save or update.
	 */
	public void saveOrUpdate(Synchronizable entity){
		getHibernateTemplate().saveOrUpdate(entity);
	}
	
	/**
	 * Count the number of persisted bean of a specific type.
	 * @param beanClass the class of the bean.
	 * @return number of bean persisted.
	 */
	public int countBeans(Class<?> beanClass){
		Criteria crit = getSession().createCriteria(beanClass);
		crit.setProjection(Projections.rowCount());		
		List<?> list = crit.list();		
		int result = Integer.parseInt(list.get(0).toString());		
		return result;
	}
	
	
	/**
	 * Get bean with this id.
	 * @param beanClass the bean class
	 * @param id the bean id.
	 * @return the persisted bean.
	 */
	public Synchronizable getBean(Class<?> beanClass, String id) {
		
		return (Synchronizable)getHibernateTemplate().get(beanClass, id);
	}
	
	/**
	 * List all beans.
	 * @param beanClass the bean class.
	 * @return list of beans.
	 */
	@SuppressWarnings("unchecked")
	public List<Synchronizable> listBeans(Class beanClass){
		return getHibernateTemplate().loadAll(beanClass);
	}
	
}
