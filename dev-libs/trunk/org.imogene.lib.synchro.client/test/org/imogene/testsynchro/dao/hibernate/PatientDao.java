package org.imogene.testsynchro.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.DateHelper;
import org.imogene.sync.client.EntityListener;
import org.imogene.sync.client.dao.sqlite.HibernateGenericDao;
import org.imogene.testsynchro.entity.Patient;


public class PatientDao implements EntityDao{		
		
	private static List<EntityListener> listeners = new Vector<EntityListener>();
	
	private final Class<Patient> type = Patient.class;
	
	public PatientDao(){
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntity(java.lang.String)
	 */
	public Synchronizable loadEntity(String patientId) {
		return HibernateGenericDao.loadEntity(type, patientId);
	}
	
	
	/**
	 * Delete the patient with the specified id
	 * @param patientId the patient id
	 */
	public void delete(String patientId){
		delete(loadEntity(patientId));
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
	public void deleteEntities(){
		throw new RuntimeException("'deleteEntities' not implemented in class"+getClass().getSimpleName());
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#countAll()
	 */
	public int countAll(){
		return HibernateGenericDao.countAll(type);
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#loadEntities()
	 */
	public List<Synchronizable> loadEntities() {
		return HibernateGenericDao.loadEntities(type);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities(org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadEntities(ImogJunction criterions){
		return HibernateGenericDao.loadEntities(type, criterions);
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

		return HibernateGenericDao.loadEntities(Patient.class, junction,
				startRow, maxRows, sortProperty, sortOrder);
	}	
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#loadEntities()
	 */
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		return HibernateGenericDao.loadModified(type, date, criterions);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.lang.String, java.util.Date, org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadModified(String modifiedBy, Date date, ImogJunction criterions){
		return HibernateGenericDao.loadModified(type, modifiedBy, date, criterions);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, java.lang.String)
	 */
	public Synchronizable loadModified(Date date,String entityId){
		return HibernateGenericDao.loadModified(type, date, entityId);
	}
	
	
	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		return HibernateGenericDao.loadModified(Patient.class, date, criterions, entityId);
	}


	/* (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date)
	 */
	public List<Synchronizable> loadModified(Date date) {		
		return HibernateGenericDao.loadModified(Patient.class, date);
	}


	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#merge(org.imogene.data.Synchronizable)
	 */
	public void merge(Synchronizable patient){
		HibernateGenericDao.merge(patient);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.EntityHandler#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Synchronizable entity){
		HibernateGenericDao.saveOrUpdate(entity);		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#flush()
	 */
	public void flush(){
		throw new RuntimeException("'flush()' not implemented in the class "+getClass().getSimpleName());
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#clear()
	 */
	public void clear(){
		throw new RuntimeException("'clear()' not implemented in the class "+getClass().getSimpleName());
	}
	
	/**
	 * String representation of the specified patient
	 */
	public String toString(Object entity){
		Patient patient = (Patient)entity;
		StringBuffer str = new StringBuffer();				
		str.append(patient.getName()).append(" ");
		str.append(patient.getFirstName()).append(", ");		
		if(patient.getBirthday()!=null){
			str.append("born the ");			
			str.append(DateHelper.toString(patient.getBirthday()));
		}
							
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
