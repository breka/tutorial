package org.imogene.android.widget.field.edit;

import org.imogene.android.Constants.Extras;
import org.imogene.android.W;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;
import org.imogene.android.widget.field.FieldManager.RelationManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class RelationOneFieldEdit extends RelationFieldEdit<Uri> implements OnActivityResultListener {
	
	public RelationOneFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public String getDisplay() {
		final Uri uri = getValue();
		final String result;
		if (uri != null) {
			EntityCursor cursor = (EntityCursor) SQLiteWrapper.query(getContext(), uri, null, null);
			cursor.moveToFirst();
			result = cursor.getMainDisplay(getContext());
			cursor.close();
		} else {
			result = getEmptyText();
		}
		return result;
	}
	
	@Override
	protected void dispatchClick(View v) {
		if (mType == 1 && mHasReverse && mOppositeCardinality == 1) {
			// forbidden case
			Toast.makeText(getContext(), W.string.relation_unsettable, Toast.LENGTH_LONG).show();
			return;
		}
		if (isReadOnly() && getValue() != null) {
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, getValue()));
			return;
		}
		if (mOppositeCardinality == 1 && !mHasReverse) {
			final Uri uri = getValue();
			if (uri != null) {
				getContext().startActivity(new Intent(Intent.ACTION_EDIT, uri));
			} else {
				Intent intent = new Intent(Intent.ACTION_INSERT, mContentUri);
				intent.putExtra(Extras.EXTRA_ENTITY, createBundle());
				intent.addCategory(PreferenceHelper.getEditionCategory(getContext()));
				getFieldManager().getActivity().startActivityForResult(intent, mRequestCode);
			}
			return;
		}
		super.dispatchClick(v);
	}
	
	@Override
	protected boolean onPrepareSQLBuilder(SQLiteBuilder builder) {
		if (mHasReverse && mOppositeCardinality == 1 && mType == 0) {
			final RelationManager mgr = getFieldManager().getRelationManager();
			final SQLiteBuilder request = new SQLiteBuilder(mTableName, mFieldName);
			request.appendNotEq(Entity.Columns._ID, mgr.getId());
			builder.appendNotIn(Entity.Columns._ID, request.create());
			return true;
		}
		return false;
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode && resultCode != Activity.RESULT_CANCELED) {
			final Uri uri = data.getData();
			if (!uri.equals(getValue())) {
				setValue(uri);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState myState = new SavedState(superState);
		myState.value = getValue();
		return myState;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		setValue(myState.value);
	}
	
	private static class SavedState extends BaseSavedState {
		
		private Uri value;

		public SavedState(Parcel source) {
			super(source);
			value = source.readParcelable(null);
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeParcelable(value, 0);
		}
		
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
			
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}
		};
		
	}

}
