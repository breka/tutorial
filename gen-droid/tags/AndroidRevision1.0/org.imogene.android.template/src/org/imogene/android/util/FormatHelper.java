package org.imogene.android.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.imogene.android.Constants;
import org.imogene.android.Constants.Extras;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

public class FormatHelper {
	
	private static final DecimalFormatSymbols DFS = new DecimalFormatSymbols();
	
	static {
		DFS.setDecimalSeparator('.');
	}
	
	private static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
	private static DateFormat DATI_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
	private static DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.FULL);
	
	private static final SimpleDateFormat CONSTRAINT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat CONSTRAINT_DATI_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final SimpleDateFormat CONSTRAINT_TIME_FORMAT = new SimpleDateFormat("HH:mm");
	
	public static final void updateFormats() {
		DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
		DATI_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.FULL);
	}
	
    public static final String displayAsDate(long time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	return DATE_FORMAT.format(cal.getTime());
    }
    
    public static final String displayAsDateTime(long time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	return DATI_FORMAT.format(cal.getTime());
    }
    
    public static final String displayAsTime(long time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	return TIME_FORMAT.format(cal.getTime());
    }
    
    public static final String displayLocation(Location location) {
    	return displayDecimals(location.getLatitude(), 2) +	" ; " +
    		displayDecimals(location.getLongitude(), 2);
    }
    
    public static final String displayBoundingBox(BoundingBox box) {
    	return displayDecimals(box.getWest(), 2) + " ; " + 
    	displayDecimals(box.getNorth(), 2) + " ; " +
    	displayDecimals(box.getEast(), 2) + " ; " +
    	displayDecimals(box.getSouth(), 2);
    }
    
    public static final String displayDecimals(double value, int decimals) {
		if (decimals > 0) {
			StringBuilder builder = new StringBuilder("#0.");
			for (int i = 0; i < decimals; i++)
				builder.append('#');
			DecimalFormat format = new DecimalFormat(builder.toString());
			format.setDecimalFormatSymbols(DFS);
			return format.format(value);
		} else {
			return String.valueOf(value);
		}
    }
    
    public static final String displayEnumMulti(String[] array, boolean[] value) {
    	StringBuilder builder = new StringBuilder();
    	boolean hasOne = false;
    	for (int i = 0; i < array.length; i++) {
    		if (value[i]) {
    			if (hasOne)
    				builder.append(" ; ");
    			else
    				hasOne = true;
    			builder.append(array[i]);
    		}
    	}
    	return builder.toString();
    }
    
	public static final Long toLong(String str) {
		try {
			return new Long(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final Float toFloat(String str) {
		try {
			return new Float(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final Integer toInteger(String str) {
		try {
			return new Integer(str);
		} catch (Exception e){
			return null;
		}
	}
	
	public static final Boolean toBoolean(String str) {
		if (str == null) {
			return null;
		} else {
			return Boolean.valueOf(str);
		}
	}
	
	public static final Double toDouble(String str) {
		try {
			return new Double(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final Long readDate(String str) {
		try {
			Date date = CONSTRAINT_DATE_FORMAT.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static final Long readDateTime(String str) {
		try {
			Date date = CONSTRAINT_DATI_FORMAT.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static final Long readTime(String str) {
		try {
			Date date = CONSTRAINT_TIME_FORMAT.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static final Location getLocationFromIntent(Intent data) {
		return data.getParcelableExtra(Extras.EXTRA_LOCATION);
	}
	
	public static final Uri buildUriForFragment(String path) {
		return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
				.authority(Constants.AUTHORITY).path(path).build();
	}
	
}
