package org.imogene.android.util;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.imogene.android.Constants.Extras;
import org.imogene.android.template.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;

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
    
    public static final String displayLocation(Context context, Location location) {
    	return context.getString(R.string.location_format, location.getLatitude(), location.getLongitude());
    }
    
    public static final String displayBoundingBox(Context context, BoundingBox box) {
    	return context.getString(R.string.bbox_format, box.getWest(), box.getNorth(), box.getEast(), box.getSouth());
    }
    
    public static final String displayEnumSingle(CharSequence[] items, int[] itemsValues, int value) {
    	return items[Tools.find(itemsValues, value)].toString();
    }
    
    public static final String displayEnumSingle(Context context, int itemsId, int itemsValuesId, int value) {
    	Resources r = context.getResources();
    	return displayEnumSingle(r.getTextArray(itemsId), r.getIntArray(itemsValuesId), value);
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
	
}
