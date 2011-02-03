package org.imogene.common.data.handler;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.uao.security.ImogSecurityHandler;



/**
 * Abstract class for EntityHandler implementation
 * @author MEDES-IMPS
 */
public abstract class EntityHandlerImpl {
	
	public abstract EntityDao getDao();
	
	protected boolean hasNewClientFilter = false;
	
	
	/**
	 * Gets an entity whose fields are filtered depending on
	 * the user privileges 
	 * @param entityId the entity id
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return the entity, null if no entity has id
	 */
	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {
		
		if (user!=null)
			return ImogSecurityHandler.getInstance().getPolicy().toSecure(getDao().loadEntity(entityId), user);			
		else 
			return getDao().loadEntity(entityId);				
	}
	
	/**
	 * Gets a list of entities whose fields are filtered depending on
	 * the user privileges 
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return list of entities
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user) {	
		
		if (user!=null) {
			ImogConjunction conj = new ImogConjunction();
			conj.add(createFilterJuntion(user));
			return ImogSecurityHandler.getInstance().getPolicy().toSecure(getDao().loadEntities(conj), user);			
		}						
		else 
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
			ImogConjunction conj = new ImogConjunction();
			conj.add(createFilterJuntion(user));
			ImogJunction clientFilterJunction = createClientFilterJuntion(user.getLogin(),terminalId);
			if (clientFilterJunction!=null) {
				conj.add(clientFilterJunction);
				if (hasNewClientFilter) {
					hasNewClientFilter = false;
				}	
			}
			return ImogSecurityHandler.getInstance().getPolicy().toSecure(getDao().loadEntities(conj), user);			
		}						
		else 
			return getDao().loadEntities();
	}
	
	/**
	 * 
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @param conj search criterions
	 * @return list of entities
	 */
	private List<Synchronizable> loadEntities(SynchronizableUser user, ImogConjunction conj) {	
		
		if (user!=null)
			return ImogSecurityHandler.getInstance().getPolicy().toSecure(getDao().loadEntities(conj), user);								
		else 
			return getDao().loadEntities(conj);
	}

	/**
	 * Gets a list of entities that have been modified since a certain date
	 * @param date date since modifications are searched
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @return list of entities
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {
		
		if (user!=null) {
			ImogConjunction conj = new ImogConjunction();
			conj.add(createFilterJuntion(user));	
			List<Synchronizable> entities = getDao().loadModified(date, conj);
			List<Synchronizable> securedEntities = ImogSecurityHandler.getInstance().getPolicy().toSecure(entities, user);
			return securedEntities;			
		}		
		else 
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
			ImogConjunction conj = new ImogConjunction();
			conj.add(createFilterJuntion(user));	
			ImogJunction clientFilterJunction = createClientFilterJuntion(user.getLogin(),terminalId);					
			
			if (clientFilterJunction!=null) {
				conj.add(clientFilterJunction);
				/* if new client filter, send all cardentities, not only last modified ones */
				if (hasNewClientFilter) {
					hasNewClientFilter = false;
					return loadEntities(user, conj);
				}					
			}
				
			List<Synchronizable> entities = getDao().loadModified(date, conj);
			List<Synchronizable> securedEntities = ImogSecurityHandler.getInstance().getPolicy().toSecure(entities, user);
			return securedEntities;			
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
		
		if (user!=null) {
			ImogConjunction conj = new ImogConjunction();
			conj.add(createFilterJuntion(user));	
			Synchronizable entity = getDao().loadModified(date, conj, entityId);
			Synchronizable securedEntity = ImogSecurityHandler.getInstance().getPolicy().toSecure(entity, user);
			return securedEntity;			
		}		
		else 
			return getDao().loadModified(date, entityId);	
	}

	/**
	 * Saves or update an entity after fields are filtered depending on
	 * the user privileges 
	 * @param entity the entity to be saved
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 */
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {	
		
		if (user!=null) {
			Synchronizable toHibernate = ImogSecurityHandler.getInstance().getPolicy().toHibernate(entity, user);
			if (toHibernate!=null)
				getDao().saveOrUpdate(toHibernate);
		}					
		else 
			getDao().saveOrUpdate(entity);
	}
	
	/**
	 * Merge an entity after fields are filtered depending on
	 * the user privileges 
	 * @param entity the entity to be saved
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 */
	public void merge(Synchronizable entity, SynchronizableUser user) {	
		
		if (user!=null) {
			Synchronizable toHibernate = ImogSecurityHandler.getInstance().getPolicy().toHibernate(entity, user);
			if (toHibernate!=null)
				getDao().merge(toHibernate);
		}					
		else 
			getDao().merge(entity);
	}
	
	/**
	 * Deletes an entity
	 * @param entity the entity to be deleted
	 * @param user user current user whose access has to be filtered (if null, no filtering)
	 */
	public void delete(Synchronizable entity, SynchronizableUser user) {
		
		if (user!=null) {
			Synchronizable toHibernate = ImogSecurityHandler.getInstance().getPolicy().toHibernate(entity, user);
			if (toHibernate!=null)
				getDao().delete(toHibernate);
		}					
		else 
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
	 * Creates filtering criterias
	 * @param actor the current user
	 * @return Meedoo junction containing filtering criterias if the type of user
	 * has been assigned filtering criteria
	 */
	protected abstract ImogJunction createFilterJuntion(SynchronizableUser actor);
	
	/**
	 * Gets filtering criterias
	 * @param userId the login of the user whose filters are searched
	 * @param terminalId the id of the terminal for which filtering criterias are defined
	 * @return a list of ClientFilters
	 */
	protected abstract ImogJunction createClientFilterJuntion(String userId, String terminalId);
	





}
