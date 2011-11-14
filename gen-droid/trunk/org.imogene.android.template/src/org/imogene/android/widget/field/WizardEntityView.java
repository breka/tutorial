package org.imogene.android.widget.field;

import org.imogene.android.W;
import org.imogene.android.widget.field.FieldFlipper.Controller;
import org.imogene.android.widget.field.edit.BaseFieldEdit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WizardEntityView extends RelativeLayout implements OnClickListener, Controller {
	
	public interface OnFinishClickListener {
		public void onFinishClick();
	}
	
	private OnFinishClickListener mListener;
	
	private FieldFlipper mFlipper;
	
	private View mNext;
	private View mPrevious;
	private View mFinish;
	
	private View mRequiredLayout;
	private View mHelpLayout;
	
	private TextView mHelp;
	
	public WizardEntityView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	public void setup() {
		mFlipper = (FieldFlipper) findViewById(W.id.flipper);
		
		mNext = findViewById(W.id.next_field);
		mPrevious = findViewById(W.id.previous_field);
		mFinish = findViewById(W.id.finish);
		
		mRequiredLayout = findViewById(W.id.required_layout);
		mHelpLayout = findViewById(W.id.help_layout);
		
		mHelp = (TextView) mHelpLayout.findViewById(W.id.help);
		
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
		case W.id.next_field:
			mFlipper.showNext();
			break;
		case W.id.previous_field:
			mFlipper.showPrevious();
			break;
		case W.id.finish:
			if (mListener != null) {
				mListener.onFinishClick();
			}
			break;
		}
	}
	
	public void onFieldChanged() {
		final BaseFieldEdit<?> displayed = mFlipper.getCurrentField();
		findViewById(W.id.previous_field).setVisibility(mFlipper.hasPrevious() ? View.VISIBLE : View.GONE);
		findViewById(W.id.next_field).setEnabled(displayed.isValid());
		final boolean hasNext = mFlipper.hasNext();
		findViewById(W.id.next_field).setVisibility(hasNext ? View.VISIBLE : View.GONE);
		findViewById(W.id.finish).setVisibility(hasNext ? View.GONE : View.VISIBLE);
		mRequiredLayout.setVisibility(displayed.isRequired() ? View.VISIBLE : View.INVISIBLE);
		final int helpId = displayed.getHelpId();
		if (helpId != 0) {
			mHelpLayout.setVisibility(View.VISIBLE);
			mHelp.setText(helpId);
		} else {
			mHelpLayout.setVisibility(View.GONE);
		}
	}
	
}
