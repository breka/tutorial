package org.imogene.android.util.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.imogene.android.Constants.Databases;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Paths;
import org.imogene.android.Constants.Sync;
import org.imogene.android.database.sqlite.SQLiteBuilder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class DatabaseUtils {
	
	public static SQLiteBuilder computeWhere(SQLiteBuilder builder) {
		SQLiteBuilder result = new SQLiteBuilder();
		if (builder != null)
			result.appendWhere(builder.create());
		return result.appendNotEq(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
	}
	
	public static void markAs(ContentResolver res, Uri uri, boolean unread) {
		ContentValues values = new ContentValues();
		values.put(Keys.KEY_UNREAD, unread ? 1 : 0);
		res.update(uri, values, null, null);
	}

	public static void dbBackup(Context context) {
		Paths.PATH_BACKUP.mkdirs();

		String name = "backup_" + System.currentTimeMillis() + ".db";
		File backup = new File(Paths.PATH_BACKUP, name);
		File db = context.getDatabasePath(Databases.DATABASE_NAME);

		try {
			FileInputStream fis = new FileInputStream(db);
			FileOutputStream fos = new FileOutputStream(backup);

			byte[] bytes = new byte[1024];
			while (fis.read(bytes) != -1) {
				fos.write(bytes);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			// Maybe next time
		}
	}
}
