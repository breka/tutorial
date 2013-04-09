package org.imogene.android.database.sqlite;

import java.util.ArrayList;

import org.imogene.android.database.sqlite.SQLiteWhere.Separator;

import android.os.Parcel;
import android.os.Parcelable;

public class SQLiteBuilder implements Parcelable {

	private Separator mSeparator;
	private String mTable;
	private String[] mSelect;
	private final ArrayList<String> mWhere = new ArrayList<String>();
	
	private SQLiteBuilder(Parcel in) {
		mSelect = in.createStringArray();
		mTable = in.readString();
		mSeparator = Separator.values()[in.readInt()];
		in.readStringList(mWhere);
	}

	public SQLiteBuilder(String table, String... select) {
		this();
		mTable = table;
		mSelect = select;
	}

	public SQLiteBuilder() {
		mSeparator = Separator.AND;
	}

	public SQLiteBuilder setAnd(boolean and) {
		mSeparator = and ? Separator.AND : Separator.OR;
		return this;
	}

	public SQLiteBuilder setOr(boolean or) {
		return setAnd(!or);
	}

	public SQLiteBuilder setTable(String table) {
		mTable = table;
		return this;
	}

	public SQLiteBuilder setSelect(String... select) {
		mSelect = select;
		return this;
	}

	public SQLiteBuilder setSelectInTable(String table, String... select) {
		mTable = table;
		mSelect = select;
		return this;
	}

	public SQLiteBuilder appendWhere(String where) {
		mWhere.add(where);
		return this;
	}
	
	public SQLiteBuilder appendEq(String property, int value) {
		return appendWhere(property + "=" + value);
	}

	public SQLiteBuilder appendEq(String property, String value) {
		return appendWhere(property + "='" + value + "'");
	}

	public SQLiteBuilder appendNotEq(String property, String value) {
		return appendWhere(property + "!='" + value + "'");
	}

	public SQLiteBuilder appendIsNull(String property) {
		return appendWhere(property+" IS NULL");
	}
	
	public SQLiteBuilder appendIsNotNull(String property) {
		return appendWhere(property+" IS NOT NULL");
	}
	
	public SQLiteBuilder appendLike(String property, String value) {
		return appendWhere(property+" like '%"+value+"%'");
	}

	public SQLiteBuilder appendIn(String property, SQLiteRequest select) {
		if (select instanceof SQLiteSelect)
			return appendWhere(property + " in (" + select.toSQL() + ")");
		return this;
	}
	
	public SQLiteBuilder appendIn(String property, long... list) {
		if (list != null && list.length > 0) {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (int i = 0; i < list.length; i++) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}
				builder.append(list[i]);
			}
			return appendWhere(property + " in (" + builder.toString() + ")");
		}
		return this;
	}
	
	public SQLiteBuilder appendIn(String property, String... list) {
		if (list != null && list.length > 0) {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (int i = 0; i < list.length; i++) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}
				builder.append("'"+list[i]+"'");
			}
			return appendWhere(property + " in (" + builder.toString() + ")");
		}
		return this;
	}

	public SQLiteBuilder appendNotIn(String property, SQLiteRequest select) {
		if (select instanceof SQLiteSelect)
			return appendWhere(property + " not in (" + select.toSQL() + ")");
		return this;
	}
	
	public SQLiteBuilder appendInf(String property, double value) {
		return appendWhere(property + "<" + value);
	}
	
	public SQLiteBuilder appendSup(String property , double value) {
		return appendWhere(property + ">" + value);
	}

	public SQLiteBuilder appendWhere(SQLiteRequest where) {
		if (where instanceof SQLiteWhere)
			return appendWhere("(" + where.toSQL() + ")");
		return this;
	}

	public SQLiteBuilder reset() {
		mSeparator = Separator.AND;
		mSelect = null;
		mTable = null;
		mWhere.clear();
		return this;
	}

	public SQLiteRequest create() {
		if (mTable != null && mSelect != null) {
			return new SQLiteSelect(mTable, new SQLiteWhere(mWhere, mSeparator), mSelect);
		} else {
			return new SQLiteWhere(mWhere, mSeparator);
		}
	}
	
	public String toSQL() {
		return create().toSQL();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(mSelect);
		dest.writeString(mTable);
		dest.writeInt(mSeparator.ordinal());
		dest.writeStringList(mWhere);
	}

	public static final Parcelable.Creator<SQLiteBuilder> CREATOR = new Parcelable.Creator<SQLiteBuilder>() {
		public SQLiteBuilder createFromParcel(Parcel in) {
			return new SQLiteBuilder(in);
		}

		public SQLiteBuilder[] newArray(int size) {
			return new SQLiteBuilder[size];
		}
	};

}
