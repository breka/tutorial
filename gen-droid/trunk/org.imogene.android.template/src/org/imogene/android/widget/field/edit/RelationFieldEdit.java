package org.imogene.android.widget.field.edit;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.W;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public abstract class RelationFieldEdit<T> extends BaseFieldEdit<T> implements OnActivityResultListener {
	
	public static interface ExtraBuilder {
		public void build(Bundle bundle);
	}

	private ArrayList<Entry> mCommonFields;
	private ArrayList<RelationFieldEdit<?>> mHierarchicalDependents;
	private ArrayList<ExtraBuilder> mBuilders;

	protected final boolean mHasReverse;
	protected final int mDisplayPlId;
	protected final int mDisplaySgId;
	protected final int mOppositeCardinality;
	protected final int mType; // 0 for main relation field; 1 for reverse relation field
	protected String mOppositeRelationField;
	protected String mFieldName;
	protected String mTableName;

	private final int mHierarchicalParentId;
	
	protected int mRequestCode;
	protected Uri mContentUri;

	private RelationFieldEdit<?> mHierarchicalParent = null;
	private String mHierarchicalField;

	public RelationFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_relation);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.RelationField, 0, 0);
		mHasReverse = a.getBoolean(W.styleable.RelationField_hasReverse, false);
		mDisplayPlId = a.getResourceId(W.styleable.RelationField_displayPl, 0);
		mDisplaySgId = a.getResourceId(W.styleable.RelationField_displaySg, 0);
		mOppositeCardinality = a.getInt(W.styleable.RelationField_oppositeCardinality, 0);
		mType = a.getInt(W.styleable.RelationField_type, 0);
		mHierarchicalParentId = a.getResourceId(W.styleable.RelationField_hierarchicalParent, -1);
		a.recycle();
		setOnClickListener(this);
	}
	
	public void setOppositeRelationField(String oppositerelationField) {
		mOppositeRelationField = oppositerelationField;
	}
	
	public void setFieldName(String fieldName) {
		mFieldName = fieldName;
	}
	
	public void setTableName(String tableName) {
		mTableName = tableName;
	}
	
	public void setHierarchicalField(String hierarchicalField) {
		mHierarchicalField = hierarchicalField;
	}
	
	@Override
	public void onAttachedToHierarchy(FieldManager manager) {
		super.onAttachedToHierarchy(manager);
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextId();
		final View view = manager.getActivity().findViewById(mHierarchicalParentId);
		if (view != null && view instanceof RelationFieldEdit<?>) {
			mHierarchicalParent = (RelationFieldEdit<?>) view;
			if (mHierarchicalParent != null && mHierarchicalField != null) {
				mHierarchicalParent.registerHierarchicalDependent(this);
			}
		} else {
			mHierarchicalParent = null;
		}
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}
	
	public void setContentUri(Uri contentUri) {
		mContentUri = contentUri;
	}
	
	public void setDrawable(Drawable drawable) {
		final View color = findViewById(W.id.color);
		if (color != null) {
			color.setBackgroundDrawable(drawable);
		}
	}
	
	public void registerCommonField(RelationFieldEdit<?> commonField, String commonName) {
		if (mCommonFields == null) {
			mCommonFields = new ArrayList<Entry>();
		}
		
		mCommonFields.add(new Entry(commonField, commonName));
	}
	
	public void registerHierarchicalDependent(RelationFieldEdit<?> dependent) {
		if (mHierarchicalDependents == null) {
			mHierarchicalDependents = new ArrayList<RelationFieldEdit<?>>();
		}
		
		mHierarchicalDependents.add(dependent);
	}
	
	public void registerExtraBuilder(ExtraBuilder builder) {
		if (mBuilders == null) {
			mBuilders = new ArrayList<ExtraBuilder>();
		}
		
		mBuilders.add(builder);
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		if (notifyValueChangedEnabled()) {
			notifyHierarchicalDependencyChange();
		}
	}
	
	private void notifyHierarchicalDependencyChange() {
		if (mHierarchicalDependents == null) {
			return;
		}
		
		final int size = mHierarchicalDependents.size();
		for (int i = 0 ; i < size; i++) {
			mHierarchicalDependents.get(i).setValue(null);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		final Intent intent = new Intent(Intent.ACTION_PICK, mContentUri);
		final SQLiteBuilder builder = new SQLiteBuilder();
		boolean sqlTouched = onPrepareSQLBuilder(builder);
		onPrepareIntent(intent);
		if (mHierarchicalParent != null && mHierarchicalField != null) {
			if (mHierarchicalParent instanceof RelationOneFieldEdit) {
				final Uri uri = ((RelationOneFieldEdit) mHierarchicalParent).getValue();
				if (uri != null) {
					final String id = uri.getLastPathSegment();
					builder.appendEq(mHierarchicalField, id);
					sqlTouched = true;
				} else {
					final String fieldName = getResources().getString(mHierarchicalParent.getTitleId());
					Toast.makeText(getContext(), getResources().getString(W.string.relation_hierarchical_parent_unset, fieldName), Toast.LENGTH_LONG).show();
					return;
				}
			} else if (mHierarchicalParent instanceof RelationManyFieldEdit) {
				final ArrayList<Uri> uris = ((RelationManyFieldEdit) mHierarchicalParent).getValue();
				if (uris != null && uris.size() > 0) {
					String[] ids = new String[uris.size()];
					for (int i = 0; i < uris.size(); i++) {
						ids[i] = uris.get(i).getLastPathSegment();
					}
					builder.appendIn(mHierarchicalField, ids);
					sqlTouched = true;
				} else {
					final String fieldName = getResources().getString(mHierarchicalParent.getTitleId());
					Toast.makeText(getContext(), getResources().getString(W.string.relation_hierarchical_parent_unset, fieldName), Toast.LENGTH_LONG).show();
					return;
				}
			}
		}
		if (sqlTouched) {
			intent.putExtra(Extras.EXTRA_WHERE, builder);
		}
		intent.putExtra(Extras.EXTRA_ENTITY, createBundle());
		getFieldManager().getActivity().startActivityForResult(intent, mRequestCode);
	}
	
	protected Bundle createBundle() {
		Bundle bundle = new Bundle();
		if (mHasReverse && mOppositeCardinality == 1) {
			bundle.putParcelable(mOppositeRelationField, getFieldManager().getUri());
		}
		if (mCommonFields != null && !mCommonFields.isEmpty()) {
			final int size = mCommonFields.size();
			for (int i = 0; i < size ; i++) {
				final Entry entry = mCommonFields.get(i);
				if (entry.commonField instanceof RelationOneFieldEdit) {
					bundle.putParcelable(entry.oppositeRelation, ((RelationOneFieldEdit) entry.commonField).getValue());
				} else if (entry.commonField instanceof RelationManyFieldEdit) {
					bundle.putParcelableArrayList(entry.oppositeRelation, ((RelationManyFieldEdit) entry.commonField).getValue());
				}
			}
		}
		if (mBuilders != null) {
			for (ExtraBuilder builder : mBuilders) {
				builder.build(bundle);
			}
		}
		return bundle;
	}
	
	protected void onPrepareIntent(Intent intent) {
		
	}
	
	protected boolean onPrepareSQLBuilder(SQLiteBuilder builder) {
		return false;
	}
	
	private static class Entry {
		final RelationFieldEdit<?> commonField;
		final String oppositeRelation;
		
		public Entry(RelationFieldEdit<?> field, String relationName) {
			commonField = field;
			oppositeRelation = relationName;
		}
	}
}
