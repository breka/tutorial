package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.util.field.FieldPattern;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BoundedIntegerFieldEdit extends BaseFieldEdit<Integer> implements
		DialogInterface.OnClickListener, OnSeekBarChangeListener {

	private SeekBar mSeekBar;
	private TextView mTextView;

	private int mMax;
	private int mMin;
	private String mUnit;

	public BoundedIntegerFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.NumberField, 0, 0);
		setMin(a.getInt(W.styleable.NumberField_intMin, 0));
		setMax(a.getInt(W.styleable.NumberField_intMax, 100));
		setUnit(a.getString(W.styleable.NumberField_unit));
		a.recycle();
	}

	public void setMin(int min) {
		mMin = min;
	}

	public void setMax(int max) {
		mMax = max;
	}

	public void setUnit(String unit) {
		mUnit = unit;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}

	@Override
	public String getDisplay() {
		final Integer value = getValue();
		if (value == null) {
			return getEmptyText();
		} else {
			if (TextUtils.isEmpty(mUnit)) {
				return value.toString();
			} else {
				return value + " " + mUnit;
			}
		}
	}

	@Override
	public boolean matchesDependencyValue(String value) {
		final Integer i = getValue();
		return i != null ? FieldPattern.matchesInt(value, i.intValue()) : false;
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		View view = LayoutInflater.from(getContext()).inflate(
				W.layout.field_edit_integer_bounded, null);

		mTextView = (TextView) view.findViewById(W.id.display_value);

		mSeekBar = (SeekBar) view.findViewById(W.id.seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setMax(mMax - mMin);

		final OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case W.id.decrease:
					mSeekBar.incrementProgressBy(-1);
					break;
				case W.id.increase:
					mSeekBar.incrementProgressBy(1);
					break;
				}
			}
		};

		view.findViewById(W.id.increase).setOnClickListener(listener);
		view.findViewById(W.id.decrease).setOnClickListener(listener);
		
		final Integer value = getValue();
		if (value != null) {
			mSeekBar.setProgress(getValue() - mMin);
		} else {
			mSeekBar.setProgress((mMax - mMin) / 2);
		}

		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, this);
		builder.setNeutralButton(android.R.string.cut, this);
		builder.setNegativeButton(android.R.string.cancel, null);
	}

	@Override
	public void dispatchClick(View v) {
		showDialog(null);
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			setValue(mSeekBar.getProgress() + mMin);
			break;
		case Dialog.BUTTON_NEUTRAL:
			setValue(null);
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mTextView.setText((progress + mMin) + " " + mUnit);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
