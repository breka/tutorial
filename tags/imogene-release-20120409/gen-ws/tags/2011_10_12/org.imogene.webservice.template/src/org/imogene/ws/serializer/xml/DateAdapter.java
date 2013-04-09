package org.imogene.ws.serializer.xml;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Enables to serialize and deserialize a Date object
 * as a String representation of its Long value
 * @author MEDES-IMPS
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	public String marshal(Date date) throws Exception {
		if(date!=null)
			return String.valueOf(date.getTime());
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	public Date unmarshal(String dateAsLong) throws Exception {
		if(!dateAsLong.equals(""))
			return new Date(Long.parseLong(dateAsLong));
		else
			return null;
	}
	
	

}
