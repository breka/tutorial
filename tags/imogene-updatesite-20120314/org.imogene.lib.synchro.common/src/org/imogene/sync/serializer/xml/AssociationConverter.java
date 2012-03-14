package org.imogene.sync.serializer.xml;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;



/**
 * XStream converter for the entity association with cardinality 1.
 * @author MEDES-IMPS
 */
public class AssociationConverter implements Converter {

	
	private DataHandlerManager dataHandlerManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		
		String className = value.getClass().getName();
		writer.startNode(className);
		writer.addAttribute("id",((Synchronizable)value).getId().toString());
		writer.endNode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		reader.moveDown();
		String className = reader.getNodeName();
		String id = reader.getAttribute(0);
		reader.moveUp();
		
		EntityHandler handler = dataHandlerManager.getHandler(className);
		Synchronizable result = handler.getDao().loadEntity(id);
		if(result==null){
			result = handler.createNewEntity(id);
			handler.getDao().saveOrUpdate(result);
		}
		return result;				

	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class toConvert) {		
		return true;
	}

	/**
	 * setter for bean injection
	 * @param dataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager) {
		this.dataHandlerManager = dataHandlerManager;
	}
	
	

}
