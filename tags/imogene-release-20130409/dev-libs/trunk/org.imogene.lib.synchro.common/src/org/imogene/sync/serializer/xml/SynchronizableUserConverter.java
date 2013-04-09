package org.imogene.sync.serializer.xml;

import java.util.Set;

import org.apache.log4j.Logger;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.uao.role.Role;
import org.imogene.uao.synchronizable.SynchronizableEntity;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * XStream converter for the a Imogene User.
 * 
 * NOT USED ANYMORE
 * 
 * @author MEDES-IMPS
 */
public abstract class SynchronizableUserConverter implements Converter {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public abstract void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context);	
	public abstract Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context);
	public abstract boolean canConvert(Class toConvert);
	
	
	/**
	 * Marshals a user, to be used in the implementation
	 * of the abstract method marshal
	 * @param writer
	 * @param user the user to marshal
	 */
	protected void marshalUser(HierarchicalStreamWriter writer, SynchronizableUser user) {
		
		// id
		writer.startNode("id");
		writer.setValue(user.getId());
		writer.endNode();
		
		// Login
		writer.startNode("login");
		writer.setValue(user.getLogin());
		writer.endNode();
		
		// Password
		writer.startNode("password");
		writer.setValue(user.getPassword());
		writer.endNode();
		
		// Roles
		writer.startNode("assignedRoles");	
		for(Role role:(Set<Role>)user.getRoles()){
			writer.startNode("role");
			writer.setValue(role.getId());			
			writer.endNode();
		}	
		writer.endNode();	
		
		// Synchronizables
		writer.startNode("assignedSynchronizables");	
		for(SynchronizableEntity synchronizable:(Set<SynchronizableEntity>)user.getSynchronizables()){
			writer.startNode("entity");
			writer.setValue(synchronizable.getId());			
			writer.endNode();
		}	
		writer.endNode();			
	}
	
	
	/**
	 * Unmarshals a user, to be used in the implementation
	 * of the abstract method unmarshal
	 * @param reader
	 * @param user the user to unmarshal
	 */
	protected void unmarshalUser(HierarchicalStreamReader reader, SynchronizableUser user) {
		
		// Id
		reader.moveDown();
		String id = reader.getValue();
		user.setId(id);
		reader.moveUp();
		
		// Login
		reader.moveDown();
		String login = reader.getValue();
		user.setLogin(login);
		reader.moveUp();
		
		// Password
		reader.moveDown();
		String password = reader.getValue();
		user.setPassword(password);
		reader.moveUp();		
		
		// Roles
		reader.moveDown();
		while (reader.hasMoreChildren()) {		
			reader.moveDown();
			Role role = new Role();
			String roleId = reader.getValue();
			role.setId(roleId);
			role.setName(roleId);
			user.addRole(role);	
			reader.moveUp();							
		}		
		reader.moveUp();
		
		// Synchronizables
		reader.moveDown();
		while (reader.hasMoreChildren()) {		
			reader.moveDown();
			SynchronizableEntity synchronizable = new SynchronizableEntity();
			String synchronizableId = reader.getValue();
			synchronizable.setId(synchronizableId);
			synchronizable.setName(synchronizableId);
			user.addSynchronizable(synchronizable);
			reader.moveUp();							
		}		
		reader.moveUp();
		
	}
	

	

}
