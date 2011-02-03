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
import org.imogene.testsynchro.entity.Doctor;


public class DoctorDao implements EntityDao {		
		
	private static List<EntityListener> listeners = new Vector<EntityListener>();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#load(java.lang.String)
	 */
	public Synchronizable loadEntity(String doctorId) {
		return (Doctor)HibernateGenericDao.loadEntity(Doctor.class, doctorId);						
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#delete(java.lang.String)
	 */
	public void delete(String doctorId){
		Synchronizable entity = HibernateGenericDao.loadEntity(Doctor.class, doctorId);
		HibernateGenericDao.delete(entity);			
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#delete(org.imogene.data.Synchronizable)
	 */
	public void delete(Synchronizable toDelete){
		HibernateGenericDao.delete(toDelete);
	}			
	
	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		return HibernateGenericDao.loadModified(Doctor.class, date, criterions, entityId);
	}

	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date)
	 */
	public List<Synchronizable> loadModified(Date date) {	
		return HibernateGenericDao.loadModified(Doctor.class, date);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#deleteEntities()
	 */
	public void deleteEntities(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String hsql = "delete from Doctor";
	    Query query = session.createQuery(hsql);
	    query.executeUpdate();	
		tx.commit();
		session.close();		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#countAll()
	 */
	public int countAll(){
		return HibernateGenericDao.countAll(Doctor.class);				
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities(org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadEntities(ImogJunction junction){
		return HibernateGenericDao.loadEntities(Doctor.class, junction);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#loadEntities()
	 */
	public List<Synchronizable> loadEntities() {
		return HibernateGenericDao.loadEntities(Doctor.class);				
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

		return HibernateGenericDao.loadEntities(Doctor.class, junction,
				startRow, maxRows, sortProperty, sortOrder);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, String entityId){
		return HibernateGenericDao.loadModified(Doctor.class, date, entityId);			
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.lang.String, java.util.Date, org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadModified(String modifiedBy, Date date, ImogJunction criterions){
		return HibernateGenericDao.loadModified(Doctor.class, modifiedBy, date, criterions);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#loadEntities()
	 */
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		return HibernateGenericDao.loadModified(Doctor.class, date, criterions);		
	}			
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#merge(org.imogene.data.Synchronizable)
	 */
	public void merge(Synchronizable object){
		HibernateGenericDao.merge(object);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#clear()
	 */
	public void clear(){
		// Nothing to do with this implementation
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#flush()
	 */
	public void flush(){
		// Noting to do with this implementation
	}			
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Synchronizable object){
		HibernateGenericDao.saveOrUpdate(object);		
	}
	
	
	/**
	 * String representation of the specified entity
	 */
	public String toString(Object entity){
		Doctor doctor = (Doctor)entity;
		StringBuffer str = new StringBuffer();				
		str.append(doctor.getName()).append(" ");
		str.append(doctor.getFirstName()).append(", ");										
		return str.toString();
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
