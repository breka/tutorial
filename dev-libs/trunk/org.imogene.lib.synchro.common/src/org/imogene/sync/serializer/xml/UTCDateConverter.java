package org.imogene.sync.serializer.xml;

import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class UTCDateConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		if(value!=null)
			writer.setValue(String.valueOf(((Date)value).getTime()));
		else
			writer.setValue("");
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String value = reader.getValue();
		if(!value.equals(""))
			return new Date(Long.parseLong(reader.getValue()));
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class arg0) {
		if(Date.class.isAssignableFrom(arg0))
			return true;
		return false;
	}
	

}
