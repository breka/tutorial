package org.imogene.sync.serializer.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * XStream converter for the entity associations.
 * @author MEDES-IMPS
 */
public class CollectionConverter implements Converter{
	
	private static String COLLECTION_NODE = "collection";
	
	private DataHandlerManager dataHandlerManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@SuppressWarnings("unchecked")
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

		writer.startNode(COLLECTION_NODE);
		for(Synchronizable rep:(Collection<Synchronizable>)value){			
			String className = rep.getClass().getName();
			writer.startNode(className);
			writer.addAttribute("id", rep.getId().toString());			
			writer.endNode();
		}
		writer.endNode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		reader.moveDown();

		Set<Synchronizable> entities = new HashSet<Synchronizable>();
		while (reader.hasMoreChildren()) {
			
			reader.moveDown();
			String className = reader.getNodeName();
			String id = reader.getAttribute(0);
			reader.moveUp();
	
			EntityHandler handler = dataHandlerManager.getHandler(className);
			Synchronizable linkedEntity = handler.getDao().loadEntity(id);
			if(linkedEntity == null){					
				linkedEntity = handler.createNewEntity(id);
				handler.getDao().saveOrUpdate(linkedEntity);
			}
			entities.add(linkedEntity);				
		}
		reader.moveUp();
		return entities;
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
