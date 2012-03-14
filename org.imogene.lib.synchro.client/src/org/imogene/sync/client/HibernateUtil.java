package org.imogene.sync.client;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static Logger logger = Logger.getLogger("org.imogene.HibernateUtil");

	private static SessionFactory sessionFactory;
	
	public ThreadLocal<Session> sessions = new ThreadLocal<Session>(); 
	
	static{
		try{
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession(){
		if(sessions.get()==null)
			sessions.set(sessionFactory.openSession());
		return sessions.get();
	}
	
	public void closeSession(){
		if(sessions.get()!=null){
			sessions.get().flush();
			sessions.get().close();
		}
	}
	
	public static void shutdown(){
		getSessionFactory().close();
	}
}
