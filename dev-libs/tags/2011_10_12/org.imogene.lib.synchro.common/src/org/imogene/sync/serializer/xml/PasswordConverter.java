package org.imogene.sync.serializer.xml;

import org.imogene.encryption.EncryptionManager;
import org.imogene.sync.serializer.xml.base64.Base64Coder;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * XStream converter for the User password property.
 * @author MEDES-IMPS
 */
public class PasswordConverter implements Converter{
	
	private EncryptionManager encryptionManager;
	public void setEncryptionManager(EncryptionManager manager){
		encryptionManager = manager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

		String password =(String)value;		
		byte[] EncodedResult = encryptionManager.encrypt(password.getBytes());	
		char[] Base64Result = Base64Coder.encode(EncodedResult);
		String result = new String(Base64Result);
		writer.setValue(result);
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class toConvert) {		
		return true;
	}

	

}
