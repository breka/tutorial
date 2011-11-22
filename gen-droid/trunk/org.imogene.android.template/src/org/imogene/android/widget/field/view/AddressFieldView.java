package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.widget.IntentChooserAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class AddressFieldView extends BaseFieldView<String> implements OnClickListener {

	private Intent[] mIntents;
	
	public AddressFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		setOnClickListener(this);
		setIconId(android.R.drawable.ic_dialog_map);
	}
	
	@Override
	protected void dispatchClick(View v) {
		mIntents = new Intent[] {
			new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + getValue())),
			new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + getValue()))
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
	public boolean isEmpty() {
		return TextUtils.isEmpty(getValue());
	}
	
	@Override
	protected String getDisplay() {
		return getValue();
	}

}
