package org.imogene.data.handler.server;

import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.sync.server.hibernate.GenericHibernateDao;

public class GenericDataHandler {
	
	private GenericHibernateDao dao;
	
	/**
	 * Save or update a bean.
	 * 
	 * @param entity entity to save or update.
	 */
	public void saveOrUpdate(Synchronizable entity){
		dao.saveOrUpdate(entity);
	}
	
	/**
	 * Count the number of persisted bean of a specific type.
	 * 
	 * @param beanClass the class of the bean.
	 * @return number of bean persisted.
	 */
	public int countBeans(Class<?> beanClass){
		return dao.countBeans(beanClass);
	}
	
	
	/**
	 * Get bean with this id.
	 * 
	 * @param beanClass the bean class
	 * @param id the bean id.
	 * @return the persisted bean.
	 */
	public Synchronizable getBean(Class<?> beanClass, String id) {		
		return dao.getBean(beanClass, id);
	}
	
	/**
	 * List all beans.
	 * 
	 * @param beanClass the bean class.
	 * @return list of beans.
	 */
	public List<Synchronizable> listBeans(Class<?> beanClass){
		return dao.listBeans(beanClass);
	}

	
	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(GenericHibernateDao dao) {
		this.dao = dao;
	}
	
	

}
