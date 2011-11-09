package org.imogene.android.widget;

import org.imogene.android.W;
import org.imogene.android.database.interfaces.EntityCursor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EntityCursorAdapter extends CursorAdapter {

	private final Drawable mColor;
	private final int mChoiceMode;

	public EntityCursorAdapter(Context context, EntityCursor c,
			Drawable color, int choiceMode) {
		super(context, c);
		mColor = color;
		mChoiceMode = choiceMode;
	}

	public EntityCursorAdapter(Context context, EntityCursor c, Drawable color) {
		this(context, c, color, ListView.CHOICE_MODE_NONE);
	}
	
	public String getItemStringId(int position) {
		EntityCursor c = (EntityCursor) getItem(position);
		if (c != null) {
			return c.getId();
		}
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		EntityCursor c = (EntityCursor) cursor;

		view.findViewById(W.id.list_color).setBackgroundDrawable(mColor);

		TextView main = (TextView) view.findViewById(W.id.list_main);
		TextView secondary = (TextView) view.findViewById(W.id.list_secondary);

		main.setText(null);
		secondary.setText(null);

		if (c.getUnread()) {
			view.setBackgroundResource(W.drawable.list_selector_background_inverse);
			main.setTextAppearance(context, android.R.style.TextAppearance_Medium_Inverse);
			main.setTypeface(Typeface.DEFAULT_BOLD);
			secondary.setTextAppearance(context, android.R.style.TextAppearance_Small_Inverse);
			secondary.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			view.setBackgroundResource(android.R.drawable.list_selector_background);
			main.setTextAppearance(context, android.R.style.TextAppearance_Medium);
			main.setTypeface(Typeface.DEFAULT);
			secondary.setTextAppearance(context, android.R.style.TextAppearance_Small);
			secondary.setTypeface(Typeface.DEFAULT);
		}

		if (mChoiceMode == ListView.CHOICE_MODE_NONE) {
			ImageView icon = (ImageView) view.findViewById(W.id.list_icon);
			if (icon != null) {
				icon.setImageResource(android.R.drawable.stat_notify_sync);
				icon.setVisibility(c.getSynchronized() ? View.GONE : View.VISIBLE);
			}
		}

		main.setText(c.getMainDisplay(context));
		String sec = c.getSecondaryDisplay(context);
		if (TextUtils.isEmpty(sec.trim())) {
			secondary.setVisibility(View.GONE);
		} else {
			secondary.setVisibility(View.VISIBLE);
			secondary.setText(sec);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater	.inflate(mChoiceMode != ListView.CHOICE_MODE_MULTIPLE ? W.layout.entity_row	: W.layout.entity_row_multiple, parent, false);
	}
}
