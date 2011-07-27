package org.imogene.android.preference;

import org.imogene.android.W;
import org.imogene.android.Constants.Categories;
import org.imogene.android.util.base64.Base64;
import org.imogene.android.util.encryption.EncryptionManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {
	
	public static final SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static final long getRealTime(Context context) {
		return System.currentTimeMillis() - getNtpOffset(context);
	}

	public static final long getNtpOffset(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(
				context.getString(W.string.ntp_offset_key), 0);
	}

	public static final String getNtpServerUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(W.string.ntp_server_key), "%Android_sntpServer%");
	}

	public static final String getShortPassword(Context context) {
		return getDecryptedString(context, context.getString(W.string.sync_shortpw_key));
	}

	public static final String getHardwareId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(W.string.sync_hardware_key), null);
	}

	public static final String getServerUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(W.string.sync_server_url_key),
						null);
	}
	
	public static final String getWebServiceUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(W.string.webservice_url_key), null);
	}

	public static final boolean getSynchronizationStatus(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(W.string.sync_on_off_key), false);
	}

	public static final long getSynchronizationPeriod(Context context) {
		return Long.parseLong(PreferenceManager.getDefaultSharedPreferences(
				context).getString(context.getString(W.string.sync_time_key),
				"15"));
	}

	public static final boolean getSynchronizationBidirectional(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(W.string.sync_bidirectional_key),
						true);
	}

	public static final long getSmsNumber(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(
				context.getString(W.string.debug_smsnb_key), 0);
	}

	public static final boolean isDebugActive(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(W.string.debug_save_key), false);
	}

	public static final String getEditionCategory(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(context.getString(W.string.app_wizard_key), false) ?
				Categories.CATEGORY_WIZARD : Categories.CATEGORY_CLASSIC;
	}
	
	public static final String getSyncLogin(Context context) {
		return getDecryptedString(context, context.getString(W.string.sync_login_key));
	}
	
	public static final String getSyncPassword(Context context) {
		return getDecryptedString(context, context.getString(W.string.sync_password_key));
	}
	
	public static final String getSyncRoles(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString(context.getString(W.string.sync_roles_key), null);
	}
	
	public static final boolean isMultilogin(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(W.string.multilogin), false);
	}
	
	public static final String getCurrentLogin(Context context) {
		if (isMultilogin(context)) {
			String login = getDecryptedString(context, context.getString(W.string.current_login_key));
			return login != null ? login : getSyncLogin(context);
		} else {
			return getSyncLogin(context);
		}
	}

	public static final String getCurrentRoles(Context context) {
		if (isMultilogin(context)) {
			String roles = PreferenceManager.getDefaultSharedPreferences(context)
			.getString(context.getString(W.string.current_roles_key), null);
			return roles != null ? roles : getSyncRoles(context);
		} else {
			return getSyncRoles(context);
		}
	}

	private static final String getDecryptedString(Context context, String key) {
		String enc = PreferenceManager.getDefaultSharedPreferences(context)
		.getString(key, null);
		return decrypt(context, enc);
	}
	
	private static final String decrypt(Context context, String value) {
		if (value != null)
			return new String(EncryptionManager.getInstance(context).decrypt(Base64.decodeBase64(value.getBytes())));
		return null;
	}
	
	public static final boolean isSetAdmin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.contains(context.getString(W.string.admin_login_key))
		&& sp.contains(context.getString(W.string.admin_password_key))
		&& sp.contains(context.getString(W.string.admin_roles_key));
	}
	
	public static final boolean isAdmin(Context context, String login, String password) {
		if (login != null & password != null & isSetAdmin(context)) {
			String adminLogin = getDecryptedString(context, context.getString(W.string.admin_login_key));
			String adminPassword = getDecryptedString(context, context.getString(W.string.admin_password_key));
			return login.equals(adminLogin) && password.equals(adminPassword);
		}
		return false;
	}
	
	public static final String getAdminRoles(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getString(context.getString(W.string.admin_roles_key), null);
	}
	
	public static final boolean isHttpAuthenticated(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(context.getString(W.string.http_authentication_key),
						true);
	}
	
	public static final String getSmsLogin(Context context) {
		return getDecryptedString(context, context.getString(W.string.sms_login_key));
	}
	
	public static final String getSmsServerPhone(Context context) {
		return getDecryptedString(context, context.getString(W.string.sms_server_phone_key));
	}

	public static final String getSmsServerCode(Context context) {
		return getDecryptedString(context, context.getString(W.string.sms_server_code_key));
	}
}
