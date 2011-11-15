package org.imogene.android.widget.field.edit;

import org.imogene.android.Constants.Categories;
import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class GeoFieldEdit extends BaseFieldEdit<Location> implements OnActivityResultListener {

	private final int mProvider;
	
	private int mRequestCode;

	public GeoFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_edit_buttons);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.GeoFieldEdit, 0, 0);
		mProvider = a.getInt(W.styleable.GeoFieldEdit_geoType, -1);
		a.recycle();
		findViewById(W.id.acquire).setOnClickListener(this);
		findViewById(W.id.delete).setOnClickListener(this);
		findViewById(W.id.view).setOnClickListener(this);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		findViewById(W.id.acquire).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(W.id.delete).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(W.id.view).setVisibility(readOnly ? View.GONE : View.VISIBLE);
	}
	
	@Override
	public void onAttachedToHierarchy(FieldManager manager) {
		super.onAttachedToHierarchy(manager);
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextId();
	}
	
	@Override
	public String getDisplay() {
		final Location location = getValue();
		if (location != null) {
			return FormatHelper.displayLocation(location);
		} else {
			return getEmptyText();
		}
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final Location location = getValue();
		if (location == null) {
			findViewById(W.id.acquire).setVisibility(View.VISIBLE);
			findViewById(W.id.delete).setVisibility(View.GONE);
			findViewById(W.id.view).setVisibility(View.GONE);
		} else {
			findViewById(W.id.acquire).setVisibility(View.GONE);
			findViewById(W.id.delete).setVisibility(View.VISIBLE);
			findViewById(W.id.view).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case W.id.acquire:
			Intent acquire = new Intent(Intents.ACTION_CAPTURE_GPS);
			switch (mProvider) {
			case 0:
				acquire.addCategory(Categories.CATEGORY_GPS);
				break;
			case 1:
				acquire.addCategory(Categories.CATEGORY_NETWORK);
				break;
			case 2:
				acquire.addCategory(Categories.CATEGORY_MAP);
				break;
			case 3:
				acquire.addCategory(Categories.CATEGORY_BEST);
				break;
			}
			getFieldManager().getActivity().startActivityForResult(acquire, mRequestCode);
			break;
		case W.id.delete:
			setValue(null);
			break;
		case W.id.view:
			Intent show = IntentUtils.createShowOnMapIntent(getValue());
			getContext().startActivity(show);
			break;
		}
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode && resultCode != Activity.RESULT_CANCELED) {
			setValue(FormatHelper.getLocationFromIntent(data));
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
		
		private Location value;

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
