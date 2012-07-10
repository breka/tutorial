package org.imogene.web.server.util;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Hibernate session request management. The session is opened at the beginning
 * of the request and flushed and closed at the end of it.
 * 
 * @author Medes-IMPS
 */
public class HibernateSessionFilter extends
		org.springframework.orm.hibernate3.support.OpenSessionInViewFilter {

	@Override
	protected Session getSession(SessionFactory sessionFactory)
			throws DataAccessResourceFailureException {
		Session session = super.getSession(sessionFactory);
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
					throw new CleanupFailureDataAccessException(
							"Failed to flush session before close: "
									+ e.getMessage(), e);
				} catch (Exception e) {
				}
			}
		} finally {
			super.closeSession(session, sessionFactory);
		}
	}

}
