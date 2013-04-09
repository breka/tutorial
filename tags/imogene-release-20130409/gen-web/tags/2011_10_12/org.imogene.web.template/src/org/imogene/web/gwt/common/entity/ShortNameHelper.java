package org.imogene.web.gwt.common.entity;

import java.util.Set;

public interface ShortNameHelper {

	public String getClassName(String shortName);
	
	public String getShortName(String className);
	
	public Set<String> getAllShortNames();
	
	public String getLabelName(String shortName);
}
