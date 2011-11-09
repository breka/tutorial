package org.imogene.android.widget.field.edit;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.W;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.widget.field.FieldManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class RelationManyFieldEdit extends RelationFieldEdit<ArrayList<Uri>> {
	
	public RelationManyFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		setValue(new ArrayList<Uri>());
	}
	
	@Override
	public void onAttachedToHierarchy(FieldManager manager) {
		super.onAttachedToHierarchy(manager);
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextId();
	}
	
	@Override
	public void setValue(ArrayList<Uri> value) {
		if (value == null) {
			super.setValue(new ArrayList<Uri>());
		} else {
			super.setValue(value);
		}
	}
	
	@Override
	public boolean isValid() {
		final ArrayList<Uri> list = getValue();
		return isRequired() ? (list != null && !list.isEmpty()) : true;
	}
	
	public void setContentUri(Uri contentUri) {
		mContentUri = contentUri;
	}

	@Override
	public String getDisplay() {
		final ArrayList<Uri> uris = getValue();
		if (uris != null && uris.size() > 0) {
			if (uris.size() > 1) {
				return uris.size() + " " + getResources().getString(mDisplayPlId);
			} else {
				return "1 " + getResources().getString(mDisplaySgId);
			}
		} else {
			return getResources().getString(W.string.select);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		if (isReadOnly()) {
			final ArrayList<Uri> list = getValue();
			
			if (list == null || list.size() == 0) {
				return;
			}
			
			final int size = list.size();
			if (size == 1) {
				getContext().startActivity(new Intent(Intent.ACTION_VIEW, list.get(0)));
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW, mContentUri);
				long[] ids = new long[size];
				for (int i = 0; i < size; i++) {
					ids[i] = Long.parseLong(list.get(i).getLastPathSegment());
				}
				SQLiteBuilder builder = new SQLiteBuilder();
				builder.appendIn(Entity.Columns._ID, ids);
				intent.putExtra(Extras.EXTRA_WHERE, builder);
				getContext().startActivity(intent);
			}
			return;
		}
		super.dispatchClick(v);
	}
	
	@Override
	protected void onPrepareIntent(Intent intent) {
		final ArrayList<Uri> list = getValue();
		if (list.size() > 0) {
			intent.putParcelableArrayListExtra(Extras.EXTRA_SELECTED, list);
		}
		intent.putExtra(Extras.EXTRA_MULTIPLE, true);
	}
	
	@Override
	protected boolean onPrepareSQLBuilder(SQLiteBuilder builder) {
		if (mHasReverse && mOppositeCardinality == 1) {
			SQLiteBuilder where = new SQLiteBuilder();
			where.setOr(true);
			where.appendEq(mOppositeRelationField, getFieldManager().getRelationManager().getId());
			where.appendIsNull(mOppositeRelationField);
			builder.appendWhere(where.create());
			return true;
		}
		return false;
	}

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode && resultCode != Activity.RESULT_CANCELED) {
			final ArrayList<Uri> result = data.getParcelableArrayListExtra(Extras.EXTRA_SELECTED);
			setValue(result);
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
		
		private ArrayList<Uri> value;

		public SavedState(Parcel source) {
			super(source);
			value = source.createTypedArrayList(Uri.CREATOR);
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeTypedList(value);
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
