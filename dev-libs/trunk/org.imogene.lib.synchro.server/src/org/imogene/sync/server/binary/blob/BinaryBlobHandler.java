package org.imogene.sync.server.binary.blob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Binary;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.common.data.handler.EntityHandlerImpl;
import org.imogene.sync.SyncConstants;


/**
 * Implements a data handler for the Binary 
 * @author MEDES-IMPS
 */
public class BinaryBlobHandler extends EntityHandlerImpl implements EntityHandler {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.server.binary");
	
	private BinaryBlobHibernateDao dao;
	
	private DataHandlerManager dataHandlerManager;
	
	
	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		BinaryBlob entity = new BinaryBlob();
		entity.setId(id);
		entity.setModified(new Date());
		entity.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return entity;
	}
	
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {	
		getDao().saveOrUpdate(entity);
	}
	
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user, String terminalId) {
		return loadModified(date, user);
	}
	
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {	
		List<Synchronizable> result = new ArrayList<Synchronizable>();	
		List<Synchronizable> binaries = getDao().loadModified(date);	
		for(Synchronizable item:binaries) {
			Binary binary =  (Binary)item;
			EntityHandler entityHandler = (EntityHandler) dataHandlerManager.getHandler(binary.getParentEntity());
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
		BinaryBlob current = (BinaryBlob) entity;
		displayValue.append(current.getFileName());
		return displayValue.toString();
	}

	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(BinaryBlobHibernateDao dao) {
		this.dao = dao;
	}
	
	public void setDao(EntityDao dao){
		this.dao = (BinaryBlobHibernateDao)dao;
	}
	
	public BinaryBlobHibernateDao getDao() {
		return dao;
	}

	/**
	 * Setter for bean injection
	 * @param dataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager) {
		this.dataHandlerManager = dataHandlerManager;
	}

	protected ImogJunction createClientFilterJuntion(String userId, String terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder, SynchronizableUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	


	
	
	
	

}
