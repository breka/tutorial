package org.imogene.android.preference.filter;

import org.imogene.android.W;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.preference.PreferenceHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public abstract class FilterPreference<T extends ClientFilter> extends DialogPreference {

	private final ClientFilter.Creator<T> mCreator;

	private final String mUserId;
	private final String mTerminalId;
	private final String mEntityField;
	
	private T mFilter;

	protected final String mCardEntity;
	
	private boolean persisted;

	public FilterPreference(Context context, AttributeSet attrs, ClientFilter.Creator<T> creator) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.FilterPreference, 0, 0);
		mCardEntity = a.getString(W.styleable.FilterPreference_entity);		
		mEntityField = a.getString(W.styleable.FilterPreference_field);
		a.recycle();
		
		mUserId = PreferenceHelper.getSyncLogin(context);
		mTerminalId = PreferenceHelper.getHardwareId(context);

		mCreator = creator;
		
		
		mFilter = getPersistedFilter();
	}

	public final T getFilter() {
		return mFilter;
	}
	
	public final boolean persisted() {
		return persisted;
	}

	protected final boolean persistFilter() {
		callChangeListener(mFilter);
		mFilter.commit(getContext(), true, false).toString();
		persisted = true;
		notifyDependencyChange(shouldDisableDependents());
		persisted = false;
		notifyChanged();
		return true;
	}

	protected final T getPersistedFilter() {
		return mCreator.create(getContext(), mUserId, mTerminalId, mCardEntity, mEntityField);
	}
	
	public void notifyFilter() {
		mFilter = getPersistedFilter();
		notifyDependencyChange(shouldDisableDependents());
		notifyChanged();
	}

}
