package org.imogene.sync.client.binary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Binary;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.SynchronizableUtil;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.common.data.handler.EntityHandlerImpl;
import org.imogene.sync.SyncConstants;



/**
 * Implements a data handler for the Binary 
 * @author MEDES-IMPS
 */
public class BinaryFileHandler extends EntityHandlerImpl implements EntityHandler {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.server.binary");
	
	private BinaryFileHibernateDao dao;
	
	private DataHandlerManager dataHandlerManager;
	
	/**
	 * key: entity short name
	 * value: entity class path
	 */
	public Map<String, String> entityClassReferences = new HashMap<String, String>();
	
	
	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		BinaryFile entity = new BinaryFile();
		entity.setId(id);
		entity.setModified(new Date());
		entity.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return entity;
	}
	
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {	
		getDao().saveOrUpdate(entity);
	}
	
	public void merge(Synchronizable entity, SynchronizableUser user) {	
		getDao().merge(entity);
	}
	
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user, String terminalId) {
		return loadModified(date, user);
	}
	
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {	
		List<Synchronizable> result = new ArrayList<Synchronizable>();	
		List<Synchronizable> binaries = getDao().loadModified(date);	
		for(Synchronizable item:binaries) {
			Binary binary =  (Binary)item;
			logger.debug("Parent entity: "+binary.getParentEntity());
			EntityHandler entityHandler = (EntityHandler) dataHandlerManager.getHandler(SynchronizableUtil.getInstance().getEntityPath(binary.getParentEntity()));
			if (entityHandler!=null) {
				Synchronizable entity = entityHandler.loadModified(binary.getParentKey(), date, user);
				if (entity!=null) {
					
						try {
							Method getterMethod = entity.getClass().getMethod(binary.getParentFieldGetter(), (Class[])null); 
							String value = (String)getterMethod.invoke(entity, (Object[])null);
							if (value!=null)
								result.add(item);
						} catch (IllegalArgumentException e) {
							logger.error(e.getMessage());
						} catch (SecurityException e) {
							logger.error(e.getMessage());
						} catch (IllegalAccessException e) {
							logger.error(e.getMessage());
						} catch (InvocationTargetException e) {
							logger.error(e.getMessage());
						} catch (NoSuchMethodException e) {
							logger.error(e.getMessage());
						}
				}else{
					logger.debug("Parent entity not found");
				}
			}			
		}

		return result;
	}
	
	protected ImogJunction createFilterJuntion(SynchronizableUser user) {
		ImogConjunction filterConjunction = new ImogConjunction();
		return filterConjunction;
	}
	
	public String toString(Synchronizable entity) {
		StringBuffer displayValue = new StringBuffer();
		BinaryFile current = (BinaryFile) entity;
		displayValue.append(current.getFileName());
		return displayValue.toString();
	}

	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(BinaryFileHibernateDao dao) {
		this.dao = dao;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#setDao(org.imogene.dao.EntityDao)
	 */
	public void setDao(EntityDao dao){
		this.dao = (BinaryFileHibernateDao)dao;	
	}
	
	/**
	 * 
	 */
	public BinaryFileHibernateDao getDao() {
		return dao;
	}

	/**
	 * Setter for bean injection
	 * @param dataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager) {
		this.dataHandlerManager = dataHandlerManager;
	}

	/**
	 * Setter for bean injection
	 * @param entityClassReferences
	 */
	public void setEntityClassReferences(Map<String, String> entityClassReferences) {
		this.entityClassReferences = entityClassReferences;
	}
		
	/**
	 * Gets the entity class path from entity short name
	 * @param entityName entity short name
	 * @return entity class path
	 */
	@SuppressWarnings("unused")
	private String getEntityPath(String entityName) {
		return entityClassReferences.get(entityName);	
	}

	protected ImogJunction createClientFilterJuntion(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Synchronizable> loadEntities(int arg0, int arg1, String arg2,
			boolean arg3, SynchronizableUser arg4) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
	
	

}
