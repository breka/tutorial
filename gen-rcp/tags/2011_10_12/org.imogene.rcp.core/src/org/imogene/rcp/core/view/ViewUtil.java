package org.imogene.rcp.core.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;



public class ViewUtil {
	
	public final static String DEFAULT_TEMA = "default";
	
	
	/**
	 * Creates a sorted HashMap with key=tema name and value= list of EntityInfo 
	 * objects reprensenting the entities belonging to the tema
	 * @return
	 */
	public static LinkedHashMap<String, List<EntityInfo>> assignEntitiesToTemas() {
		
		LinkedHashMap<String, List<EntityInfo>> temaContent = new LinkedHashMap<String, List<EntityInfo>>();
		
		/* parse Entity extensions */	
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint entity = registry.getExtensionPoint("org.imogene.rcp.core", "Entity");
		IExtension[] entities = entity.getExtensions();
		
		for (IExtension extension : entities) {
			IConfigurationElement conf = extension.getConfigurationElements()[0];
			
			/* get entity information */
			String entityId = conf.getAttribute("id");
			String className = conf.getAttribute("className");
			String entityName = conf.getAttribute("name");
			String iconName = conf.getAttribute("iconName");
			EntityInfo entityObj = new EntityInfo(className, entityName, iconName, entityId);
					
			boolean inMenu = Boolean.valueOf(conf.getAttribute("inMenu")).booleanValue();
			String temaName = conf.getAttribute("thema");
			
			/* assign entities to temas */
			if (inMenu) {
				if (temaName!=null && temaName.length()>0)
					addEntityObjToTema(temaContent, temaName, entityObj);
				else 
					addEntityObjToTema(temaContent, DEFAULT_TEMA, entityObj);				
			}

		}
		return temaContent;
	}

	
	/**
	 * Dispatch entities into temas
	 * @param temaContent HashMap of the temas
	 * @param tema the tema of the entity
	 * @param entityObj the entity information
	 */
	private static void addEntityObjToTema (LinkedHashMap<String, List<EntityInfo>> temaContent, String tema, EntityInfo entityObj) {
		
		if (temaContent.containsKey(tema)) {
			List<EntityInfo> entityList = temaContent.get(tema);
			entityList.add(entityObj);				
		}
		else {
			List<EntityInfo> entityList = new ArrayList<EntityInfo>();
			entityList.add(entityObj);
			temaContent.put(tema, entityList);		
		}
	}
	
	
}
