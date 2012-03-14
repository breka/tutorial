package org.imogene.sync.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateHelper {
	
	private static Logger logger = Logger.getLogger("org.imogene.DateHelper");

	private static String format = "dd/MM/yyyy";
	
	private static String format_time = "dd/MM/yyyy hh:mm:ss";
	
	/**
	 * Convert date to string
	 * @param date the date to convert
	 * @return the string representation.
	 */
	public static String toString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
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
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Convert date time to string
	 * @param date the date to convert
	 * @return The string representation
	 */
	public static String toStringTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format_time);
		return sdf.format(date);
	}
}
