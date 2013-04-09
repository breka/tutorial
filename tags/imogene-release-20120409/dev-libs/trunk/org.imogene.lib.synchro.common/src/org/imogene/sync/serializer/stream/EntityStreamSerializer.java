package org.imogene.sync.serializer.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.imogene.common.data.Synchronizable;
import org.imogene.sync.serializer.ImogSerializationException;


/**
 * This interface describes a simple entity serializer.
 * @author MEDES-IMPS
 */
public interface EntityStreamSerializer {

	/**
	 * De-serialize an entity. 
	 * @param data the data stream
	 * @return the entity object
	 */
	public Synchronizable deSerialize(DataInputStream data) throws ImogSerializationException;
	
	/**
	 * Serialize the entity in the specified stream.
	 * @param entity the entity to serialize
	 * @param data the output stream where to serialize
	 */
	public void serialize(Synchronizable entity, DataOutputStream data) throws ImogSerializationException;
}
