package org.imogene.sync.client.binary;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.dao.sqlite.HibernateGenericDao;


/**
 * Implements a Hibernate DAO for the Binary 
 * @author MEDES-IMPS
 */
public class BinaryFileHibernateDao implements EntityDao {	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities()
	 */
	public List<Synchronizable> loadEntities() {
		return HibernateGenericDao.loadEntities(BinaryFile.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntities(org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadEntities(ImogJunction criterions) {
				
		return HibernateGenericDao.loadEntities(BinaryFile.class, criterions);		
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

		return HibernateGenericDao.loadEntities(BinaryFile.class, junction,
				startRow, maxRows, sortProperty, sortOrder);
	}	

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadEntity(java.lang.String)
	 */
	public Synchronizable loadEntity(String entityId) {
		return HibernateGenericDao.loadEntity(BinaryFile.class, entityId);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction)
	 */
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date, criterions);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date)
	 */
	public List<Synchronizable> loadModified(Date date) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, String entityId) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date, entityId);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#loadModified(java.util.Date, org.imogene.dao.criteria.ImogJunction, java.lang.String)
	 */
	public Synchronizable loadModified(Date date, ImogJunction criterions, String entityId) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date, criterions, entityId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#saveOrUpdate(org.imogene.data.Synchronizable)
	 */
	public void saveOrUpdate(Synchronizable entity) {
		HibernateGenericDao.saveOrUpdate(entity);			
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
	 * @see org.imogene.dao.EntityDao#countAll()
	 */
	public int countAll() {
		return HibernateGenericDao.countAll(BinaryFile.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#delete(org.imogene.data.Synchronizable)
	 */
	public void delete(Synchronizable entity) {

    	// Delete attached file
    	BinaryFile binary = (BinaryFile)entity;
    	File attachedFile = new File(BinaryFileManager.getInstance().buildFilePath(binary.getId(), binary.getFileName()));
    	attachedFile.delete();
    	
    	// Delete binary bean
    	HibernateGenericDao.delete(entity);   		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#deleteEntities()
	 */
	public void deleteEntities() {
		//TODO
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#clear()
	 */
	public void clear() {
		throw new RuntimeException("Not implemented ");
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.EntityDao#flush()
	 */
	public void flush() {
		throw new RuntimeException("Not implemented ");
	}

}
