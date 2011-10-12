package org.imogene.common.data.handler;

import java.util.HashMap;
import java.util.Map;


public class DataHandlerManager {
	
	private String packagePath;

	public Map<String, EntityHandler> handlers = new HashMap<String, EntityHandler>();
	
	public void setHandlers(Map<String, EntityHandler> pHandlers){
		handlers = pHandlers;
	}
	
	
	
	public Map<String, EntityHandler> getHandlers() {
		return handlers;
	}



	public EntityHandler getHandler(String className){
		if(className!=null && className.contains("."))
			return handlers.get(className);
		return handlers.get(packagePath+className);
	}



	public String getPackagePath() {
		return packagePath;
	}



	public void setPackagePath(String packagePath) {
		if(!packagePath.endsWith("."))
			packagePath=packagePath+".";
		this.packagePath = packagePath;
	}
	
	
}

