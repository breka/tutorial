package org.imogene.android.receiver;

import java.io.File;
import java.util.UUID;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.template.R;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.service.MetadataService;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;

public class OperationSmsReceiver extends BroadcastReceiver {
	
	private static final String KEY = "16021899";
	
	private static final int READ_LOGIN = 1;
	private static final int READ_TELNB = 2;
	private static final int READ_ENTNB = 3;
	private static final int READ_SYNCSTAT = 4;
	private static final int READ_SYNCURL = 5;
	private static final int READ_SYNCPER = 6;
	private static final int READ_OFFSET = 7;
	private static final int READ_NTP = 8;
	private static final int READ_DEBUG = 9;
	private static final int READ_BATLEV = 10;
	private static final int READ_SDCARD = 11;
	
	private static final int WRITE_RENEW_SYNC_HIST = 1;
	private static final int WRITE_RENEW_DATABASE = 2;
	private static final int WRITE_RENEW_HARDWARE_ID = 3;
	private static final int WRITE_FORCE_SYNCHRO = 4;
	private static final int WRITE_STOP_SYNCHRO = 5;
	private static final int WRITE_ACTIVATE_DEBUG = 6;
	private static final int WRITE_DEACTIVATE_DEBUG = 7;
	private static final int WRITE_SYNCURL = 8;
	private static final int WRITE_SYNCPER = 9;
	private static final int WRITE_CLEAR_USER = 10;
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                String[] body = msgs[i].getMessageBody().split(";");
            	if (KEY.equals(body[0])) {
            		long smsnb = PreferenceHelper.getSmsNumber(context);
            		if (Long.parseLong(body[1]) == smsnb) {
            			PreferenceHelper.getSharedPreferences(context).edit().putLong(context.getString(R.string.debug_smsnb_key), ++smsnb).commit();
            			if (body[2].equals("r")) {
            				performRead(context, body);
            			} else if (body[2].equals("w")) {
            				performWrite(context, body);
            			}
            		}
            	}
            }
        }
	}
	
	private void performRead(Context context, String[] body) {
		ContentValues values = new ContentValues();
		for (int i = 3; i < body.length; i++) {
			int param = Integer.parseInt(body[i]);
			switch (param) {
			case READ_LOGIN:
				values.put("login", PreferenceHelper.getCurrentLogin(context));
				break;
			case READ_TELNB:
				break;
			case READ_ENTNB:
				//TODO
				break;
			case READ_SYNCSTAT:
				String syncStatKey = context.getString(R.string.sync_on_off_key);
				values.put(syncStatKey, PreferenceHelper.getSynchronizationStatus(context));
				break;
			case READ_SYNCURL:
				String serverUrlKey = context.getString(R.string.sync_server_url_key);
				values.put(serverUrlKey, PreferenceHelper.getServerUrl(context));
				break;
			case READ_SYNCPER:
				String periodeKey = context.getString(R.string.sync_time_key);
				values.put(periodeKey, PreferenceHelper.getSynchronizationPeriod(context));
				break;
			case READ_OFFSET:
				String offsetKey = context.getString(R.string.ntp_offset_key);
				values.put(offsetKey, PreferenceHelper.getNtpOffset(context));
				break;
			case READ_NTP:
				String ntpServerKey = context.getString(R.string.ntp_server_key);
				values.put(ntpServerKey, PreferenceHelper.getNtpServerUrl(context));
				break;
			case READ_DEBUG:
				String debugKey = context.getString(R.string.debug_save_key);
				values.put(debugKey, PreferenceHelper.isDebugActive(context));
				break;
			case READ_BATLEV:
				
				break;
			case READ_SDCARD:
				File sdcard = Environment.getExternalStorageDirectory();
				values.put("can-write-to-sdcard", sdcard.canWrite());
				values.put("can-read-from-sdcard", sdcard.canRead());
				break;
			default:
				break;
			}
		}
		MetadataService.startMetadataService(context, values);
	}

	private void performWrite(Context context, String[] body) {
		for (int i = 3; i < body.length; i++) {
			int param = Integer.parseInt(body[i]);
			switch (param) {
			case WRITE_RENEW_SYNC_HIST:
				context.sendBroadcast(new Intent(Intents.ACTION_RM_SYNC_HISTORY));
				break;
			case WRITE_RENEW_DATABASE:
				context.sendBroadcast(new Intent(Intents.ACTION_RM_DATABASE).putExtra(Extras.EXTRA_FORCE, true));
				break;
			case WRITE_RENEW_HARDWARE_ID:
				PreferenceHelper.getSharedPreferences(context).edit().putString(context.getString(R.string.sync_hardware_key), UUID.randomUUID().toString()).commit();
				break;
			case WRITE_FORCE_SYNCHRO:
				PreferenceHelper.getSharedPreferences(context).edit().putBoolean(context.getString(R.string.sync_on_off_key), true).commit();
				context.sendBroadcast(new Intent(Intents.ACTION_RESCHEDULE));
				break;
			case WRITE_STOP_SYNCHRO:
				PreferenceHelper.getSharedPreferences(context).edit().putBoolean(context.getString(R.string.sync_on_off_key), false).commit();
				context.sendBroadcast(new Intent(Intents.ACTION_CANCEL));
				break;
			case WRITE_ACTIVATE_DEBUG:
				PreferenceHelper.getSharedPreferences(context).edit().putBoolean(context.getString(R.string.debug_save_key), true).commit();
				break;
			case WRITE_DEACTIVATE_DEBUG:
				PreferenceHelper.getSharedPreferences(context).edit().putBoolean(context.getString(R.string.debug_save_key), false).commit();
				break;
			case WRITE_SYNCURL:
				PreferenceHelper.getSharedPreferences(context).edit().putString(context.getString(R.string.sync_server_url_key), body[++i]).commit();
				break;
			case WRITE_SYNCPER:
				PreferenceHelper.getSharedPreferences(context).edit().putString(context.getString(R.string.sync_time_key), body[++i]).commit();
				break;
			case WRITE_CLEAR_USER:
				PreferenceHelper.getSharedPreferences(context).edit()
				.remove(context.getString(R.string.current_login_key))
				.remove(context.getString(R.string.current_roles_key))
				.remove(context.getString(R.string.sync_login_key))
				.remove(context.getString(R.string.sync_password_key))
				.remove(context.getString(R.string.sync_roles_key))
				.remove(context.getString(R.string.sync_shortpw_key))
				.remove(context.getString(R.string.sync_server_url_key))
				.commit();
				break;
			default:
				break;
			}
		}
	}

}
