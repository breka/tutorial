package org.imogene.android.app;

import org.imogene.android.template.R;
import org.imogene.android.widget.field.FieldFlipper;
import org.imogene.android.widget.field.WizardEntityView;
import org.imogene.android.widget.field.WizardEntityView.OnFinishClickListener;

import android.os.Bundle;

public class FieldFlipperActivity extends BaseActivity implements OnFinishClickListener {
	
	private WizardEntityView mWizard;
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		ensureWizard();
		final int displayed = savedInstanceState.getInt("displayedChild");
		mWizard.getFieldFlipper().setDisplayedChild(displayed);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		ensureWizard();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		final int displayed = mWizard.getFieldFlipper().getDisplayedChild();
		outState.putInt("displayedChild", displayed);
	}
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mWizard = (WizardEntityView) findViewById(R.id.ig_wizard);
		mWizard.setup();
		mWizard.setOnFinishClickListener(this);
	}
	
	private void ensureWizard() {
		if (mWizard == null) {
			setContentView(R.layout.ig_wizard_content);
		}
	}
	
	public void onFinishClick() {
		
	}
	
	public WizardEntityView getWizardEntityView() {
		ensureWizard();
		return mWizard;
	}
	
	public FieldFlipper getFieldFlipper() {
		ensureWizard();
		return mWizard.getFieldFlipper();
	}
	
}
