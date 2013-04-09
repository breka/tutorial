package org.imogene.common.data;

import java.util.HashMap;
import java.util.Map;


/**
 * Singleton to manage the entity information, 
 * @author MEDES-IMPS
 */
public class SynchronizableUtil {
	
	private static SynchronizableUtil instance = new SynchronizableUtil();
	
	/**
	 * key: entity short name
	 * value: entity class path
	 */
	public Map<String, String> entityClassReferences = new HashMap<String, String>();
	
	/**
	 * key: entity class name
	 * value: entity short path
	 */
	public Map<String, String> entityShortNameReferences = new HashMap<String, String>();
	
	
	/**
	 * Get the SynchronizableUtil instance
	 */
	public static SynchronizableUtil getInstance(){
		return instance;
	}


	/**
	 * Gets the entity class path from entity short name
	 * @param entityName entity short name
	 * @return entity class path
	 */
	public String getEntityPath(String shortname) {
		return entityClassReferences.get(shortname);	
	}
	
	/**
	 * Get the entity type 
	 * @param entityId
	 * @return
	 */
	public String getEntityShortName(String classname){
		return entityShortNameReferences.get(classname);
	}

	/**
	 * Setter for bean injection
	 * @param entityClassReferences
	 */
	public void setEntityClassReferences(Map<String, String> entityClassReferences) {
		this.entityClassReferences = entityClassReferences;
	}


	/**
	 * Setter for bean injection
	 * @param entityShortNameReferences
	 */
	public void setEntityShortNameReferences(Map<String, String> entityShortNameReferences) {
		this.entityShortNameReferences = entityShortNameReferences;
	}


	/**
	 * 
	 * @return
	 */
	public Map<String, String> getEntityClassReferences() {
		return entityClassReferences;
	}
	
	
	
	
	
	



}
