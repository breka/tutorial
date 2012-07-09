package org.imogene.web.server.util;

import java.util.ResourceBundle;


/**
 * Class that helps to get a String display of Boolean objects.
 * @author Medes-IMPS 
 */
public class BooleanUtil {

	private static ResourceBundle rb = ResourceBundle.getBundle("org.imogene.web.gwt.client.i18n.ImogConstants");

	public static String getFormatedBoolean(Boolean bool) {
		if (bool!=null)
		{
			if(bool.booleanValue())
				return rb.getString("boolean_true");
			else
				return rb.getString("boolean_false");			
		}
		else
			return rb.getString("boolean_unknown");		
	}
	
	public static String getFormatedBoolean(Boolean bool, ResourceBundle bundle) {
		rb = bundle;
		return getFormatedBoolean(bool);
	}
}
