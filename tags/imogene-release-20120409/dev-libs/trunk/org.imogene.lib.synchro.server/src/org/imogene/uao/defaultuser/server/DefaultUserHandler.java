package org.imogene.uao.defaultuser.server;

import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandlerImpl;
import org.imogene.common.data.handler.UserHandler;
import org.imogene.sync.server.hibernate.DefaultUserHibernateDao;

/**
 * Implements a data handler for the DefaultUser 
 * @author MEDES-IMPS
 */
public class DefaultUserHandler extends EntityHandlerImpl implements UserHandler {
	
	private DefaultUserHibernateDao dao;
	
	
	public Synchronizable createNewEntity(String id) {
		return null;
	}
	

	/* (non-Javadoc)
	 * @see org.imogene.data.EntityHandlerImpl#createFilterJuntion(org.imogene.data.SynchronizableUser)
	 */
	protected ImogJunction createFilterJuntion(SynchronizableUser actor) {
		ImogConjunction filterConjunction = new ImogConjunction();
		return filterConjunction;
	}
	
	public List<SynchronizableUser> getUserFromLogin(String login) {
		return dao.getUserFromLogin(login);
	}

	public String toString(Synchronizable entity) {		
		return null;
	}
	
	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(DefaultUserHibernateDao dao) {
		this.dao = dao;
	}
	
	public void setDao(EntityDao dao){
		this.dao = (DefaultUserHibernateDao)dao;
	}

	public DefaultUserHibernateDao getDao() {
		return dao;
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
