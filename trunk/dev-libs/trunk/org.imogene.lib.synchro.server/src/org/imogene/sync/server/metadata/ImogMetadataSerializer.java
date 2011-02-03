package org.imogene.sync.server.metadata;

import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImogMetadataSerializer {
	
	private static String META_TERM = "terminal";
	private static String META_TIME = "time";
	private static String META_TAG = "data";
	private static String META_NAME = "name";
	private static String META_VALUE = "value";
	private static String EX_TAG = "exception";
	
	private Logger logger = Logger.getLogger("org.imogene.sync.server.metadata");
	
	private ImogMetadataDao dao;
	
	public ImogMetadataSerializer(ImogMetadataDao metadataDao){
		dao = metadataDao;
	}
	
	public void unserialize(InputStream xml){
		try {
			/* create a document from the xml string */
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder domBuilder = factory.newDocumentBuilder();
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			Document dom = domBuilder.parse(xml);
			/* parse the dom */
			Element root = dom.getDocumentElement();
			String longDate = root.getAttribute(META_TIME);
			String terminal = root.getAttribute(META_TERM);		
			handleMetadata(root, longDate, terminal);
			handleException(root, longDate, terminal);					
						
		} catch (Exception ex) {
			logger.error("Deserialization error", ex);
		}
	}
	
	/**
	 * Handle meta-data elements
	 * @param root the root element
	 * @param longDate the date string representation
	 * @param terminal the terminal id
	 */
	private void handleMetadata(Element root, String longDate, String terminal){
		/* meta elements */
		NodeList metaEntries = root.getElementsByTagName(META_TAG);			
		for(int i =0; i< metaEntries.getLength(); i++){
			Node node = metaEntries.item(i);
			String name = node.getAttributes().getNamedItem(META_NAME).getNodeValue();
			String value = node.getAttributes().getNamedItem(META_VALUE).getNodeValue();
			ImogMetadata data = new ImogMetadata();
			data.setDate(new Date(Long.parseLong(longDate)));
			data.setTerminalId(terminal);
			data.setName(name);
			data.setValue(value);
			dao.saveOrUpdate(data);
		}	
	}
	
	/**
	 * Handle the exception elements
	 * @param root the root element
	 * @param longDate the date string representation
	 * @param terminalthe terminal id
	 */
	private void handleException(Element root, String longDate, String terminal){
		NodeList exEntries = root.getElementsByTagName(EX_TAG);		
		for(int j=0; j<exEntries.getLength(); j++){			
			Node node = exEntries.item(j);
			String time = node.getAttributes().getNamedItem("time").getNodeValue();
			String cdata = node.getFirstChild().getNodeValue();
			ImogMetadata ex = new ImogMetadata();
			ex.setDate(new Date(Long.parseLong(time)));
			ex.setTerminalId(terminal);
			ex.setName("exception");
			ex.setValue(cdata);
			dao.saveOrUpdate(ex);
		}
	}
}
