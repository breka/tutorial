package org.imogene.sync.serializer.xml;

import com.thoughtworks.xstream.converters.Converter;

public class ClassConverter {

	private String alias;
	
	private String classType;
	
	private Converter converter;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	
}
