package org.imogene.testsynchro.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.EntityListener;
import org.imogene.sync.client.HibernateUtil;
import org.imogene.sync.client.dao.sqlite.HibernateGenericDao;
import org.imogene.testsynchro.entity.MedicalCenter;


public class MedicalCenterDao implements EntityDao {
		
	private List<EntityListener> listeners = new Vector<EntityListener>();
	
	public MedicalCenterDao(){
		
	}	

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#clear()
	 */
	public void clear() {		
		// No need to be implemented here
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#countAll()
	 */
	public int countAll() {
		return HibernateGenericDao.countAll(MedicalCenter.class);			
	}

	/*
	 * 
	 */
	public void delete(String entityId) {		
		delete(loadEntity(entityId));
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#delete(org.imogene.data.Synchronizable)
	 */
	public void delete(Synchronizable entity){		
		HibernateGenericDao.delete(entity);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#deleteEntities()
	 */
	public void deleteEntities() {		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String hsql = "delete from MedicalCenter";
	    Query query = session.createQuery(hsql);
	    query.executeUpdate();	
		tx.commit();
		session.close();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#flush()
	 */
	public void flush() {		
		//no need to be implemented here
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities()
	 */
	public List<Synchronizable> loadEntities() {		
		return HibernateGenericDao.loadEntities(MedicalCenter.class);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities(org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		return HibernateGenericDao.loadEntities(MedicalCenter.class, criterions);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.common.dao.EntityDao#loadEntities(int, int, java.lang.String, boolean)
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder) {

		/* remove entities created by system */
		ImogJunction junction = new ImogConjunction();
		BasicCriteria criteria = new BasicCriteria();
		criteria.setOperation(CriteriaConstants.STRING_OPERATOR_DIFF);
		criteria.setField("createdBy");
		criteria.setValue(SyncConstants.SYNC_ID_SYS);
		junction.add(criteria);

		return HibernateGenericDao.loadEntities(MedicalCenter.class, junction,
				startRow, maxRows, sortProperty, sortOrder);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntity(java.lang.String)
	 */
	public Synchronizable loadEntity(String entityId) {
		return HibernateGenericDao.loadEntity(MedicalCenter.class, entityId);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {			
		return HibernateGenericDao.loadModified(MedicalCenter.class, date, criterions);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, String entityId) {
		return HibernateGenericDao.loadModified(MedicalCenter.class, date, entityId);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.lang.String, java.util.Date, org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadModified(String modifiedBy, Date date,
			ImogJunction criterions) {
		return HibernateGenericDao.loadModified(MedicalCenter.class, modifiedBy, date, criterions);		
	}		

	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		return HibernateGenericDao.loadModified(MedicalCenter.class, date, criterions, entityId);
	}

	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date)
	 */
	public List<Synchronizable> loadModified(Date date) {
		
		return HibernateGenericDao.loadModified(MedicalCenter.class, date);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#merge(org.imogene.data.Synchronizable)
	 */
	public void merge(Synchronizable entity) {		
		HibernateGenericDao.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#saveOrUpdate(org.imogene.data.Synchronizable)
	 */
	public void saveOrUpdate(Synchronizable entity) {			
		HibernateGenericDao.saveOrUpdate(entity);		
	}	
	
	/**
	 * add a listener notified when data are saved
	 * @param listener the listener to add
	 */
	public synchronized void addListener(EntityListener listener){
		listeners.add(listener);
	}
	
	/**
	 * remove a listener
	 * @param listener the listener to remove
	 */
	public synchronized void removeListener(EntityListener listener){
		listeners.remove(listener);
	}
}
