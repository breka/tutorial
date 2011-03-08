package org.imogene.sync.serializer.xml;

import org.imogene.common.data.GeoField;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class GeoFieldConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		
		GeoField field = (GeoField) value;
		if(field!=null) {		
			if (field.getLatitude() != null && field.getLongitude() != null)
				writer.setValue(field.getLatitude() + ";" + field.getLongitude());
			else
				writer.setValue("");
		}
		else
			writer.setValue("");
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		String value = reader.getValue();
		if(!value.equals("")) {
			String[] values = value.split(";");
			if (values.length == 2) {
				GeoField field = new GeoField();
				field.setLatitude(Double.valueOf(values[0]));
				field.setLongitude(Double.valueOf(values[1]));
				return field;
			} 
			else
				return null;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class arg0) {
		if(GeoField.class.isAssignableFrom(arg0))
			return true;
		return false;
	}
	

}
