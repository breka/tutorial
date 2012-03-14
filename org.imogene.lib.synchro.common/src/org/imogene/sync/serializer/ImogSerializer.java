package org.imogene.sync.serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;


/**
 * This interface specify a bean serializer/unserializer
 * @author Medes-IMPS
 *
 */
public interface ImogSerializer {

	/**
	 * Serialize an entity in the specified output stream
	 * @param entity the entity to serialize
	 * @param data the output stream
	 */
	public void serialize(Synchronizable entity, OutputStream data) throws ImogSerializationException;
	
	/**
	 * Serialize a list of entities in the specified output stream
	 * @param entities entities to serialize
	 * @param data	  the output stream
	 */
	public void serialize(List<Synchronizable> entities, OutputStream data) throws ImogSerializationException ;
	
	/**
	 * Unserialize an entity, reading the input stream
	 * @param data input stream
	 * @return the entity
	 */
	public Synchronizable deSerialize(InputStream data) throws ImogSerializationException;
	
	/**
	 * Unserialize a list of entities, reading the input stream
	 * @param data the input stream
	 * @return the list of object
	 */
	public List<Synchronizable> deSerializeMulti(InputStream data) throws ImogSerializationException;
	
	/**
	 * Unserialize a list of entities, reading the input stream
	 * and save each entity after it has been unserialized
	 * @param data the input stream
	 * @return the number of processed entities
	 */	
	public int processMulti(InputStream data, SynchronizableUser user) throws ImogSerializationException;
}
