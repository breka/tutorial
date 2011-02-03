package org.imogene.android.widget.field;

import java.util.ArrayList;

import org.imogene.android.W;
import org.imogene.android.widget.field.FieldManager.OnActivityDestroyListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FieldEntity<T> extends LinearLayout implements DependencyMatcher, OnDependencyChangeListener, OnClickListener, OnLongClickListener, OnDismissListener, OnActivityDestroyListener {
	
	private final TextView mValueView;
	private final TextView mTitleView;
	private final View mHelpView;
	private final View mDependentView;
	
	private View mDividerView;
	
	private ArrayList<Entry> mDependsOn;
	private DialogFactory mFactory;
	private FieldManager mManager;
	private T mValue;
	private int mDividerId;
	private int mHelpId;
	private int mIconId;
	private int mTitleId;
	private boolean mDependent;
	private boolean mReadOnly;
	private boolean mRequired;
	private boolean mUpdateDisplayOnChange = true;
	private boolean mAutomaticVisibility = true;
	
	private ArrayList<OnDependencyChangeListener> mDependents;
	
	private Dialog mDialog;
	private Dialog mHelpDialog;
	
	public FieldEntity(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layoutId, this, true);
		
		View view = findViewById(W.id.value);
		if (view != null && view instanceof TextView) {
			mValueView = (TextView) view;
		} else {
			throw new NullPointerException();
		}
		
		view = findViewById(W.id.title);
		if (view != null && view instanceof TextView) {
			mTitleView = (TextView) view;
		} else {
			throw new NullPointerException();
		}
		
		mHelpView = findViewById(W.id.help);
		mDependentView = findViewById(W.id.arrow);
		
		mValueView.setSaveEnabled(false);
		mTitleView.setSaveEnabled(false);
		mHelpView.setSaveEnabled(false);
		mDependentView.setSaveEnabled(false);
		
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.FieldEntity, 0, 0);
		setTitleId(a.getResourceId(W.styleable.FieldEntity_title, android.R.string.unknownName));
		setHelpId(a.getResourceId(W.styleable.FieldEntity_help, 0));
		setDividerId(a.getResourceId(W.styleable.FieldEntity_divider, -1));
		setDependent(a.getBoolean(W.styleable.FieldEntity_dependent, false));
		setReadOnly(a.getBoolean(W.styleable.FieldEntity_readOnly, false));
		setRequired(a.getBoolean(W.styleable.FieldEntity_required, false));
		a.recycle();
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setFocusable(true);
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (mDividerView != null) {
			mDividerView.setVisibility(visibility);
		}
	}
	
	public TextView getValueView() {
		return mValueView;
	}
	
	public TextView getTitleView() {
		return mTitleView;
	}
	
	public View getHelpView() {
		return mHelpView;
	}
	
	public View getDependentView() {
		return mDependentView;
	}
	
	public boolean isValid() {
		return mRequired ? mValue != null : true;
	}
	
	public String getErrorMessage() {
		if (mRequired)
			return getResources().getString(W.string.is_required);
		return null;
	}
	
	public void setRequired(boolean required) {
		mRequired = required;
	}
	
	public boolean isRequired() {
		return mRequired;
	}
	
	public void setReadOnly(boolean readOnly) {
		mReadOnly = readOnly;
	}
	
	public boolean isReadOnly() {
		return mReadOnly;
	}
	
	public void setTitleId(int titleId) {
		mTitleId = titleId;
		mTitleView.setText(titleId);
	}
	
	public int getTitleId() {
		return mTitleId;
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
	
	public void setHelpId(int helpId) {
		mHelpId = helpId;
		if (mHelpView != null) {
			mHelpView.setOnClickListener(helpId != 0 ? this : null);
			mHelpView.setVisibility(helpId != 0 ? View.VISIBLE : View.GONE);			
		}
	}
	
	public int getHelpId() {
		return mHelpId;
	}
	
	public void setIconId(int iconId) {
		mIconId = iconId;
		if (mIconId != 0) {
			View view = findViewById(W.id.icon);
			if (view != null && view instanceof ImageView) {
				((ImageView) view).setImageResource(iconId);
				view.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public int getIconId() {
		return mIconId;
	}
	
	public void setDividerId(int dividerId) {
		mDividerId = dividerId;
		if (mManager != null) {
			mDividerView = mManager.getActivity().findViewById(mDividerId);
		}
	}
	
	public int getDividerId() {
		return mDividerId;
	}
	
	public View getDividerView() {
		return mDividerView;
	}
	
	public void setAutomaticManageVisibility(boolean automatic) {
		mAutomaticVisibility = automatic;
	}
	
	public void setValue(T value) {
		mValue = value;
		onChangeValue();
		notifyDependencyChange();
	}
	
	public T getValue() {
		return mValue;
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
		mDividerView = mManager.getActivity().findViewById(mDividerId);
		if (mDividerView != null) {
			mDividerView.setVisibility(getVisibility());
		}
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
		return true;
	}
	
	protected boolean isDependentVisible() {
		if (mDependent && mDependsOn != null) {
			final int size = mDependsOn.size();
			for (int i = 0; i < size; i++) {
				final Entry entry = mDependsOn.get(i);
				if (!entry.matcher.matchesDependencyValue(entry.dependencyValue)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void onDependencyChanged() {
		if (!mAutomaticVisibility) {
			return;
		}
		final boolean visible = isDependentVisible();
		setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public void registerDependsOn(DependencyMatcher matcher, String dependencyValue) {
		if (mDependsOn == null) {
			mDependsOn = new ArrayList<Entry>();
		}
		
		mDependsOn.add(new Entry(matcher, dependencyValue));
	}
	
	protected void enableUpdateDisplayOnChange() {
		if (!mUpdateDisplayOnChange) {
			mUpdateDisplayOnChange = true;
		}
	}
	
	protected void disableUpdateDisplayOnChange() {
		if (mUpdateDisplayOnChange) {
			mUpdateDisplayOnChange = false;
		}
	}
	
	protected void onChangeValue() {
		if (mUpdateDisplayOnChange) {
			mValueView.setText(getDisplay());
		}
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
	
	protected void dispatchClick(View v) {
		
	}
	
	public void onClick(View v) {
		if (v.getId() == W.id.help) {
			showHelpDialog(null);
		} else {
			dispatchClick(v);
		}
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
	
	private void showHelpDialog(Bundle state) {
		Builder builder = new AlertDialog.Builder(getContext())
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(mTitleId)
		.setMessage(mHelpId)
		.setPositiveButton(android.R.string.ok, null);
		
		mManager.registerOnActivityDestroyListener(this);
		
		final Dialog dialog = mHelpDialog = builder.create();
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
		if (dialog.equals(mHelpDialog)) {
			mHelpDialog = null;
		}
	}
	
	public void onActivityDestroy() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		
		if (mHelpDialog != null && mHelpDialog.isShowing()) {
			mHelpDialog.dismiss();
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final boolean hasDialog = mDialog != null && mDialog.isShowing();
		final boolean hasHelpDialog = mHelpDialog != null && mHelpDialog.isShowing();
		if (!hasDialog && !hasHelpDialog) {
			return superState;
		}
		
		final SavedState myState = new SavedState(superState);
		myState.isDialogShowing = hasDialog;
		myState.dialogBundle = hasDialog ? mDialog.onSaveInstanceState() : null;
		myState.isHelpDialogShowing = hasHelpDialog;
		myState.helpDialogBundle = hasHelpDialog ? mHelpDialog.onSaveInstanceState() : null;
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
		if (myState.isHelpDialogShowing) {
			showHelpDialog(myState.helpDialogBundle);
		}
	}
	
	private static class SavedState extends BaseSavedState {
		
		private boolean isDialogShowing;
		private Bundle dialogBundle;
		
		private boolean isHelpDialogShowing;
		private Bundle helpDialogBundle;

		public SavedState(Parcel source) {
			super(source);
			isDialogShowing = source.readInt() == 1;
			dialogBundle = source.readBundle();
			isHelpDialogShowing = source.readInt() == 1;
			helpDialogBundle = source.readBundle();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isDialogShowing ? 1 : 0);
			dest.writeBundle(dialogBundle);
			dest.writeInt(isHelpDialogShowing ? 1 : 0);
			dest.writeBundle(helpDialogBundle);
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
	
	private static class Entry {
		final DependencyMatcher matcher;
		final String dependencyValue;
		
		public Entry(DependencyMatcher matcher, String dependencyValue) {
			this.matcher = matcher;
			this.dependencyValue = dependencyValue;
		}
	}

}
