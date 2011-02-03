package org.imogene.sync.server.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.imogene.sync.server.session.SyncSession;
import org.imogene.sync.server.session.SyncSessionDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * sync session DAO implementation using Hibernate.
 * 
 * @author Medes-IMPS
 */
public class SyncSessionHibernateDao extends HibernateDaoSupport implements SyncSessionDao {
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.session.SyncSessionDao#saveOrUpdate(org.imogene.sync.server.session.SyncSession)
	 */
	public void saveOrUpdate(SyncSession session){
		getHibernateTemplate().saveOrUpdate(session);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.session.SyncSessionDao#loadSyncSession(java.lang.String)
	 */
	public SyncSession load(String sessionId){
		return (SyncSession)getHibernateTemplate().load(SyncSession.class, sessionId);
	}
	
	/*
	 * 
	 */
	public void delete(SyncSession history) {
		getHibernateTemplate().delete(history);
	}

	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void clearTerminated(){
		Criteria crit = getSession().createCriteria(SyncSession.class);		
		crit.add(Restrictions.isNull("sendDate"));		
		List<SyncSession> result = crit.list();
		for(SyncSession session : result)
			delete(session);
	}	
	
	public void createSyncSession(){
		
	}
	
	public boolean isValid(String sessionId){
		if(load(sessionId)!=null)
			return true;
		return false;
	}
	
}