package org.imogene.rcp.core.view;


/**
 * Class representing entity extension point information
 * @author MEDES-IMPS
 */
public class EntityInfo {
	
	private String className;
	private String name;
	private String iconName;
	private String entityId;

	public EntityInfo(String className, String name, String iconName, String entityId) {
		super();
		this.className = className;
		this.name = name;
		this.iconName = iconName;
		this.entityId = entityId;
	}
	
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

