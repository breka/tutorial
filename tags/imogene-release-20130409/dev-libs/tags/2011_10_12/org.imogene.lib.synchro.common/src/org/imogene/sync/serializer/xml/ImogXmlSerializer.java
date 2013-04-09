package org.imogene.sync.serializer.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.ImogSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;


/**
 * Static class that helps to 
 * the bean XML serialization.
 * @author MEDES-IMPS
 */
public class ImogXmlSerializer implements ImogSerializer {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.serializer.xml");
	
	private XStream xstream;
	
	private DataHandlerManager dataHandlerManager;
	
	public ImogXmlSerializer(){
		xstream = new XStream(new DomDriver("UTF-8"));
		xstream.registerConverter(new UTCDateConverter());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);	
	}	
	
	public ImogXmlSerializer(ClassLoader loader){
		this();
		xstream.setClassLoader(loader);
	}
	
	/**
	 * Serialize a synchronizable entity.
	 * @param entity the entity to serialize.
	 * @return the string xml representation of the entity.
	 */
	public void serialize(Synchronizable entity, OutputStream xml) throws ImogSerializationException {	
		if (!entity.getCreatedBy().equals(SyncConstants.SYNC_ID_SYS)) {
			xstream.toXML(entity, xml);		
			logger.debug(xstream.toXML(entity));			
		}
	}		
	
	/**
	 * serialize a list of objects, and write the 
	 * result in the specified output stream.
	 * @param entities List of the entities to serialize
	 * @param xml output stream where to write the result.
	 * @throws IOException If an error occured writing to the stream.
	 */
	public void serialize(List<Synchronizable> entities, OutputStream xml) throws ImogSerializationException {
		
		try {
			xml.write("<entities>".getBytes());
			for(Synchronizable entity:entities){
				if (!entity.getCreatedBy().equals(SyncConstants.SYNC_ID_SYS)) {
					xstream.toXML(entity,xml);
					//logger.debug(xstream.toXML(entity));
				}
			}
			xml.write("</entities>".getBytes());			
		}
		catch (Exception e){
			throw new ImogSerializationException(e);
		}		
	}
	
	/**
	 * Unserialize an object represented by 
	 * the specified xml stream.
	 * @param xml the xml stream
	 * @return the object
	 */
	public Synchronizable deSerialize(InputStream xml) throws ImogSerializationException {
		Synchronizable result =(Synchronizable) xstream.fromXML(xml);		
		return result;
	}
	
	/**
	 * Unserialize severals entity enclosed 
	 * in an xml document as string.
	 * @param xml the string xml document
	 * @return the list of the object unserialized
	 */
	public List<Synchronizable> deSerializeMulti(InputStream xml) throws ImogSerializationException {
		List<Synchronizable> list = new Vector<Synchronizable>();
		
		try {
			/* create a document from the xml string */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = factory.newDocumentBuilder();
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");			
			Document dom = domBuilder.parse(xml);
			
			/* each parent node is an entity, 
			   we export it in a temporary file */
			NodeList entities = dom.getDocumentElement().getChildNodes();
			for(int i=0; i<entities.getLength(); i++){
				Node current = entities.item(i);				
				File tempFile = File.createTempFile("receive", ".imogobject");
				StreamResult result = new StreamResult(new FileOutputStream(tempFile));
				DOMSource source = new DOMSource(current);
				trans.transform(source, result);					
				list.add(deSerialize(new FileInputStream(tempFile)));
				tempFile.delete();
			}

		}catch (Exception ex) {
			throw new ImogSerializationException(ex);
		}
		return list;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.serializer.ImogSerializer#processMulti(java.io.InputStream, org.imogene.common.data.SynchronizableUser)
	 */
	public int processMulti(InputStream xml, SynchronizableUser user) throws ImogSerializationException {
		int j=0;
		try {
			/* create a document from the xml string */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = factory.newDocumentBuilder();
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");			
			Document dom = domBuilder.parse(xml);
			
			/* each parent node is an entity, 
			   we export it in a temporary file */
			NodeList entities = dom.getDocumentElement().getChildNodes();
			for(int i=0; i<entities.getLength(); i++){
				Node current = entities.item(i);				
				File tempFile = File.createTempFile("receive", ".imogobject");
				StreamResult result = new StreamResult(new FileOutputStream(tempFile));
				DOMSource source = new DOMSource(current);
				trans.transform(source, result);	
				
				// unserialize entity
				Synchronizable entity = deSerialize(new FileInputStream(tempFile));
				tempFile.delete();
				
				// save entity
				if (entity != null) {
					EntityHandler handler = dataHandlerManager.getHandler(entity.getClass().getName());
					Synchronizable exist = handler.getDao().loadEntity(entity.getId());
					//TODO implement synchronization policy instead of exist.getModified().before(entity.getModified())
					if(exist == null || (exist.getModifiedFrom()!=null && exist.getModifiedFrom().equals(SyncConstants.SYNC_ID_SYS)) || exist.getModified().before(entity.getModified())) {
						entity.setUploadDate(new Date(System.currentTimeMillis()));	
						if(exist != null)
							handler.merge(entity, user);
						else
							handler.saveOrUpdate(entity, user);
						j++;
					}
				}				
			}
		}catch (Exception ex) {
			throw new ImogSerializationException(ex);
		}
		return j;		
	}
	
	
	
	/* Setters for bean injection */

	/**
	 * Set the DataHandlerManager to use
	 * @param dataHandlerManager the DataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager){
		this.dataHandlerManager = dataHandlerManager;
	}
	
	/**
	 * Setter for spring injection
	 * @param converters
	 */
	public void setPropertyConverters(Set<PropertyConverter> converters){
		
		for(PropertyConverter conv: converters){
			try{
				xstream.registerLocalConverter(Class.forName(conv.getClassName()), conv.getPropertyName(), conv.getConverter());
				Mapper mapper = xstream.getMapper();
				mapper.getLocalConverter(Class.forName(conv.getClassName()),conv.getPropertyName());				
			}
			catch(ClassNotFoundException ex){
				logger.error(ex.getMessage());
			}
		}
	}
	
	/**
	 * Setter for spring injection
	 * @param converter
	 */
	public void setClassConverters(Set<ClassConverter> converters){	
		
		for(ClassConverter conv: converters){
			xstream.registerConverter(conv.getConverter());
			try {
				xstream.alias(conv.getAlias(), Class.forName(conv.getClassType()));
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
}
