package org.imogene.android.widget.field.view;

import org.imogene.android.template.R;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteWrapper;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class RelationOneFieldView extends BaseFieldView<Uri> {

	public RelationOneFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_relation);
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_contact_card);
	}
	
	public void setDrawable(Drawable drawable) {
		final View color = findViewById(R.id.color);
		if (color != null) {
			color.setBackgroundDrawable(drawable);
		}
	}
	
	@Override
	public String getDisplay() {
		final Uri uri = getValue();
		if (uri != null) {
			final String result;
			EntityCursor cursor = (EntityCursor) SQLiteWrapper.query(getContext(), uri, null, null);
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
