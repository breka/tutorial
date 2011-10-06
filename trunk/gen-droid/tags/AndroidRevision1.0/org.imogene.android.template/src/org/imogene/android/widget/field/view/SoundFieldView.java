package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.util.AttributeSet;

public class SoundFieldView extends BinaryFieldView {

	public SoundFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public int getDisplayId() {
		return W.string.bin_audio;
	}

}
