package org.imogene.sync.client.dao.sqlite;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.imogene.common.dao.criteria.HibernateDaoUtil;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;


public class HibernateGenericDao {

	private static Logger logger = Logger.getLogger("org.imogene.dao.sqlite.HibernateGenericDao");
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadEntities(Class type){
		Session session = SessionManager.getInstance().getSession();
		List<Synchronizable> result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			result = crit.list();
			tx.commit();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadEntities(Class type, ImogJunction criterions) {
		logger.info("Loading all entities of type " + type.getSimpleName());
		Session session = SessionManager.getInstance().getSession();
		List result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
			result = crit.list();
			tx.commit();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadEntities(Class type, ImogJunction criterions, String property, boolean asc) {
		logger.info("Loading all entities of type " + type.getSimpleName());
		Session session = SessionManager.getInstance().getSession();
		List result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			if (!asc)
				crit.addOrder(Property.forName(property).desc());
			else
				crit.addOrder(Property.forName(property).asc());
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
			result = crit.list();
			tx.commit();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadEntities(Class type, ImogJunction criterions, int startRow, int maxRows, String property, boolean asc) {
		logger.info("Loading all entities of type " + type.getSimpleName());
		Session session = SessionManager.getInstance().getSession();
		List result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			
			Criteria crit = session.createCriteria(type);
			crit.setFirstResult(startRow);
			crit.setMaxResults(maxRows);
			if (!asc)
				crit.addOrder(Property.forName(property).desc());
			else
				crit.addOrder(Property.forName(property).asc());
			if (criterions!=null)
				crit.add(HibernateDaoUtil.addImogJunction(criterions));		
			
			result = crit.list();
			tx.commit();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Synchronizable loadEntity(Class<? extends Synchronizable> type, String entityId) {		
		logger.debug("Loading the "+type.getSimpleName()+ " with id '" + entityId + "'.");
		List<? extends Synchronizable> resultList;
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.add(Restrictions.eq("id", entityId));
			resultList = crit.list();
			tx.commit();
		}
		if (resultList!=null && resultList.size()==1)
			return (Synchronizable)resultList.get(0);
		
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadModified(Class type, Date date, ImogJunction criterions) {
		logger.debug("Loading "+type.getSimpleName()+" modified since '"+ date.toString()+ "'.");
		Session session = SessionManager.getInstance().getSession();
		List result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			if (criterions != null)
				crit.add(HibernateDaoUtil.addImogJunction(criterions));
			crit.add(Restrictions.ge("modified", date));
			result = crit.list();
			tx.commit();
		}
		return result;
	}
	
	public static List<Synchronizable> loadModified(Class<?> type, Date date) {
		return loadModified(type,date, (ImogJunction)null);
	}
	
	public static Synchronizable loadModified(Class<?> type, Date date, String entityId) {
		return loadModified(type, date, null, entityId);
	}
	
	@SuppressWarnings("unchecked")
	public static Synchronizable loadModified(Class<?> type, Date date, ImogJunction criterions, String entityId) {
		logger.debug("Loading "+type.getSimpleName()+" with id='" + entityId + "' if modified since '"+ date.toString()+ "'.");
		List resultList;		
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.add(Restrictions.eq("id", entityId));
			crit.add(Restrictions.ge("modified", date));
			if (criterions != null)
				crit.add(HibernateDaoUtil.addImogJunction(criterions));
			resultList = crit.list();
			tx.commit();
		}
		if (resultList!=null && resultList.size()==1)
			return (Synchronizable)resultList.get(0);
		
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public static List<Synchronizable> loadModified(Class<?> type, String modifiedBy, Date date, ImogJunction criterions) {
		logger.debug("Loading all "+type.getSimpleName()+" modified by '"+ modifiedBy + "' since '" + date + "'.");
		List result;
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
			crit.add(Restrictions.ge("modified", date));
			crit.add(Restrictions.eq("modifiedBy", modifiedBy));
			result = crit.list();
			tx.commit();
		}
		return result;
	}

	public static  void saveOrUpdate(Synchronizable entity) {		
		logger.debug("Saving or updating "+entity.getClass().getSimpleName()+" with id '" + entity.getId() + "'.");	
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(entity);
			tx.commit();
		}
	}
	
	public static  void merge(Synchronizable entity) {		
		logger.debug("Merging "+entity.getClass().getSimpleName()+" with id '" + entity.getId() + "'.");		
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			session.merge(entity);
			tx.commit();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static int countAll(Class<?> type) {
		Session session = SessionManager.getInstance().getSession();
		List list;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.setProjection(Projections.rowCount());
			list = crit.list();
			tx.commit();
		}
		return Integer.parseInt(list.get(0).toString());
	}
	
	@SuppressWarnings("unchecked")
	public static int countAll(Class<? extends Synchronizable> type, ImogJunction criterions) {
		Session session = SessionManager.getInstance().getSession();
		List list;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(type);
			crit.add(HibernateDaoUtil.addImogJunction(criterions));
			crit.setProjection(Projections.rowCount());
			list = crit.list();
			tx.commit();
		}
		return Integer.parseInt(list.get(0).toString());
	}
	
	public static void delete(Synchronizable entity) {
		logger.debug("Deleting "+entity.getClass().getSimpleName()+" with id '" + entity.getId() + "'.");
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			session.delete(entity);
			tx.commit();
		}
	}	
	
	public static void refresh(Synchronizable entity){
		Session session = SessionManager.getInstance().getSession();
		synchronized(session){
			session.refresh(entity);
		}
		
	}
	
}
