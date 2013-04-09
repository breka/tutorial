package org.imogene.web.gwt.client.ui.form;

import java.util.HashSet;

/**
 * Permits to broadcast a message to 
 * the listeners when an entity have been added.
 * @author Medes-IMPS 
 */
public class RelationUpdateManager {

	private static RelationUpdateManager instance = new RelationUpdateManager();
	
	private HashSet<RelationBroadcastReceiver> receivers = new HashSet<RelationBroadcastReceiver>();
	
	/**
	 * Get the unique instance
	 * @return the unique instance
	 */
	public static RelationUpdateManager get(){
		return instance;
	}
	
	/**
	 * add a receiver
	 * @param reciever the receiver to add
	 */
	public void addRelationBroadcastReceiver(RelationBroadcastReceiver reciever){
		receivers.add(reciever);
	}
	
	/**
	 * Remove the specified receiver
	 * @param reciever the receiver to remove
	 */
	public void removeRelationBroadcastReceiver(RelationBroadcastReceiver reciever){
		receivers.remove(reciever);
	}
	
	/**
	 * Caller notify that a new entity have been added
	 * @param newEntity the entity added
	 */
	public void relationUdpate(Object newEntity){
		for(RelationBroadcastReceiver rec : receivers){
			if(rec!=null)
				rec.relationUpdate(newEntity);			
		}
	}
	
	/** INTERNAL CLASSES/INTERFACES  **/
	
	/**
	 * relation broadcast message receiver
	 */
	public interface RelationBroadcastReceiver {
						
		/**
		 * New entity have been created
		 * @param newEntity the newly created entity
		 */
		public void relationUpdate(Object newEntity);
	}
}
