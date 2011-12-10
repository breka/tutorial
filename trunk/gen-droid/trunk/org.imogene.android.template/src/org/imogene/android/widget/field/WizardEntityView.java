package org.imogene.android.widget.field;

import org.imogene.android.template.R;
import org.imogene.android.widget.field.FieldFlipper.Controller;
import org.imogene.android.widget.field.edit.BaseFieldEdit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class WizardEntityView extends RelativeLayout implements OnClickListener, Controller {
	
	public interface OnFinishClickListener {
		public void onFinishClick();
	}
	
	private OnFinishClickListener mListener;
	
	private FieldFlipper mFlipper;
	
	private View mNext;
	private View mPrevious;
	private View mFinish;
	
	public WizardEntityView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	public void setup() {
		mFlipper = (FieldFlipper) findViewById(R.id.flipper);
		
		mNext = findViewById(R.id.next_field);
		mPrevious = findViewById(R.id.previous_field);
		mFinish = findViewById(R.id.finish);
		
		mNext.setOnClickListener(this);
		mPrevious.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		
		mFlipper.setController(this);
	}
	
	public void setOnFinishClickListener(OnFinishClickListener listener) {
		mListener = listener;
	}
	
	public FieldFlipper getFieldFlipper() {
		return mFlipper;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_field:
			mFlipper.showNext();
			break;
		case R.id.previous_field:
			mFlipper.showPrevious();
			break;
		case R.id.finish:
			if (mListener != null) {
				mListener.onFinishClick();
			}
			break;
		}
	}
	
	public void onFieldChanged() {
		final BaseFieldEdit<?> displayed = mFlipper.getCurrentField();
		findViewById(R.id.previous_field).setVisibility(mFlipper.hasPrevious() ? View.VISIBLE : View.GONE);
		findViewById(R.id.next_field).setEnabled(displayed.isValid());
		final boolean hasNext = mFlipper.hasNext();
		findViewById(R.id.next_field).setVisibility(hasNext ? View.VISIBLE : View.GONE);
		findViewById(R.id.finish).setVisibility(hasNext ? View.GONE : View.VISIBLE);
	}
	
}
