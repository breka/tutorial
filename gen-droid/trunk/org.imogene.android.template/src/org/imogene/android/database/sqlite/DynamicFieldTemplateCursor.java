package org.imogene.android.database.sqlite;

import org.imogene.android.domain.DynamicFieldTemplate;
import org.imogene.android.domain.DynamicFieldTemplate.Type;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;

public class DynamicFieldTemplateCursor extends ImogBeanCursorImpl {

	public DynamicFieldTemplateCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
			return new DynamicFieldTemplateCursor(db, masterQuery, editTable, query);
		}
	}

	@Override
	public DynamicFieldTemplate newBean() {
		return new DynamicFieldTemplate(this);
	}

	public final String getFieldName() {
		return getString(DynamicFieldTemplate.Columns.FIELDNAME);
	}

	public final Type getFieldType() {
		return Type.parseType(getString(DynamicFieldTemplate.Columns.FIELDTYPE));
	}

	public final String getParameters() {
		return getString(DynamicFieldTemplate.Columns.PARAMETERS);
	}

	public final String getFormType() {
		return getString(DynamicFieldTemplate.Columns.FORMTYPE);
	}

	public final Uri getTemplateCreator() {
		return getEntity(getString(DynamicFieldTemplate.Columns.TEMPLATECREATOR));
	}

	public final String getDescription() {
		return getString(DynamicFieldTemplate.Columns.DESCRIPTION);
	}

	public final String getReason() {
		return getString(DynamicFieldTemplate.Columns.REASON);
	}

	public final Boolean getIsDefault() {
		return getAsBoolean(DynamicFieldTemplate.Columns.ISDEFAULT);
	}

	public final Boolean getRequiredValue() {
		return getAsBoolean(DynamicFieldTemplate.Columns.REQUIREDVALUE);
	}

	public final Integer getFieldPosition() {
		return getAsInteger(DynamicFieldTemplate.Columns.FIELDPOSITION);
	}

	public final String getMinimumValue() {
		return getString(DynamicFieldTemplate.Columns.MINIMUMVALUE);
	}

	public final String getMaximumValue() {
		return getString(DynamicFieldTemplate.Columns.MAXIMUMVALUE);
	}

	public final String getDefaultValue() {
		return getString(DynamicFieldTemplate.Columns.DEFAULTVALUE);
	}

	public final Boolean getAllUsers() {
		return getAsBoolean(DynamicFieldTemplate.Columns.ALLUSERS);
	}

	public final Boolean getIsActivated() {
		return getAsBoolean(DynamicFieldTemplate.Columns.ISACTIVATED);
	}

	@Override
	public String getMainDisplay(Context context) {
		return null;
	}

	@Override
	public String getSecondaryDisplay(Context context) {
		return null;
	}

}
