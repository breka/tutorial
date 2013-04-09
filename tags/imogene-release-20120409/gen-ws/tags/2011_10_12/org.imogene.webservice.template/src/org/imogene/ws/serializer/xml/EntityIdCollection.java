package org.imogene.ws.serializer.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;


/**
 * Class that contains a list of enitity ids
 * Used in jaxb adapters for xml serialization
 * @author MEDES-IMPS
 */
public class EntityIdCollection {
	
	private List<String> idList;
	
	
	public EntityIdCollection() {
	}
	
	@XmlElement(name="id")
	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
}
