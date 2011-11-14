package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.widget.field.BaseField;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BaseFieldView<T> extends BaseField<T> {
	
	private final ImageView mIconView;
	
	public BaseFieldView(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs, layoutId);
		mIconView = (ImageView) findViewById(W.id.icon);
		
		if (mIconView != null) {
			mIconView.setSaveEnabled(false);
		}
	}
	
	protected boolean isEmpty() {
		return false;
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final boolean visible = !isEmpty() && isDependentVisible();
		setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public void setIconId(int iconId) {
		if (mIconView != null) {
			if (iconId > 0) {
				mIconView.setImageResource(iconId);
			}
			mIconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
		}
	}

}
