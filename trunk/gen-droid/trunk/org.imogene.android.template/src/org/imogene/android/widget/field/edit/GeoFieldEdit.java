package org.imogene.android.widget.field.edit;

import org.imogene.android.Constants.Categories;
import org.imogene.android.Constants.Intents;
import org.imogene.android.template.R;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

public class GeoFieldEdit extends BaseFieldEdit<Location> implements OnActivityResultListener {

	private final int mProvider;
	
	private int mRequestCode;

	public GeoFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.ig_field_edit_buttons);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GeoFieldEdit, 0, 0);
		mProvider = a.getInt(R.styleable.GeoFieldEdit_igGeoType, -1);
		a.recycle();
		findViewById(R.id.ig_acquire).setOnClickListener(this);
		findViewById(R.id.ig_delete).setOnClickListener(this);
		findViewById(R.id.ig_view).setOnClickListener(this);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		findViewById(R.id.ig_acquire).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(R.id.ig_delete).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(R.id.ig_view).setVisibility(readOnly ? View.GONE : View.VISIBLE);
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
			return FormatHelper.displayLocation(getContext(), location);
		} else {
			return getEmptyText();
		}
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final Location location = getValue();
		if (location == null) {
			findViewById(R.id.ig_acquire).setVisibility(View.VISIBLE);
			findViewById(R.id.ig_delete).setVisibility(View.GONE);
			findViewById(R.id.ig_view).setVisibility(View.GONE);
		} else {
			findViewById(R.id.ig_acquire).setVisibility(View.GONE);
			findViewById(R.id.ig_delete).setVisibility(View.VISIBLE);
			findViewById(R.id.ig_view).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case R.id.ig_acquire:
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
		case R.id.ig_delete:
			setValue(null);
			break;
		case R.id.ig_view:
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
	
}
