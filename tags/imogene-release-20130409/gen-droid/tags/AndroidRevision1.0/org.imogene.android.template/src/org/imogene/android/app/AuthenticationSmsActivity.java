package org.imogene.android.app;

import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.telephony.SmsHelper;
import org.imogene.android.util.base64.Base64;
import org.imogene.android.util.encryption.EncryptionManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class AuthenticationSmsActivity extends Activity implements OnClickListener, OnCancelListener {
	
	private static final String EXTRA_LOGIN = "Authentication_login";
	private static final String EXTRA_SERVER_PHONE = "Authentication_serverPhone";
	private static final String EXTRA_SERVER_CODE = "Authentication_serverCode";
	
	private static final int DIALOG_AUTHING_ID = 1;
	private static final int DIALOG_AUTH_FAILED_ID = 2;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("failed")) {
				onAuthFailed();				
			} else {
				onAuthSucceed();
			}
		}
	};
	
	private final BroadcastReceiver mSentReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
				onAuthFailed();
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
				onAuthFailed();
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
				onAuthFailed();
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
				onAuthFailed();
				break;
			}
		}
	};
	
	private final BroadcastReceiver mDeliveredReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
				onAuthFailed();
				break;
			}
		}
	};
	
	private final BroadcastReceiver mResponseReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String server = mServerPhone.substring(1);
			SmsMessage[] messages = SmsHelper.getMessagesFromIntent(intent);
			for (SmsMessage message : messages) {
				if (message.getOriginatingAddress().endsWith(server)) {
					String body = message.getMessageBody();
					if (body.startsWith("Code correct")) {
						Toast.makeText(context, "Registration successfull", Toast.LENGTH_SHORT).show();
						onAuthSucceed();
					} else {
						Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
						onAuthFailed();
					}
				}
			}			
		}
	};
	
	public static final Intent getAuthenticationIntent(Context context, String login, String serverPhone, String serverCode) {
		Intent intent = new Intent(context, AuthenticationSmsActivity.class);
		intent.putExtra(EXTRA_LOGIN, login);
		intent.putExtra(EXTRA_SERVER_PHONE, serverPhone);
		intent.putExtra(EXTRA_SERVER_CODE, serverCode);
		return intent;
	}
	
	private String mLogin;
	private String mServerPhone;
	private String mServerCode;
	
	private PendingIntent pi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mLogin = intent.getStringExtra(EXTRA_LOGIN);
		mServerPhone = intent.getStringExtra(EXTRA_SERVER_PHONE);
		mServerCode = intent.getStringExtra(EXTRA_SERVER_CODE);
		
		launchAuthentication();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(Intents.ACTION_SMS_AUTHENTICATION));
		registerReceiver(mSentReceiver, new IntentFilter(Intents.ACTION_SMS_AUTH_SENT));
		registerReceiver(mDeliveredReceiver, new IntentFilter(Intents.ACTION_SMS_AUTH_DELIVERED));
		registerReceiver(mResponseReceiver, new IntentFilter(Intents.ACTION_SMS_RECEIVED));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		unregisterReceiver(mSentReceiver);
		unregisterReceiver(mDeliveredReceiver);
		unregisterReceiver(mResponseReceiver);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTHING_ID:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getString(W.string.obtaining_roles));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(this);
			return dialog;
		case DIALOG_AUTH_FAILED_ID:
			return new AlertDialog.Builder(this)
			.setTitle(android.R.string.dialog_alert_title)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(W.string.obtaining_roles_failed)
			.setCancelable(false)
			.setPositiveButton(android.R.string.ok, this)
			.setNegativeButton(android.R.string.no, this)
			.create();
		default:
			return super.onCreateDialog(id);
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			launchAuthentication();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pi);
		
		setResult(RESULT_CANCELED);
		finish();
	}
	
	private void launchAuthentication() {
		String message = "register " + mServerCode + " " + mLogin;
		
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(Intents.ACTION_SMS_AUTH_SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(Intents.ACTION_SMS_AUTH_DELIVERED), 0);

		SmsManager.getDefault().sendTextMessage(mServerPhone, null, message, sentPI, deliveredPI);
		
		Intent intent = new Intent(Intents.ACTION_SMS_AUTHENTICATION);
		intent.putExtra("failed", true);
		pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 90 * 1000, pi);
		
		showDialog(DIALOG_AUTHING_ID);
	}
	
	private void onAuthSucceed() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pi);
		
		EncryptionManager em = EncryptionManager.getInstance(this);
		String encLogin = new String(Base64.encodeBase64(em.encrypt(mLogin.getBytes())));
		String encServer = new String(Base64.encodeBase64(em.encrypt(mServerPhone.getBytes())));
		String encCode = new String(Base64.encodeBase64(em.encrypt(mServerCode.getBytes())));
		PreferenceHelper.getSharedPreferences(this).edit()
		.putString(getString(W.string.sms_login_key), encLogin)
		.putString(getString(W.string.sms_server_phone_key), encServer)
		.putString(getString(W.string.sms_server_code_key), encCode)
		.commit();
		
		dismissDialog(DIALOG_AUTHING_ID);
		setResult(RESULT_OK);
		finish();
	}
	
	private void onAuthFailed() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pi);
		
		dismissDialog(DIALOG_AUTHING_ID);
		showDialog(DIALOG_AUTH_FAILED_ID);
	}
	
}
