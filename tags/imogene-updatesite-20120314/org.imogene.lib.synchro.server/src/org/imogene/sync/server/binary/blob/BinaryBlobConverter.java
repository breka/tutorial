package org.imogene.sync.server.binary.blob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.imogene.common.data.Binary;
import org.imogene.sync.serializer.xml.base64.Base64FileDecoder;
import org.imogene.sync.serializer.xml.base64.Base64FileEncoder;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream converter for Binary.
 * @author MEDES-IMPS
 */
public class BinaryBlobConverter implements Converter {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.serializer.xml");


	/*
	 * (non-Javadoc)
	 * @see Converter#marshal(java.lang.Object, HierarchicalStreamWriter, MarshallingContext)
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		
		BinaryBlob binary = (BinaryBlob) value;
		
		// id
		writer.startNode("id");
		writer.setValue(binary.getId());
		writer.endNode();
		
		// modified
		writer.startNode("modified");
		writer.setValue(String.valueOf(binary.getModified().getTime()));
		writer.endNode();
		
		// uploadDate
		writer.startNode("uploadDate");
		writer.setValue(String.valueOf(binary.getUploadDate().getTime()));
		writer.endNode();
		
		// modifiedBy
		writer.startNode("modifiedBy");
		writer.setValue(binary.getModifiedBy());
		writer.endNode();
		
		// created
		writer.startNode("created");
		writer.setValue(String.valueOf(binary.getCreated().getTime()));
		writer.endNode();
		
		// createdBy
		writer.startNode("createdBy");
		writer.setValue(binary.getCreatedBy());
		writer.endNode();
		
		// fileName
		writer.startNode("fileName");
		if (binary.getFileName()!=null)
			writer.setValue(binary.getFileName());
		writer.endNode();
		
		// contentType
		writer.startNode("contentType");
		if (binary.getContentType()!=null)
			writer.setValue(binary.getContentType());
		writer.endNode();
		
		// length
		writer.startNode("length");
		if (binary.getLength()>0)
			writer.setValue(String.valueOf(binary.getLength()));
		writer.endNode();
		
		// content
		writer.startNode("content");
		//System.out.println("<content>");
		if (binary.getContent() != null) {
			
			try {
				File tempFile = File.createTempFile(binary.getFileName(), null);	
				
				// write content from binary to temp file
				OutputStream out = new FileOutputStream(tempFile);
				OutputStreamWriter outW = new OutputStreamWriter(out);
				BufferedWriter bw = new BufferedWriter(outW);						
				Base64FileEncoder.encodeStream(binary.createInputStream(), bw);
				bw.flush();
				outW.flush();
				out.flush();
				bw.close();
				outW.close();
				out.close();			
				// write content from temp file to xml
				FileReader fr = new FileReader(tempFile);
				BufferedReader bf = new BufferedReader(fr);				
				while (true) {
					String s = bf.readLine();
					if (s == null)
						break;
					writer.startNode("data");
					//System.out.println("<data>");
					writer.setValue(s);	
					//System.out.println(s);
					writer.endNode();
					//System.out.println("</data>");
				}
				bf.close();
				fr.close();	
				tempFile.delete();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			} 			
		}		
		writer.endNode();
		//System.out.println("</content>");
		
		// parentEntity
		writer.startNode("parentEntity");
		if (binary.getParentEntity()!=null)
			writer.setValue(binary.getParentEntity());
		writer.endNode();
		
		// parentKey
		writer.startNode("parentKey");
		if (binary.getParentKey()!=null)
			writer.setValue(binary.getParentKey());
		writer.endNode();
		
		// parentFieldGetter
		writer.startNode("parentFieldGetter");
		if (binary.getParentFieldGetter()!=null)
			writer.setValue(binary.getParentFieldGetter());
		writer.endNode();
		
	}

	/*
	 * (non-Javadoc)
	 * @see Converter#unmarshal(HierarchicalStreamReader, UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		BinaryBlob binary = new BinaryBlob();
		
		// Id
		reader.moveDown();
		String id = reader.getValue();
		binary.setId(id);
		reader.moveUp();		
		
		// modified
		reader.moveDown();
		String modified = reader.getValue();
		binary.setModified(new Date(Long.valueOf(modified)));
		reader.moveUp();	
		
		// uploadDate
		reader.moveDown();
		String uploadDate = reader.getValue();
		binary.setUploadDate(new Date(Long.valueOf(uploadDate)));
		reader.moveUp();
		
		// modifiedBy
		reader.moveDown();
		String modifiedBy = reader.getValue();
		binary.setModifiedBy(modifiedBy);
		reader.moveUp();
		
		// created
		reader.moveDown();
		String created = reader.getValue();
		binary.setCreated(new Date(Long.valueOf(created)));
		reader.moveUp();
		
		// createdBy
		reader.moveDown();
		String createdBy = reader.getValue();
		binary.setCreatedBy(createdBy);
		reader.moveUp();
		
		// fileName
		reader.moveDown();
		String fileName = reader.getValue();
		if (!fileName.equals(""))
			binary.setFileName(fileName);
		reader.moveUp();
		
		// contentType
		reader.moveDown();
		String contentType = reader.getValue();
		if (!contentType.equals(""))
			binary.setContentType(contentType);
		reader.moveUp();
		
		// length
		reader.moveDown();
		String length = reader.getValue();
		if (!length.equals(""))
			binary.setLength(Long.valueOf(length));
		reader.moveUp();
		
		// content
		reader.moveDown();	
		if (reader.hasMoreChildren()) {
			try {
				//File tempFile = File.createTempFile(fileName, null);
				File resultFile = new File("./data/" + UUID.randomUUID().toString() + ".jpg");
				OutputStream out = new FileOutputStream(resultFile);		
				
				// write content from xml to temp file
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					String content = reader.getValue();
					StringReader strReader = new StringReader(content);	
					BufferedReader br = new BufferedReader(strReader);							
					try {
						Base64FileDecoder.decodeStream(br, out);	
						out.flush();					
						br.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}	
					strReader.close();				
					reader.moveUp();
				}
				out.close();
				
				// write content from temp file to binary
				binary.setContent(new FileInputStream(resultFile));

				//tempFile.delete();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}			
		}		
		reader.moveUp();
		
		// parentEntity
		reader.moveDown();
		String parentEntity = reader.getValue();
		if (!parentEntity.equals(""))
			binary.setParentEntity(parentEntity);
		reader.moveUp();
		
		// parentKey
		reader.moveDown();
		String parentKey = reader.getValue();
		if (!parentKey.equals(""))
			binary.setParentKey(parentKey);
		reader.moveUp();
		
		// parentFieldGetter
		reader.moveDown();
		String parentFieldGetter = reader.getValue();
		if (!parentFieldGetter.equals(""))
			binary.setParentFieldGetter(parentFieldGetter);
		reader.moveUp();
		
		return binary;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class cl) {
		for(Class implemented : cl.getInterfaces()){
			   if(implemented.equals(Binary.class))
			    return true;
			  }
			  return false;
	}


}
