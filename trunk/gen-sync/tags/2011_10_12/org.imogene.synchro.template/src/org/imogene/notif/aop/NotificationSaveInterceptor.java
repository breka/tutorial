package org.imogene.notif.aop;

import java.lang.reflect.Method;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

import org.imogene.common.data.Synchronizable;

public class NotificationSaveInterceptor implements AfterReturningAdvice {
	
	private String notifierHost = "http://localhost:8080/DiabsatNotif";
	
	private Logger logger = Logger.getLogger("org.imogene.notif.aop");
	
	private ShortNameHelper shortNameHelper;

	/**
	 * Set the short names helper
	 * @param nameHelper the short name helper
	 */
	public void setShortNameHelper(ShortNameHelper nameHelper){
		shortNameHelper = nameHelper;
	}
	
	/**
	 * set the notifier URL
	 * @param notifier the notifier URL
	 */
	public void setNotifierUrl(String notifier){
		notifierHost = notifier;
	}
	
	/**
	 * Notify about the entity modification/creation
	 */	
	public void afterReturning(Object result, Method method, Object[] args,
			Object target) throws Throwable {	
		
		if(method.getName().startsWith("save"))
			handleSaveOrUpdate(args, target);				
	}
	
	
	/**
	 * Handle the saveOrUpdate method
	 * @param args arguments of the method
	 * @param target the entity target
	 */
	private void handleSaveOrUpdate(Object[] args, Object target){		
		
		/* handle the entity */
		Synchronizable bean = (Synchronizable)args[0];		
		final String id = bean.getId();
		final String operation="create";	
		final String type = shortNameHelper.getShortName(bean.getClass().getName());
		
		/* thread the notification process */
		new Thread(){
			@Override
			public void run() {
				sendToNotifier(type, operation, id);
			}			
		}.start();		
	}
	
	
	/**
	 * Notify the notifier by http.
	 * @param type the card type
	 * @param operation the operation on the card
	 * @param id the card id
	 */
	private void sendToNotifier(String type, String operation, String id){		
		HttpClient client = new HttpClient();
		String uri = notifierHost+"?"+"type="+type+"&op="+operation+"&id="+id;
		logger.debug("Notifier URI : "+uri);
		GetMethod method = new GetMethod(uri);
		try{
			client.executeMethod(method);
		}catch(Exception ex){			
			logger.debug(ex.getMessage());
		}
	}
	
	
}
