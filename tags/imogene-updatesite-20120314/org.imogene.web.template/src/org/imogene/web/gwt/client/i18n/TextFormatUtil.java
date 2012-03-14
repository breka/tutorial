package org.imogene.web.gwt.client.i18n;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Static methods which provide help to format 
 * text regarding the local configuration and specificities.
 * @author Medes-IMPS 
 */
public class TextFormatUtil {
	
	private static DateTimeFormat dateFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_date());
	private static DateTimeFormat dateTimeFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time()+ " (v)");
	private static DateTimeFormat timeFormater = DateTimeFormat.getFormat(BaseNLS.constants().format_time()+ " v");

	/**
	 * Get Localized date
	 */
	public static String getFormatedDate(Date date) {
		return dateFormater.format(date);		
	}

	/**
	 * Get Localized datetime
	 */
	public static String getFormatedDateTime(Date datetime) {
		return dateTimeFormater.format(datetime);		
	}

	/**
	 * Get Localized time
	 */
	public static String getFormatedTime(Date time) {
		return timeFormater.format(time);		
	}
	
	public static String getDate(Date date) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_date()).format(date);		
	}	
	
	public static String getDateTime(Date datetime) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time()).format(datetime);		
	}
	
	public static String getTime(Date time) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_time()).format(time);		
	}	
	
	/**
	 * Parse Localized datetime
	 */
	public static Date parseFormatedDateTime(String datetime) {
		return dateTimeFormater.parse(datetime);		
	}

	/**
	 * Parse validation date
	 */
	public static Date parseValidationDate(String date) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_date()).parse(date);		
	}

	/**
	 * Parse User validation datetime
	 */
	public static Date parseValidationDateTime(String datetime) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_date() + " " + BaseNLS.constants().format_validation_time()).parse(datetime);		
	}
	
	/**
	 * Parse User validation time
	 */	
	public static Date parseValidationTime(String time) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_validation_time()).parse(time);		
	}	
	
	/**
	 * Parse User entered date
	 */
	public static Date parseDate(String date) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_date()).parse(date);		
	}

	/**
	 * Parse User entered datetime
	 */
	public static Date parseDateTime(String datetime) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_date() + " " + BaseNLS.constants().format_time()).parse(datetime);		
	}
	
	/**
	 * Parse User entered time
	 */	
	public static Date parseTime(String time) {
		return DateTimeFormat.getFormat(BaseNLS.constants().format_time()).parse(time);		
	}	
	
	
	public static String getFormatedBoolean(Boolean bool) {
		if (bool!=null)
		{
			if(bool.booleanValue())
				return BaseNLS.constants().boolean_true();
			else
				return BaseNLS.constants().boolean_false();			
		}
		else
			return BaseNLS.constants().boolean_unknown();		
	}

}
