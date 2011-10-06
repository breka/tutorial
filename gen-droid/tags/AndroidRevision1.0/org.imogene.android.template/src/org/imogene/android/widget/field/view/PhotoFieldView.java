package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.util.AttributeSet;

public class PhotoFieldView extends BinaryFieldView {

	public PhotoFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public int getDisplayId() {
		return W.string.bin_photo;
	}

}
