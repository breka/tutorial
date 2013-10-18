package org.imogene.sync.server.binary.file;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.HibernateDaoUtil;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implements a Hibernate DAO for the Binary 
 * @author Medes-IMPS
 */
public class BinaryFileHibernateDao extends HibernateDaoSupport implements EntityDao {
	
	private Logger logVerbose = Logger.getLogger("org.imogene.sync.server.hibernate");
	
	private final static String UPLOAD_DATE_PROPERTY = "uploadDate";
	
	private final static String ID_PROPERTY = "id";
	
	private final static String HSQL_REQ_DELETE = "delete from Binaries";
	

	/**
	 * 
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadEntities()
	 */
	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadEntities() {
		logVerbose.debug("Loading all Binaries");			
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		return crit.list();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadEntities(org.imogene.common.dao.criteria.ImogJunction)
	 */
	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		logVerbose.debug("Loading all Binaries");			
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		return crit.list();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadEntity(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Synchronizable loadEntity(String entityId) {
		logVerbose.debug("Loading the Binary with id '" + entityId + "'.");		
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.add(Restrictions.eq(ID_PROPERTY, entityId));
		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (BinaryFile) resultList.get(0);

		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadModified(java.util.Date, org.imogene.common.dao.criteria.ImogJunction)
	 */
	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		logVerbose.debug("Loading Binaries modified since '"+ date.toString()+ "'.");		
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge(UPLOAD_DATE_PROPERTY, date));
		return crit.list();
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadModified(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadModified(Date date) {
		logVerbose.debug("Loading Binaries modified since '"+ date.toString()+ "'.");		
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.add(Restrictions.ge(UPLOAD_DATE_PROPERTY, date));
		return crit.list();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadModified(java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Synchronizable loadModified(Date date, String entityId) {
		logVerbose.debug("Loading Binary with id='" + entityId + "' if modified since '"+ date.toString()+ "'.");	
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.add(Restrictions.eq(ID_PROPERTY, entityId));
		crit.add(Restrictions.ge(UPLOAD_DATE_PROPERTY, date));

		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (BinaryFile) resultList.get(0);
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadModified(java.util.Date, org.imogene.common.dao.criteria.ImogJunction, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Synchronizable loadModified(Date date, ImogJunction criterions, String entityId) {
		logVerbose.debug("Loading the Binary with id '"+ entityId + "' modified since '" + date + "'.");				
		Criteria crit = getSession().createCriteria(BinaryFile.class);	
		crit.add(HibernateDaoUtil.addImogJunction(criterions));	
		crit.add(Restrictions.ge(UPLOAD_DATE_PROPERTY, date));
		crit.add(Restrictions.eq(ID_PROPERTY, entityId));
		
		List resultList = crit.list();
		if (resultList!=null && resultList.size()==1)
			return (BinaryFile)resultList.get(0);		
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#saveOrUpdate(org.imogene.common.data.Synchronizable)
	 */
	public void saveOrUpdate(Synchronizable entity) {
		logVerbose.debug("Saving or updating Binary with id '" + entity.getId() + "'.");		
		getSession().beginTransaction();
		getHibernateTemplate().saveOrUpdate(entity);
		getSession().getTransaction().commit();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#merge(org.imogene.common.data.Synchronizable)
	 */
	public void merge(Synchronizable entity) {
		logVerbose.debug("Merging Binary with id '" + entity.getId() + "'.");		
		getSession().beginTransaction();
		getHibernateTemplate().merge(entity);
		getSession().getTransaction().commit();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#countAll()
	 */
	@SuppressWarnings("unchecked")
	public int countAll() {
		Criteria crit = getSession().createCriteria(BinaryFile.class);
		crit.setProjection(Projections.rowCount());
		List list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#delete(org.imogene.common.data.Synchronizable)
	 */
	public void delete(Synchronizable entity) {
		logVerbose.debug("Deleting Binary with id '" + entity.getId() + "'.");
		getHibernateTemplate().delete(entity);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#deleteEntities()
	 */
	public void deleteEntities() {
		logVerbose.debug("Deleting Binaries");		
		Query query = getSession().createQuery(HSQL_REQ_DELETE);
		query.executeUpdate();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#clear()
	 */
	public void clear() {
		getHibernateTemplate().clear();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#flush()
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

}