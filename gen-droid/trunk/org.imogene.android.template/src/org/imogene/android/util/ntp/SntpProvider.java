package org.imogene.android.util.ntp;

import org.imogene.android.W;
import org.imogene.android.preference.PreferenceHelper;

import android.content.Context;
import android.os.SystemClock;

public class SntpProvider {
	
	private static long getRealTimeFromNtp(String ntpServer) throws SntpException {
		SntpClient client = new SntpClient();
        if (client.requestTime(ntpServer, 15000)) {
        	/* test done with NTP
        	  if (false) {
        		long time = client.getNtpTime();
        		long timeReference = client.getNtpTimeReference();
        		int certainty = (int)(client.getRoundTripTime()/2);
 
       			Log.i(TAG, 
       					"calling native_inject_time: " + time + 
       					" reference: " + timeReference + 
       					" certainty: " + certainty);
        	}*/
            
            long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
            
//            SystemClock.setCurrentTimeMillis(time);
//            native_inject_time(time, timeReference, certainty);
            
            return now;
        } else {
        	throw new SntpException();
        }
	}
	
    public static final long getTimeOffsetFromNtp(Context context) throws SntpException {
    	String ntpServer = PreferenceHelper.getNtpServerUrl(context);
    	
		try {
			long real = SntpProvider.getRealTimeFromNtp(ntpServer);
			long local = System.currentTimeMillis();
			return local-real;
		} catch (SntpException e) {
			throw new SntpException();
		}
    }
    
    public static final void updateTimeOffset(Context context, long offset) {
    	String ntpOffsetKey = context.getString(W.string.ntp_offset_key);
    	PreferenceHelper.getSharedPreferences(context).edit().putLong(ntpOffsetKey, offset).commit();
    }
}
