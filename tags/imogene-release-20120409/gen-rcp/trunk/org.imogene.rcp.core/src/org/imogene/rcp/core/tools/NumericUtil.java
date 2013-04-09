package org.imogene.rcp.core.tools;

public class NumericUtil {
	
	/**
	 * 
	 * @param strInt
	 * @return
	 */
	public static Integer parseToInteger(String strInt){
		try{
			int integer = Integer.parseInt(strInt);
			return new Integer(integer);
		}catch(NumberFormatException nfe){			
			return null;			
		}		
	}
	
	/**
	 * 
	 * @param integer
	 * @return
	 */
	public static String parseToString(Integer integer){
		if(integer != null)
			return String.valueOf(integer.intValue());
		return null;
	}
	
	/**
	 * 
	 * @param strInt
	 * @return
	 */
	public static Float parseToFloat(String strInt){
		try{
			float flotant = Float.parseFloat(strInt);
			return new Float(flotant);
		}catch(NumberFormatException nfe){			
			return null;	
		}		
	}
	
	/**
	 * Validation test for integers
	 */
	public static boolean numberMatches(String pattern, int value) {
		try {
			if (pattern.startsWith("<=")) {
				String integer = pattern.substring(2);
				return value <= Integer.parseInt(integer);
			}

			if (pattern.startsWith("<")) {
				String integer = pattern.substring(1);
				return value < Integer.parseInt(integer);
			}

			if (pattern.startsWith(">=")) {
				String integer = pattern.substring(2);
				return value >= Integer.parseInt(integer);
			}

			if (pattern.startsWith(">")) {
				String integer = pattern.substring(1);
				return value > Integer.parseInt(integer);
			}

			if (pattern.startsWith("!=")) {
				String integer = pattern.substring(2);
				return value != Integer.parseInt(integer);
			}

			if (pattern.startsWith("=")) {
				String integer = pattern.substring(1);
				return value == Integer.parseInt(integer);
			}
			if (pattern.contains(";")) {
				String[] integers = pattern.split(";");
				return Integer.parseInt(integers[0]) < value && value < Integer.parseInt(integers[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	/**
	 * Validation test for floats
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
	public static boolean numberMatches(String pattern, float value) {
		try {
			if (pattern.startsWith("<=")) {
				String number = pattern.substring(2);
				return value <= Float.parseFloat(number);
			}

			if (pattern.startsWith("<")) {
				String number = pattern.substring(1);
				return value < Float.parseFloat(number);
			}

			if (pattern.startsWith(">=")) {
				String number = pattern.substring(2);
				return value >= Float.parseFloat(number);
			}

			if (pattern.startsWith(">")) {
				String number = pattern.substring(1);
				return value > Float.parseFloat(number);
			}

			if (pattern.startsWith("!=")) {
				String number = pattern.substring(2);
				return value != Float.parseFloat(number);
			}

			if (pattern.startsWith("==")) {
				String number = pattern.substring(2);
				return value == Float.parseFloat(number);
			}
			if (pattern.contains(";")) {
				String[] floats = pattern.split(";");
				return Float.parseFloat(floats[0]) < value && value < Float.parseFloat(floats[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return false;
	}
}
