package org.imogene.android.receiver;

import java.util.UUID;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.app.HighPreferences;
import org.imogene.android.app.UnSyncDialog;
import org.imogene.android.common.SyncHistory;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.content.ContentUrisUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class ServicingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			boolean sync = PreferenceHelper.getSynchronizationStatus(context);
			if (sync) {
				context.sendBroadcast(new Intent(Intents.ACTION_RESCHEDULE));
			}
		} else if (Intent.ACTION_CONFIGURATION_CHANGED.equals(action)) {
			FormatHelper.updateFormats();
		} else if (Intents.ACTION_SECRET_CODE.equals(action)) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setClass(context, HighPreferences.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (Intents.ACTION_RM_SYNC_HISTORY.equals(intent.getAction())) {
			SQLiteWrapper.delete(context, SyncHistory.Columns.TABLE_NAME, null, null);
		} else if (Intents.ACTION_RM_DATABASE.equals(intent.getAction())) {
			if (intent.hasExtra(Extras.EXTRA_FORCE)) {
				deleteAll(context);
			} else {
				if (hasUnSync(context)) {
					context.startActivity(new Intent(context,
							UnSyncDialog.class)
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				} else {
					deleteAll(context);
				}
			}
		}
	}
	
	private static final String SQLITE_MASTER = "sqlite_master";
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private static final String KEY_SQL = "sql";
	private static final String TYPE_TABLE = "table";
	private static final String ANDROID_METADATA = "android_metadata";
	private static final String[] COL = new String[] {KEY_NAME};
	
	private void deleteAll(Context context) {
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.setAnd(true);
		builder.appendEq(KEY_TYPE, TYPE_TABLE);
		
		Cursor c = SQLiteWrapper.query(context, SQLITE_MASTER, COL, builder.toSQL());
		while (c.moveToNext()) {
			String table = c.getString(0);
			if (!ANDROID_METADATA.equals(table)) {
				SQLiteWrapper.delete(context, table, null, null);
			}
		}
		c.close();
		
		context.getContentResolver().notifyChange(ContentUrisUtils.buildUriForFragment(null), null);
		PreferenceHelper.getSharedPreferences(context).edit().putString(context.getString(W.string.sync_hardware_key), UUID.randomUUID().toString()).commit();
	}

	private boolean hasUnSync(Context context) {
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.setAnd(true);
		builder.appendLike(KEY_SQL, Entity.Columns.SYNCHRONIZED);
		builder.appendEq(KEY_TYPE, TYPE_TABLE);
		
		Cursor c = SQLiteWrapper.query(context, SQLITE_MASTER, new String[] {KEY_NAME}, builder.toSQL());
		SQLiteBuilder b = new SQLiteBuilder();
		b.setSelect("count(*)");
		b.appendEq(Entity.Columns.SYNCHRONIZED, 0);
		while(c.moveToNext()) {
			long count = SQLiteWrapper.queryForLong(context, b.setTable(c.getString(0)).toSQL());
			if (count > 0) {
				c.close();
				return true;
			}
		}
		c.close();
		return false;
	}

}
