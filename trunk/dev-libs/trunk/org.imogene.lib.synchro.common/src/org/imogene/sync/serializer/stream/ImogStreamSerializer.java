package org.imogene.sync.serializer.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.ImogSerializer;

/**
 * This class enables to serialize and de-serialize Objects using DataInputStream/DataOuputStream objects.
 * for each entity the serialization has to start with the entity class name encoded in utf format.
 * 
 * TODO : add a sample with description
 * @author MEDES-IMPS
 */
public class ImogStreamSerializer implements ImogSerializer {
	
	private DataHandlerManager dataHandlerManager;

	/* each entity type serializer */
	private Map<String, EntityStreamSerializer> entitySerializers = new HashMap<String, EntityStreamSerializer>();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.serialize.BeanSerializer#deSerialize(java.io.InputStream)
	 */
	public Synchronizable deSerialize(InputStream data) throws ImogSerializationException {
		DataInputStream daos = new DataInputStream(data);
		try{
			String type = daos.readUTF();		
			EntityStreamSerializer internal = entitySerializers.get(type);
			if(internal!=null) 
				return internal.deSerialize(daos);	
			else
				throw new ImogSerializationException("No serializer found for the type " + type);
		}catch(IOException ioe){
			throw new ImogSerializationException(ioe);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.serialize.BeanSerializer#deSerializeMulti(java.io.InputStream)
	 */
	public List<Synchronizable> deSerializeMulti(InputStream data) throws ImogSerializationException {
		List<Synchronizable> entities = new Vector<Synchronizable>();
		try {
			while (data.available() > 0) {
				Synchronizable entity = deSerialize(data);
				if(entity!=null) entities.add(entity);
			}
			return entities;
		} catch (IOException ioe) {
			throw new ImogSerializationException(ioe);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.serializer.ImogSerializer#processMulti(java.io.InputStream, org.imogene.common.data.SynchronizableUser)
	 */
	public int processMulti(InputStream data, SynchronizableUser user) throws ImogSerializationException {		
		int i = 0;
		try {
			while (data.available() > 0) {			
				//unserialize entity
				Synchronizable entity = deSerialize(data);
				
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
						i++;
					}
				}
			}
		} catch (IOException ioe) {
			throw new ImogSerializationException(ioe);
		}		
		return i;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.serialize.BeanSerializer#serialize(java.util.List, java.io.OutputStream)
	 */
	public void serialize(List<Synchronizable> entities, OutputStream data)	throws ImogSerializationException {				
		for(Synchronizable entity:entities){	
			if (!entity.getCreatedBy().equals(SyncConstants.SYNC_ID_SYS)) {
				serialize(entity, data);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.serialize.BeanSerializer#serialize(org.imogene.data.Synchronizable, java.io.OutputStream)
	 */
	public void serialize(Synchronizable entity, OutputStream data) throws ImogSerializationException {
		EntityStreamSerializer serializer = entitySerializers.get(entity.getClass().getName());
		if(serializer != null){
			try {
				DataOutputStream dos = new DataOutputStream(data);
				dos.writeUTF(entity.getClass().getName());
				serializer.serialize(entity, dos);
			} catch (IOException ioe) {
				throw new ImogSerializationException(ioe);
			}
		}
	}
	
	/**
	 * Set the entity serializers
	 * @param pEntitySerializers the entity serializers
	 */
	public void setEntitySerializers(Map<String, EntityStreamSerializer> pEntitySerializers){
		entitySerializers = pEntitySerializers;
	}

	/**
	 * Set the DataHandlerManager to use
	 * @param dataHandlerManager the DataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager) {
		this.dataHandlerManager = dataHandlerManager;
	}

}
