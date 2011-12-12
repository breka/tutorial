package org.imogene.android.widget.field.view;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.template.R;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.SQLiteBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class RelationManyFieldView extends BaseFieldView<ArrayList<Uri>> {
	
	private final int displayId;
	
	private Uri contentUri;

	public RelationManyFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.ig_field_relation);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelationField, 0, 0);
		displayId = a.getResourceId(R.styleable.RelationField_igDisplay, 0);
		a.recycle();
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_contact_card);
	}
	
	public void setContentUri(Uri uri) {
		contentUri = uri;
	}
	
	public void setDrawable(Drawable drawable) {
		final View color = findViewById(R.id.ig_color);
		if (color != null) {
			color.setBackgroundDrawable(drawable);
		}
	}

	@Override
	public boolean isEmpty() {
		final ArrayList<Uri> list = getValue();
		return list == null || list.size() == 0;
	}

	@Override
	public String getDisplay() {
		final ArrayList<Uri> uris = getValue();
		if (uris != null && !uris.isEmpty()) {
			int size = uris.size();
			return getResources().getQuantityString(displayId, size, size);
		}
		return super.getDisplay();
	}
	
	@Override
	protected void dispatchClick(View v) {
		final ArrayList<Uri> list = getValue();
		
		if (list == null || list.size() == 0) {
			return;
		}
		
		final int size = list.size();
		if (size == 1) {
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, list.get(0)));
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);
			String[] ids = new String[size];
			for (int i = 0; i < size; i++) {
				ids[i] = list.get(i).getLastPathSegment();
			}
			SQLiteBuilder builder = new SQLiteBuilder();
			builder.appendIn(Entity.Columns._ID, ids);
			intent.putExtra(Extras.EXTRA_WHERE, builder);
			getContext().startActivity(intent);
		}
	}

}
