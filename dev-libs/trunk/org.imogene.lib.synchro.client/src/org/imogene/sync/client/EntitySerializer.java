package org.imogene.sync.client;

import java.io.InputStream;
import java.io.OutputStream;

public interface EntitySerializer {

	/**
	 * Serialize the entity 
	 * @param entity the entity to serialize
	 * @param output the output stream 
	 */
	public void serialize(Object entity, OutputStream output);
	
	/**
	 * Un-serialize an entity from a serialized stream.
	 * @param in input stream where to read the data
	 * @return the entity
	 */
	public Object unSerialize(InputStream in); 
}
