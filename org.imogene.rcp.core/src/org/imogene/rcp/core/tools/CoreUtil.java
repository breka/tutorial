package org.imogene.rcp.core.tools;

public class CoreUtil {
	
	
	/**
	 * Sets the first character of a String to upper case
	 * @param from the string to be converted
	 * @return the string starting with an upper case
	 */
	public static String toFirstUpper(String from) {
		String to = new String();
		to = from.substring(0, 1).toUpperCase();
		to = to + from.substring(1);
		return to;
	}
	
	/**
	 * Sets the first character of a String to lower case
	 * @param from the string to be converted
	 * @return the string starting with an lower case
	 */
	public static String toFirstLower(String from) {
		String to = new String();
		to = from.substring(0, 1).toLowerCase();
		to = to + from.substring(1);
		return to;
	}

}
