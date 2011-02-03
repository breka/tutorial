package org.imogene.testsynchro.serializer.xml;

import org.imogene.sync.serializer.xml.SynchronizableUserConverter;
import org.imogene.testsynchro.entity.DoctorUser;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * XStream converter for the a Imog User.
 * @author MEDES-IMPS
 */
public class DoctorUserConverter extends SynchronizableUserConverter  {
		
	
	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */	
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {		
		DoctorUser user = (DoctorUser) value;
		marshalUser(writer, user);	
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {	
		DoctorUser user = new DoctorUser();
		unmarshalUser(reader, user);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class toConvert) {		
		return toConvert.equals(DoctorUser.class);
	}
	
	
	


	

}
