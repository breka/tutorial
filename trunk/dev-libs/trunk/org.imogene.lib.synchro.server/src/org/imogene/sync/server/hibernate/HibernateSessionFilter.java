package org.imogene.sync.server.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * 
 * @author MEDES-IMPS
 */
public class HibernateSessionFilter extends
		org.springframework.orm.hibernate3.support.OpenSessionInViewFilter {
	
	@Override
	protected Session getSession(SessionFactory sessionFactory)
			throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		/* set the FlushMode to auto in order to save objects. */		
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}

	@Override
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		try {
			if (session != null && session.isOpen() && session.isConnected()) {
				try {
					session.flush();					
				} catch (HibernateException e) {
					logger.debug("Failed to flush session before close: "
							+ e.getMessage());
					throw new CleanupFailureDataAccessException(
							"Failed to flush session before close: "
									+ e.getMessage(), e);
				} catch (Exception e) {
					logger.debug("Failed to close session: "
							+ e.getMessage());
				}
			}
			else {
				logger.debug("Session not opened");
			}
				
		} finally {			
			super.closeSession(session, sessionFactory);
		}
	}

}
