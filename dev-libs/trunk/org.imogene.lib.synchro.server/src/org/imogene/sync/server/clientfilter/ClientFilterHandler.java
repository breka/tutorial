package org.imogene.sync.server.clientfilter;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.uao.clientfilter.ClientFilter;

/**
 * Implements a data handler for the ClientFilter 
 * @author MEDES-IMPS
 */
public class ClientFilterHandler implements EntityHandler {

	private ClientFilterDao dao;

	
	
	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		ClientFilter entity = new ClientFilter();
		entity.setId(id);
		entity.setModified(new Date());
		entity.setCreatedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return entity;
	}
	
	/**
	 * Gets an entity whose fields are filtered depending on
	 * the user privileges 
	 * @param entityId the entity id
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return the entity, null if no entity has id
	 */
	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {		
		return getDao().loadEntity(entityId);				
	}
	
	/**
	 * Gets a list of entities whose fields are filtered depending on
	 * the user privileges 
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return list of entities
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user) {	
			return getDao().loadEntities();
	}
	
	/**
	 * Gets a list of entities whose fields are filtered depending on
	 * the user privileges and user defined clients filters
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @param terminalId Id of the terminal the current user is using
	 * @return list of entities
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user, String terminalId) {	
		
		if (user!=null) {
			ImogJunction clientFilterJunction = getClientFiltersJunction(user.getLogin(),terminalId);
			if (clientFilterJunction!=null)
				clientFilterJunction.add(clientFilterJunction);
			return getDao().loadEntities(clientFilterJunction);			
		}						
		else 
			return getDao().loadEntities();
	}

	/**
	 * Gets the Filter object for a given user and a given terminal
	 * @param userId the user Id
	 * @param terminalId the terminal Id
	 * @return 
	 */
	private ImogJunction getClientFiltersJunction(String userId, String terminalId) {
		ImogConjunction conj = new ImogConjunction();
		
		BasicCriteria userCriteria = new BasicCriteria();
		userCriteria.setOperation(CriteriaConstants.STRING_OPERATOR_EQUAL);
		userCriteria.setField("userId");
		userCriteria.setValue(userId);
		conj.add(userCriteria);
		
		BasicCriteria terminalCriteria = new BasicCriteria();
		terminalCriteria.setOperation(CriteriaConstants.STRING_OPERATOR_EQUAL);
		terminalCriteria.setField("terminalId");
		terminalCriteria.setValue(terminalId);
		conj.add(terminalCriteria);		
		
		return conj;
	}

	/**
	 * Gets a list of entities that have been modified since a certain date
	 * @param date date since modifications are searched
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return list of entities
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {
			return getDao().loadModified(date);	
	}
	
	/**
	 * Load the Entities modified since the specified date depending on
	 * the user privileges and user defined clients filters
	 * @param date the modification date
	 * @param user the user which is performing the data access (if null, no filtering)
	 * @param terminalId Id of the terminal the current user is using
	 * @return List of Entities modified since the specified date
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user, String terminalId) {
		
		if (user!=null) {
			ImogJunction clientFilterJunction = getClientFiltersJunction(user.getLogin(),terminalId);
			if (clientFilterJunction!=null)
				clientFilterJunction.add(clientFilterJunction);
			List<Synchronizable> entities = getDao().loadModified(date, clientFilterJunction);
			return entities;			
		}		
		else 
			return getDao().loadModified(date);	
	}
	
	/**
	 * Gets an entity that has been modified since a certain date
	 * @param entityId the entity id
	 * @param date date since modifications are searched
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return an entity
	 */
	public Synchronizable loadModified(String entityId, Date date, SynchronizableUser user) {
			return getDao().loadModified(date, entityId);	
	}

	/**
	 * Save an entity after fields are filtered depending on
	 * the user privileges 
	 * @param entity the entity to be saved
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 */	
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {
		getDao().saveOrUpdate(entity);
	}
	
	/**
	 * Merge an entity after fields are filtered depending on
	 * the user privileges 
	 * @param entity the entity to be saved
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 */
	public void merge(Synchronizable entity, SynchronizableUser user) {	
			getDao().merge(entity);
	}
	
	/**
	 * Deletes an entity
	 */
	public void delete(Synchronizable entity, SynchronizableUser user) {
		getDao().delete(entity);
	}

	/**
	 * Count all entities
	 * @return
	 */
	public int countAll() {
		return getDao().countAll();
	}

	/**
	 * 
	 */
	public void deleteEntities() {
		getDao().deleteEntities();		
	}	
	

	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(ClientFilterDao dao) {
		this.dao = dao;
	}

	public void setDao(EntityDao dao) {
		this.dao = (ClientFilterDao) dao;
	}

	public ClientFilterDao getDao() {
		return dao;
	}
	
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder, SynchronizableUser user) {
		// TODO Auto-generated method stub
		return null;
	}

}
