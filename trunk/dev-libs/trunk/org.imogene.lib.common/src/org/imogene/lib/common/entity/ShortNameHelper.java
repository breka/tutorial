package org.imogene.lib.common.entity;

import java.util.Set;

public interface ShortNameHelper {

	public String getClassName(String shortName);

	public String getShortName(String className);

	public String getLabelName(String shortName);

	public Set<String> getAllShortNames();
}