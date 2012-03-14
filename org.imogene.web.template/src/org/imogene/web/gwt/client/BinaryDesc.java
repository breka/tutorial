package org.imogene.web.gwt.client;

/**
 * Class that describes an available binary entity
 * @author Medes-IMPS
 */
public class BinaryDesc {
	
	private String id;
	
	private String name = "-";
	
	private int size = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
		
}
