package org.imogene.sync.client.dao.sqlite;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {
	
	private Logger logger = Logger.getLogger("org.imogene.dao.sqlite.SessionManager");

	private static SessionManager instance; 
	
	private static Configuration config = null;
	
	private SessionFactory factory;
	
	private Session session;
	
	//private ThreadLocal<Session> sessions = new ThreadLocal<Session>(); 
	
	private SessionManager(){
		logger.debug("Creating the session manager.");
		try{
			if(config == null){
				logger.debug("The configuration is null, so we looke for one in the classpath.");
				config = new Configuration().configure();
			}
			session = config.buildSessionFactory().openSession();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public static SessionManager getInstance(){
		if(instance == null)
			instance = new SessionManager();
		return instance;
	}
	
	public Session getSession(){
		/*if(sessions.get()==null)
			sessions.set(factory.openSession());
		return sessions.get();*/
		return session;
	}
	
	public void closeSession(){
		/*if(sessions.get()!=null){
			sessions.get().flush();
			sessions.get().close();
		}*/
	}
	
	public void shutdown(){
		if(factory!=null)
			factory.close();
	}
	
	public static void setConfiguration(Configuration pConfig){
		config = pConfig;
	}
}
