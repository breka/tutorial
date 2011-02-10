package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class RelationOneFieldView extends FieldEntityView<Uri> {

	public RelationOneFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.relation_field_entity);
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_contact_card);
	}
	
	public void setDrawable(Drawable drawable) {
		final View color = findViewById(W.id.color);
		if (color != null) {
			color.setBackgroundDrawable(drawable);
		}
	}
	
	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		final Uri uri = getValue();
		if (uri != null) {
			final String result;
			EntityCursor cursor = AbstractDatabase.getSuper(getContext()).query(uri, null, null);
			cursor.moveToFirst();
			result = cursor.getMainDisplay(getContext());
			cursor.close();
			return result;
		}
		return super.getDisplay();
	}
	
	@Override
	protected void dispatchClick(View v) {
		final Uri uri = getValue();
		if (uri != null) {
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
		}
	}

}
