package org.imogene.sync.server.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.imogene.common.dao.UserDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.uao.defaultuser.DefaultUser;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implements a DAO for the DefaultUser 
 * @author MEDES-IMPS
 */
public class DefaultUserHibernateDao extends HibernateDaoSupport implements UserDao {
	
	private static final String LOGIN_PROPERTY="login";
	
	private Logger logVerbose = Logger.getLogger("org.imogene.sync.server.hibernate");
	
	public Synchronizable createNewEntity(String id) {
		return null;
	}

	public List<Synchronizable> loadEntities() {
		return null;
	}
	
	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		return null;
	}	
	
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	public Synchronizable loadEntity(String entityId) {
		logVerbose.debug("Loading the DefaultUser");
		Criteria crit = getSession().createCriteria(DefaultUser.class);
		crit.add(Restrictions.eq(LOGIN_PROPERTY, entityId));
		List<?> resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (DefaultUser) resultList.get(0);

		return null;
	}

	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		return null;
	}
	
	public List<Synchronizable> loadModified(Date date) {
		return null;
	}
	
	public Synchronizable loadModified(Date date, String entityId) {
		return null;
	}

	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		return null;
	}

	public void saveOrUpdate(Synchronizable entity) {		
	}

	public void deleteEntities() {	
		getHibernateTemplate().deleteAll(loadEntities());	
	}
	
	@SuppressWarnings("unchecked")
	public List<SynchronizableUser> getUserFromLogin(String login) {
		Criteria crit = getSession().createCriteria(DefaultUser.class);
		crit.add(Restrictions.eq(LOGIN_PROPERTY, login));
		return crit.list();
	}

	public String toString(Synchronizable entity) {		
		return null;
	}

	public void clear() {
	}

	public void flush() {
	}

	public void merge(Synchronizable entity) {
	}

	public int countAll() {
		return 0;
	}

	public void delete(Synchronizable entity) {		
	}
	
}
