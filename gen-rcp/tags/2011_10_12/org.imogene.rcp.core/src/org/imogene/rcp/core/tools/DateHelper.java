package org.imogene.rcp.core.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	private static String format = "dd/MM/yyyy";
	private static String format_datetime = "dd/MM/yyyy HH:mm";
	private static String format_time = "HH:mm:ss";
	
	
	/**
	 * Convert a string to a date
	 * @param dateStr the string representation
	 * @return the date
	 */
	public static Date toDate(String dateStr){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateStr);
		}catch(ParseException ex){
			ex.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * Convert a string to a date
	 * @param dateStr the string representation
	 * @return the date
	 */
	public static Date toDateTime(String dateStr){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format_datetime);
			return sdf.parse(dateStr);
		}catch(ParseException ex){
			ex.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * Convert a string to a date
	 * @param dateStr the string representation
	 * @return the date
	 */
	public static Date toTime(String dateStr){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format_time);
			return sdf.parse(dateStr);
		}catch(ParseException ex){
			ex.printStackTrace();
		}
		return null;
	}	
	
	
	/**
	 * Convert date to string
	 * @param date the date to convert
	 * @return the string representation.
	 */
	public static String toString(Date date){
		if(date != null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}
		else{
			return new String();
		}
	}
	
	/**
	 * Convert date time to string
	 * @param date the date to convert
	 * @return The string representation
	 */
	public static String toStringDateTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format_datetime);
		return sdf.format(date);
	}
	
	/**
	 * Convert time to string
	 * @param date the date to convert
	 * @return The string representation
	 */
	public static String toStringTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format_time);
		return sdf.format(date);
	}
	
	/**
	 * Validation test for date
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
	public static boolean matchesDate(String pattern, Date value) {
		try {
			if (pattern.startsWith("<=")) {
				Date limit = toDate(pattern.substring(2));
				return value.before(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith("<")) {
				Date limit = toDate(pattern.substring(1));
				return value.before(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith(">=")) {
				Date limit = toDate(pattern.substring(2));
				return value.after(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith(">")) {
				Date limit = toDate(pattern.substring(1));
				return value.after(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("!=")) {
				Date limit = toDate(pattern.substring(2));
				return value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("==")) {
				Date limit = toDate(pattern.substring(2));
				return value.compareTo(limit)== 0;
			}
			
			if (pattern.contains(";")) {
				String[] integers = pattern.split(";");
				Date limitInf = toDate(integers[0]);
				Date limitSup = toDate(integers[1]);
				return value.after(limitInf) && value.before(limitSup);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Validation test for date time
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
	public static boolean matchesDateTime(String pattern, Date value) {
		try {
			if (pattern.startsWith("<=")) {
				Date limit = toDateTime(pattern.substring(2));
				return value.before(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith("<")) {
				Date limit = toDateTime(pattern.substring(1));
				return value.before(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith(">=")) {
				Date limit = toDateTime(pattern.substring(2));
				return value.after(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith(">")) {
				Date limit = toDateTime(pattern.substring(1));
				return value.after(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("!=")) {
				Date limit = toDateTime(pattern.substring(2));
				return value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("==")) {
				Date limit = toDateTime(pattern.substring(2));
				return value.compareTo(limit)== 0;
			}
			
			if (pattern.contains(";")) {
				String[] integers = pattern.split(";");
				Date limitInf = toDateTime(integers[0]);
				Date limitSup = toDateTime(integers[1]);
				return value.after(limitInf) && value.before(limitSup);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Validation test for time
	 * @param pattern the validation pattern
	 * @param value the value to test
	 * @return true if the test matches
	 */
	public static boolean matchesTime(String pattern, Date value) {
		try {
			if (pattern.startsWith("<=")) {
				Date limit = toTime(pattern.substring(2));
				return value.before(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith("<")) {
				Date limit = toTime(pattern.substring(1));
				return value.before(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith(">=")) {
				Date limit = toTime(pattern.substring(2));
				return value.after(limit) || value.compareTo(limit)== 0;
			}

			if (pattern.startsWith(">")) {
				Date limit = toTime(pattern.substring(1));
				return value.after(limit) && value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("!=")) {
				Date limit = toTime(pattern.substring(2));
				return value.compareTo(limit)!= 0;
			}

			if (pattern.startsWith("==")) {
				Date limit = toTime(pattern.substring(2));
				return value.compareTo(limit)== 0;
			}
			
			if (pattern.contains(";")) {
				String[] integers = pattern.split(";");
				Date limitInf = toTime(integers[0]);
				Date limitSup = toTime(integers[1]);
				return value.after(limitInf) && value.before(limitSup);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
}
