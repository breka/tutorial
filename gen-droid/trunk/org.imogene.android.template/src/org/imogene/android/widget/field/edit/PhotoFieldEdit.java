package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.Constants.Categories;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

public class PhotoFieldEdit extends BinaryFieldEdit {

	public PhotoFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected int displayId() {
		return W.string.bin_photo;
	}
	
	@Override
	protected void onCreateIntent(Intent acquire) {
		acquire.setType("image/*");
		acquire.addCategory(Categories.CATERGORY_IMAGE_CAPTURE);
	}

}
