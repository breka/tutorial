package org.imogene.android.common;

import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.util.FormatHelper;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class ClientFilter extends EntityImpl {
	
	public static class Columns implements Entity.Columns {
		public static final String PACKAGE = "org.imogene.uao.clientfilter.ClientFilter";
		public static final String TABLE_NAME = "clientfilter";
		public static final String TYPE = "CLTFIL";
		public static final Uri CONTENT_URI = FormatHelper.buildUriForFragment(TABLE_NAME);
		
		public static final String USERID = "userId";
		public static final String TERMINALID = "terminalId";
		public static final String CARDENTITY = "cardEntity";
		public static final String ENTITYFIELD = "entityField";
		public static final String OPERATOR = "operator";
		public static final String FIELDVALUE = "fieldValue";
		public static final String DISPLAY = "display";
		public static final String ISNEW = "isNew";
	}
	
	public static final String MULTIENUM_OPERATOR_EQUAL = "equalMultiEnum";
	public static final String STRING_OPERATOR_CONTAINS = "contains";
	public static final String STRING_OPERATOR_STARTWITH = "startsWith";
	public static final String STRING_OPERATOR_EQUAL = "equalString";
	public static final String STRING_OPERATOR_DIFF = "diffString";
	public static final String STRING_OPERATOR_INF = "infString";
	public static final String STRING_OPERATOR_SUP = "supString";
	public static final String DATE_OPERATOR_BEFORE = "before";
	public static final String DATE_OPERATOR_AFTER = "after";
	public static final String DATE_OPERATOR_EQUAL = "equalDate";
	public static final String INT_OPERATOR_EQUAL = "equalInt";
	public static final String INT_OPERATOR_SUP = "supInt";
	public static final String INT_OPERATOR_INF = "infInt";
	public static final String FLOAT_OPERATOR_EQUAL = "equalFloat";
	public static final String FLOAT_OPERATOR_SUP = "supFloat";
	public static final String FLOAT_OPERATOR_INF = "infFloat";
	public static final String RELATIONFIELD_OPERATOR_EQUAL = "equalRelationField";
	public static final String RELATIONFIELD_OPERATOR_EQUAL_NULL = "equalNull";
	public static final String BOOLEAN_OPERATOR_EQUAL = "equalBoolean";
	public static final String GEOFILTER_OPERATOR = "geoFilter";
	public static final String OPERATOR_ISNULL = "isNull";
	public static final String OPERATOR_UNDEF = "undef";

    /**
     * For user interface only, for db query, converted into a before and after
     */
    protected static final String DATE_OPERATOR_BETWEEN = "between";
    /**
     * For user interface only, for db query, converted into a before and after
     */
    protected static final String INT_OPERATOR_BETWEEN = "betweenInt";
    /**
     * For user interface only, for db query, converted into a before and after
     */
    protected static final String FLOAT_OPERATOR_BETWEEN = "betweenFloat";
	
    
	public interface Creator<T extends ClientFilter> {
		public T create(Context context, String userId,
				String terminalId, String entity, String field);
	}
	
	protected static abstract class DefaultCreator<T extends ClientFilter> implements Creator<T> {
		
		public final T create(Context context, String userId,
				String terminalId, String entity, String field) {
			String where = new SQLiteBuilder()
			.setAnd(true)
			.appendEq(Columns.USERID, userId)
			.appendEq(Columns.TERMINALID, terminalId)
			.appendEq(Columns.CARDENTITY, entity)
			.appendEq(Columns.ENTITYFIELD, field)
			.toSQL();
			
			T filter;
			ClientFilterCursor c = (ClientFilterCursor) SQLiteWrapper.query(context, Columns.CONTENT_URI, where, null);
			if (c.getCount() == 1) {
				c.moveToFirst();
				filter = newFilter(c);
			} else {
				filter = newFilter();
				filter.setUserId(userId);
				filter.setTerminalId(terminalId);
				filter.setCardEntity(entity);
				filter.setEntityField(field);
			}
			c.close();
			return filter;
		}
		
		protected abstract T newFilter();
		
		protected abstract T newFilter(ClientFilterCursor c);
	}

	private String mUserId = null;
	private String mTerminalId = null;
	private String mCardEntity = null;
	private String mEntityField = null;
	private String mOperator = null;
	private String mFieldValue = null;
	private String mDisplay = null;
	private Boolean mIsNew = null;

	public ClientFilter(ClientFilterCursor cursor) {
		init(cursor);
		mUserId = cursor.getUserId();
		mTerminalId = cursor.getTerminalId();
		mCardEntity = cursor.getCardEntity();
		mEntityField = cursor.getEntityField();
		mOperator = cursor.getOperator();
		mFieldValue = cursor.getFieldValue();
		mDisplay = cursor.getDisplay();
		mIsNew = cursor.getIsNew();
	}

	public ClientFilter() {
	}
	
	public final String getUserId() {
		return mUserId;
	}
	public final String getTerminalId() {
		return mTerminalId;
	}
	public final String getCardEntity() {
		return mCardEntity;
	}
	public final String getEntityField() {
		return mEntityField;
	}
	public final String getOperator() {
		return mOperator;
	}
	public final String getFieldValue() {
		return mFieldValue;
	}
	public final String getDisplay() {
		return mDisplay;
	}
	public final Boolean getIsNew() {
		return mIsNew;
	}

	public final void setUserId(String userId) {
		mUserId = userId;
	}
	public final void setTerminalId(String terminalId) {
		mTerminalId = terminalId;
	}
	public final void setCardEntity(String cardEntity) {
		mCardEntity = cardEntity;
	}
	public final void setEntityField(String entityField) {
		mEntityField = entityField;
	}
	public final void setOperator(String operator) {
		mOperator = operator;
	}
	public final void setFieldValue(String fieldValue) {
		mFieldValue = fieldValue;
	}
	public final void setDisplay(String display) {
		mDisplay = display;
	}
	public final void setIsNew(Boolean isNew) {
		mIsNew = isNew;
	}

	@Override
	protected final Uri getContentUri() {
		return Columns.CONTENT_URI;
	}

	@Override
	protected final String getBeanType() {
		return Columns.TYPE;
	}
	
	@Override
	protected void preCommit(Context context) {
		preCommit();
	}
	
	protected void preCommit() {
		mIsNew = Boolean.TRUE;
	}

	@Override
	protected final void addValues(Context context, ContentValues values) {
		values.put(Columns.USERID, mUserId);
		values.put(Columns.TERMINALID, mTerminalId);
		values.put(Columns.CARDENTITY, mCardEntity);
		values.put(Columns.ENTITYFIELD, mEntityField);
		values.put(Columns.OPERATOR, mOperator);
		values.put(Columns.FIELDVALUE, mFieldValue);
		values.put(Columns.DISPLAY, mDisplay);
		if (mIsNew != null)
			values.put(Columns.ISNEW, mIsNew.toString());
		else
			values.putNull(Columns.ISNEW);
	}

	public final void reset() {
		mUserId = null;
		mTerminalId = null;
		mCardEntity = null;
		mEntityField = null;
		mOperator = null;
		mFieldValue = null;
		mDisplay = null;
		mIsNew = null;
	}
	
	@Override
	public void prepareForSave(Context context) {
		prepareForSave(context, Columns.TYPE);
	}
	
	@Override
	public Uri saveOrUpdate(Context context) {
		return saveOrUpdate(context, Columns.TABLE_NAME);
	}
}
