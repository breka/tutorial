package org.imogene.sync.client.dao.sqlite;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.binary.BinaryFile;


/**
 * Implements a Hibernate DAO for the Binary 
 * @author MEDES-IMPS
 */
public class BinaryHibernateDao implements EntityDao {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(BinaryHibernateDao.class.getName());

	public List<Synchronizable> loadEntities() {		
		return HibernateGenericDao.loadEntities(BinaryFile.class);
	}

	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		return HibernateGenericDao.loadEntities(BinaryFile.class, criterions);
	}
	
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

	public Synchronizable loadEntity(String entityId) {
		return HibernateGenericDao.loadEntity(BinaryFile.class, entityId);
	}

	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date, criterions);
	}
	
	public List<Synchronizable> loadModified(Date date) {
		//TODO implementation not presents in the generic dao
		return null;
	}

	public Synchronizable loadModified(Date date, String entityId) {
		return HibernateGenericDao.loadModified(BinaryFile.class, date, entityId);
	}
	
	public Synchronizable loadModified(Date date, ImogJunction criterions, String entityId) {
		//TODO not implemented in the generic dao
		return null;
	}

/*	public List<Synchronizable> loadModified(String modifiedBy, Date date, ImogJunction criterions) {
		//logger.info("Loading all Binaries modified by '"+ modifiedBy + "' since '" + date + "'.");				
		Criteria crit = getSession().createCriteria(BinaryBlob.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge("modified", date));
		crit.add(Restrictions.eq("modifiedBy", modifiedBy));
		return crit.list();
	}
	
	public List<Synchronizable> loadModified(String modifiedBy, Date date) {
		//logger.info("Loading all Binaries modified by '"+ modifiedBy + "' since '" + date + "'.");				
		Criteria crit = getSession().createCriteria(BinaryBlob.class);
		crit.add(Restrictions.ge("modified", date));
		crit.add(Restrictions.eq("modifiedBy", modifiedBy));
		return crit.list();
	}*/

	public void saveOrUpdate(Synchronizable entity) {
		HibernateGenericDao.saveOrUpdate(entity);
	}

	public void merge(Synchronizable entity) {
		HibernateGenericDao.merge(entity);
	}

	public int countAll() {
		return HibernateGenericDao.countAll(BinaryFile.class);
	}

	public void delete(Synchronizable entity) {
		HibernateGenericDao.delete(entity);
	}

	public void deleteEntities() {
		//TODO local implementation
	}

	public void clear() {
		throw new RuntimeException("The method clear() in BinaryHibernateDao is not implemented");
	}

	public void flush() {
		throw new RuntimeException("The method flush() in BinaryHibernateDao is not implemented");
	}

}
