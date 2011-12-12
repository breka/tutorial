package org.imogene.android.widget.field;

import java.util.ArrayList;

import org.imogene.android.template.R;
import org.imogene.android.widget.field.FieldManager.OnActivityDestroyListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseField<T> extends LinearLayout implements DependencyMatcher, OnDependencyChangeListener, OnClickListener, OnLongClickListener, OnDismissListener, OnActivityDestroyListener {
	
	private final TextView mValueView;
	private final TextView mTitleView;
	private final View mDependentView;

	private ArrayList<DependsOnEntry> mDependsOn;
	private FieldManager mManager;
	private DialogFactory mFactory;
	private Dialog mDialog;

	private boolean mDependent;
	private boolean mHidden;
	private int mTitleId;
	private int mEmptyTextId;

	private ArrayList<OnDependencyChangeListener> mDependents;

	private T mValue;

	public BaseField(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs);
		inflate(context, layoutId, this);
		
		mValueView = (TextView) findViewById(R.id.ig_value);
		if (mValueView == null) {
			throw new NullPointerException();
		}
		mValueView.setSaveEnabled(false);
		
		mTitleView = (TextView) findViewById(R.id.ig_title);
		if (mTitleView == null) {
			throw new NullPointerException();
		}
		mTitleView.setSaveEnabled(false);
		
		mDependentView = findViewById(R.id.ig_arrow);
		if (mDependentView != null) {
			mDependentView.setSaveEnabled(false);
		}
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseField, 0, 0);
		setEmptyTextId(a.getResourceId(R.styleable.BaseField_igEmptyText, R.string.ig_select));
		setTitleId(a.getResourceId(R.styleable.BaseField_igTitle, android.R.string.unknownName));
		setDependent(a.getBoolean(R.styleable.BaseField_igDependent, false));
		setHidden(a.getBoolean(R.styleable.BaseField_igHidden, false));
		a.recycle();
		
		inflate(context, R.layout.ig_divider_layout, this);
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(mHidden ? View.GONE : visibility);
	}
	
	public TextView getValueView() {
		return mValueView;
	}
	
	public boolean isHidden() {
		return mHidden;
	}
	
	public void setHidden(boolean hidden) {
		mHidden = hidden;
		setVisibility(hidden ? View.GONE : View.VISIBLE);
	}
	
	public void setTitleId(int titleId) {
		mTitleId = titleId;
		mTitleView.setText(titleId);
	}
	
	public int getTitleId() {
		return mTitleId;
	}
	
	public void setEmptyTextId(int emptyTextId) {
		mEmptyTextId = emptyTextId;
	}
	
	public String getEmptyText() {
		return getResources().getString(mEmptyTextId);
	}
	
	public void setDependent(boolean dependent) {
		mDependent = dependent;
		if (mDependentView != null) {
			mDependentView.setVisibility(dependent ? View.VISIBLE : View.GONE);
		}
	}
	
	public boolean isDependendent() {
		return mDependent;
	}
	
	public void setValue(T value) {
		mValue = value;
		onChangeValue();
		notifyDependencyChange();
	}
	
	public T getValue() {
		return mValue;
	}
	
	public boolean isEmpty() {
		return mValue == null;
	}
	
	protected FieldManager getFieldManager() {
		return mManager;
	}
	
	protected void setDialogFactory(DialogFactory factory) {
		mFactory = factory;
	}
	
	protected String getDisplay() {
		return getResources().getString(android.R.string.unknownName);
	}
	
	public void onAttachedToHierarchy(FieldManager manager) {
		mManager = manager;
	}
	
	public void registerDependent(OnDependencyChangeListener dependent, String dependencyValue) {
		if (mDependents == null) {
			mDependents = new ArrayList<OnDependencyChangeListener>();
		}
		
		mDependents.add(dependent);
		
		dependent.registerDependsOn(this, dependencyValue);
		
		dependent.onDependencyChanged();
	}
	
	public boolean matchesDependencyValue(String dependencyValue) {
		return mValue != null;
	}
	
	protected boolean isDependentVisible() {
		if (mDependent && mDependsOn != null) {
			for (DependsOnEntry entry : mDependsOn) {
				if (!entry.first.matchesDependencyValue(entry.second)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void onDependencyChanged() {
		final boolean visible = isDependentVisible();
		setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public void registerDependsOn(DependencyMatcher matcher, String dependencyValue) {
		if (mDependsOn == null) {
			mDependsOn = new ArrayList<DependsOnEntry>();
		}
		
		mDependsOn.add(new DependsOnEntry(matcher, dependencyValue));
	}
	
	protected void onChangeValue() {
		mValueView.setText(getDisplay());
	}
	
	private void notifyDependencyChange() {
		final ArrayList<OnDependencyChangeListener> dependents = mDependents;
		
		if (dependents == null) {
			return;
		}
		
		final int size = dependents.size();
		for (int i = 0 ; i < size; i++) {
			final OnDependencyChangeListener listener = dependents.get(i);
			listener.onDependencyChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		dispatchClick(v);
	}
	
	protected void dispatchClick(View v) {
		
	}
	
	public boolean onLongClick(View v) {
		setValue(null);
		return true;
	}
	
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		
	}

	protected void showDialog(Bundle state) {
		final Dialog dialog;
		if (mFactory == null) {
			final Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(mTitleId);
		
			onPrepareDialogBuilder(builder);
		
			getFieldManager().registerOnActivityDestroyListener(this);
			dialog = mDialog = builder.create();
		} else {
			getFieldManager().registerOnActivityDestroyListener(this);
			dialog = mDialog = mFactory.createDialog();
		}
		if (state != null) {
			dialog.onRestoreInstanceState(state);
		}
		dialog.setOnDismissListener(this);
		dialog.show();
	}
	
	public void onDismiss(DialogInterface dialog) {
		mManager.unregisterOnActivityDestroyListener(this);
		if (dialog.equals(mDialog)) {
			mDialog = null;
		}
	}
	
	public void onActivityDestroy() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final boolean hasDialog = mDialog != null && mDialog.isShowing();
		if (!hasDialog) {
			return superState;
		}
		
		final SavedState myState = new SavedState(superState);
		myState.isDialogShowing = hasDialog;
		myState.dialogBundle = hasDialog ? mDialog.onSaveInstanceState() : null;
		return myState;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}
		
		final SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		if (myState.isDialogShowing) {
			showDialog(myState.dialogBundle);
		}
	}
	
	private static class SavedState extends BaseSavedState {
		
		private boolean isDialogShowing;
		private Bundle dialogBundle;
		
		public SavedState(Parcel source) {
			super(source);
			isDialogShowing = source.readInt() == 1;
			dialogBundle = source.readBundle();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isDialogShowing ? 1 : 0);
			dest.writeBundle(dialogBundle);
		}
		
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
			
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}
		};
		
	}
	
	public interface DialogFactory {
		public Dialog createDialog();
	}
	
	private static class DependsOnEntry extends Pair<DependencyMatcher, String> {
		
		public DependsOnEntry(DependencyMatcher matcher, String dependencyValue) {
			super(matcher, dependencyValue);
		}
	}

}
