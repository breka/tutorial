package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.widget.IntentChooserAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

public class GeoFieldView extends BaseFieldView<Location> implements OnClickListener {

	private Intent[] mIntents;
	
	public GeoFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		setOnClickListener(this);
		setIconId(android.R.drawable.ic_dialog_map);
	}
	
	@Override
	protected void dispatchClick(View v) {
		mIntents = new Intent[] {
			IntentUtils.createShowOnMapIntent(getValue()),
			IntentUtils.createShowRadarIntent(getValue()),
			IntentUtils.createNavigateToIntent(getValue())
		};
		
		Intent launch = IntentUtils.canLaunchWithoutChoose(getContext(), mIntents);
		if (launch != null) {
			getContext().startActivity(launch);
		} else {
			showDialog(null);
		}
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setAdapter(new IntentChooserAdapter(getContext(), mIntents), this);
	}
	
	public void onClick(DialogInterface dialog, int which) {
		IntentChooserAdapter adapter = (IntentChooserAdapter) ((AlertDialog) dialog).getListView().getAdapter();
		getContext().startActivity(adapter.intentForPosition(which));
	}
	
	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		final Location location = getValue();
		if (location != null) {
			return FormatHelper.displayLocation(location);
		}
		return super.getDisplay();
	}

}
