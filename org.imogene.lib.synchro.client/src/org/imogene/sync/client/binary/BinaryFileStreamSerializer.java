package org.imogene.sync.client.binary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.stream.EntityStreamSerializer;
import org.imogene.sync.serializer.xml.base64.Base64FileDecoder;
import org.imogene.sync.serializer.xml.base64.Base64FileEncoder;


/**
 * Serialize and un-serialize a Binary
 * @author MEDES-IMPS
 */
public class BinaryFileStreamSerializer implements EntityStreamSerializer {

	private Logger logger = Logger.getLogger("org.imogene.sync.client.binary");
	
	private static String BINARY_END = "EndOfBinary";

	/*
	 * (non-Javadoc)
	 * @see EntityStreamSerializer#deSerialize(java.io.DataInputStream)
	 */
	public Synchronizable deSerialize(DataInputStream data)	throws ImogSerializationException {
		//logger.info("Un-serializing a Binary");
		BinaryFile entity = null;
		try {
			entity = new BinaryFile();
			
			String id = data.readUTF();
			entity.setId(id);
			entity.setModifiedBy(data.readUTF());
			entity.setModifiedFrom(data.readUTF());
			entity.setModified(new Date(data.readLong()));
			entity.setUploadDate(new Date(data.readLong()));
			entity.setCreatedBy(data.readUTF());
			entity.setCreated(new Date(data.readLong()));

			/* FileName */
			String fileName = data.readUTF();
			if (!fileName.equals(""))
				entity.setFileName(fileName);
			
			/* ContentType */
			String contentType = data.readUTF();
			if (!contentType.equals(""))
				entity.setContentType(contentType);
			
			/* Length */
			long length = data.readLong();
			if (length>-1)
				entity.setLength(length);
			
			/* Content */
			File resultFile = new File(BinaryFileManager.getInstance().buildFilePath(id, fileName));
			OutputStream out = new FileOutputStream(resultFile);
			
			// write content from stream to temp file	
			boolean toBeContinued = true;
			while (toBeContinued) {
				
				String content = data.readUTF();	
				if (content.equals(BINARY_END))
					toBeContinued=false;
				else {
					StringReader strReader = new StringReader(content);	
					BufferedReader br = new BufferedReader(strReader);	

					try {						
						Base64FileDecoder.decodeStream(br, out);	
						out.flush();
					} catch (IOException e) {
						logger.error(e.getMessage());
					} 					
					br.close();	
					strReader.close();					
				}			
			}
			out.close();
			
			/* ParentEntity */
			String parentEntity = data.readUTF();
			if (!parentEntity.equals(""))
				entity.setParentEntity(parentEntity);
			
			/* ParentKey */
			String parentKey = data.readUTF();
			if (!parentKey.equals(""))
				entity.setParentKey(parentKey);
			
			/* ParentFieldGetter */
			String parentFieldGetter = data.readUTF();
			if (!parentFieldGetter.equals(""))
				entity.setParentFieldGetter(parentFieldGetter);
			
			
			//logger.info("Binary with id "+ entity.getId() + " un-serialized");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new ImogSerializationException(ex);
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see EntityStreamSerializer#serialize(org.imogene.data.Synchronizable, java.io.DataOutputStream)
	 */
	public void serialize(Synchronizable entity, DataOutputStream data)
			throws ImogSerializationException {
		//logger.info("Serializing a Binary");		
		try {
			BinaryFile binary = (BinaryFile) entity;
			
			data.writeUTF(binary.getId());
			data.writeUTF(binary.getModifiedBy());
			data.writeUTF(binary.getModifiedFrom());
			data.writeLong(binary.getModified().getTime());
			data.writeLong(binary.getUploadDate().getTime());
			data.writeUTF(binary.getCreatedBy());
			data.writeLong(binary.getCreated().getTime());

			/* FileName */
			if (binary.getFileName() != null)
				data.writeUTF(binary.getFileName());
			else
				data.writeUTF("");
			
			/* ContentType */
			if (binary.getContentType() != null)
				data.writeUTF(binary.getContentType());
			else
				data.writeUTF("");
			
			/* Length */
			if (binary.getLength()>-1)
				data.writeLong(binary.getLength());
			else
				data.writeLong(-1);
			
			// Content
			if (binary.getLength()>-1) {
				
				File tempFile = File.createTempFile(binary.getFileName(), null);
				
				// write content from binary to temp file
				OutputStream out = new FileOutputStream(tempFile);
				OutputStreamWriter outW = new OutputStreamWriter(out);
				BufferedWriter bw = new BufferedWriter(outW);				
				try {					
					Base64FileEncoder.encodeStream(binary.createInputStream(), bw);
					bw.flush();
					outW.flush();
					out.flush();
				}
				catch (IOException e) {
						logger.error(e.getMessage());
				} 	
				finally {
					bw.close();				
					outW.close();
					out.close();						
				}
					
				// write content from temp file to stream
				FileReader fr = new FileReader(tempFile);
				BufferedReader bf = new BufferedReader(fr);				
				while (true) {
					String s = bf.readLine();
					if (s == null)
						break;
					data.writeUTF(s);
				}
				// end of binary
				data.writeUTF(BINARY_END);
				bf.close();
				fr.close();
				tempFile.delete();
				
			}
			else {
				data.writeUTF("");
			}	
			
			/* ParentEntity */
			if (binary.getParentEntity() != null)
				data.writeUTF(binary.getParentEntity());
			else
				data.writeUTF("");
			
			/* ParentKey */
			if (binary.getParentKey() != null)
				data.writeUTF(binary.getParentKey());
			else
				data.writeUTF("");
			
			/* ParentFieldGetter */
			if (binary.getParentFieldGetter() != null)
				data.writeUTF(binary.getParentFieldGetter());
			else
				data.writeUTF("");
			
			
		
			//logger.info("Binary with id "+ binary.getId() + "serialized.");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new ImogSerializationException(ex);
		}
	}

}
