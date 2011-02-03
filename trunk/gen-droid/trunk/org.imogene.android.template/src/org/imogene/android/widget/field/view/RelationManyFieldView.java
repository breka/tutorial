package org.imogene.android.widget.field.view;

import java.util.ArrayList;

import org.imogene.android.W;
import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Keys;
import org.imogene.android.database.sqlite.SQLiteBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class RelationManyFieldView extends FieldEntityView<ArrayList<Uri>> {
	
	private final int displayPlId;
	private final int displaySgId;
	
	private Uri contentUri;

	public RelationManyFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.relation_field_entity);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.RelationFieldEdit, 0, 0);
		displayPlId = a.getResourceId(W.styleable.RelationFieldEdit_displayPl, 0);
		displaySgId = a.getResourceId(W.styleable.RelationFieldEdit_displaySg, 0);
		a.recycle();
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_contact_card);
	}
	
	public void setContentUri(Uri uri) {
		contentUri = uri;
	}
	
	public void setDrawable(Drawable drawable) {
		final View color = findViewById(W.id.color);
		if (color != null) {
			color.setBackgroundDrawable(drawable);
		}
	}

	@Override
	protected boolean isEmpty() {
		final ArrayList<Uri> list = getValue();
		return list == null || list.size() == 0;
	}

	@Override
	public String getDisplay() {
		final ArrayList<Uri> uris = getValue();
		if (uris != null && uris.size() > 0) {
			if (uris.size() > 1) {
				return uris.size() + " " + getResources().getString(displayPlId);
			} else {
				return "1 " + getResources().getString(displaySgId);
			}
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
			long[] ids = new long[size];
			for (int i = 0; i < size; i++) {
				ids[i] = Long.parseLong(list.get(i).getPathSegments().get(1));
			}
			SQLiteBuilder builder = new SQLiteBuilder();
			builder.appendIn(Keys.KEY_ROWID, ids);
			intent.putExtra(Extras.EXTRA_WHERE, builder);
			getContext().startActivity(intent);
		}
	}

}
