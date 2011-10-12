package org.imogene.notifier.server;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.imogene.common.entity.ImogActor;
import org.imogene.common.entity.ImogBean;
import org.imogene.common.entity.ImogRole;
import org.imogene.notifier.server.common.Notification;
import org.imogene.notifier.server.common.NotificationInstance;
import org.imogene.notifier.server.common.ShortNameHelper;
import org.imogene.notifier.server.dao.GenericDao;
import org.imogene.notifier.server.dao.ImogActorDao;
import org.imogene.notifier.server.dao.NotificationDao;


/**
 * factory for notification instances
 * @author Medes-IMPS
 */
public class NotificationFactory {
	
	private Logger logger = Logger.getLogger("org.imogene.notifier.server");
	
	private NotificationDao notifDao;
	private ImogActorDao actorDao;
	private GenericDao genDao;
	
	private ShortNameHelper shortNameHelper;
	
	
	public void setNotifDao(NotificationDao dao){
		notifDao = dao;
	}
	
	public void setActorDao(ImogActorDao dao){
		actorDao = dao;
	}
	
	public void setGenericDao(GenericDao dao){
		genDao = dao;
	}
	
	public void setShortNameHelper(ShortNameHelper nameHelper){
		shortNameHelper = nameHelper;
	}
	
	
	/**
	 * Create notification for the given parameters if it exists.
	 * @param type the entity type
	 * @param id the entity id
	 * @param op the entity operation
	 * @return All notification instances
	 */
	public Set<NotificationInstance> createNotification(String type, String id, String op){
		Set<NotificationInstance> instances = new HashSet<NotificationInstance>();
		logger.debug(instances.size()+" instance found.");
		for(Notification notif : notifDao.getNotification(type, op)){
			instances.add(createInstance(notif, id, type));
		}
		return instances;
	}
	
	
	/**
	 * Create a notification instance for the specified entity, based on the specified notification template
	 * @param template the notification template
	 * @param id the id of the entity
	 * @return the notification instance
	 */
	private NotificationInstance createInstance(Notification template, String id, String type){
		NotificationInstance instance = new NotificationInstance();
		instance.setId(template.getId());
		instance.setName(template.getName());
		instance.setTile(template.getTitle());
		instance.setMessage(createMessage(template, id, type));
		instance.setMethod(template.getMethod());
		instance.setUserRecipients(createRecipients(template, id));
		logger.debug("Instance created form the template "+template.getName());
		return instance;
	}
	
	
	/**
	 * create the messages based on the message template.
	 * @param template the notification template
	 * @param id the entity id
	 * @return the prepared message
	 */
	private String createMessage(Notification template, String id, String type){
		String className = shortNameHelper.getClassName(type);
		String message = template.getMessage();
		try{
			ImogBean bean = (ImogBean)genDao.load(Class.forName(className), id);
			message = template.getMessage().replace("${creator}", bean.getCreator());
		}catch(ClassNotFoundException cnfe){
			logger.error(cnfe.getMessage());			
		}		
		return message;
	}
	
	
	/**
	 * Create user recipient
	 * @param template the notification template
	 * @param id the entity id
	 * @return a set of all recipients
	 */
	private Set<ImogActor> createRecipients(Notification template, String id){
		Set<ImogActor> actors = new HashSet<ImogActor>();
		/* add single user */
		if(template.getUserRecipients()!=null){
			String[] userIds = template.getUserRecipients().split(";");
			for(String userId : userIds){
				ImogActor actor = actorDao.getActorFormId(userId);
				if(actor!=null){
					actors.add(actor);
					logger.debug("Actor with id '"+actor.getId()+"' added.");
				}
			}
		}
		/* add role users */
		if(template.getRoleRecipients()!=null){
			for(ImogRole role : template.getRoleRecipients()){
				actors.addAll(actorDao.getActorsForRole(role));									
			}
		}
		//TODO add user implied by the entity (ie: creator, user field ... ).
		logger.debug("Numbers of recipient added : "+actors.size());
		return actors;
	}
	
}
