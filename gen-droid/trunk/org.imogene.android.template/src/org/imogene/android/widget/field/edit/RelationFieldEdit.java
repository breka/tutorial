package org.imogene.android.widget.field.edit;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.W;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.widget.field.ConstraintBuilder;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

public abstract class RelationFieldEdit<T> extends BaseFieldEdit<T> implements OnActivityResultListener {
	
	public static interface ExtraBuilder {
		public void onCreateExtra(Bundle bundle);
	}
	
	private ArrayList<ConstraintEntry> mConstraintsBuilders;
	private ArrayList<CommonFieldEntry> mCommonFields;
	private ArrayList<ExtraBuilder> mBuilders;

	protected final boolean mHasReverse;
	protected final int mDisplayPlId;
	protected final int mDisplaySgId;
	protected final int mOppositeCardinality;
	protected final int mType; // 0 for main relation field; 1 for reverse relation field
	protected String mOppositeRelationField;
	protected String mFieldName;
	protected String mTableName;

	protected int mRequestCode;
	protected Uri mContentUri;

	public RelationFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_relation);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.RelationField, 0, 0);
		mHasReverse = a.getBoolean(W.styleable.RelationField_hasReverse, false);
		mDisplayPlId = a.getResourceId(W.styleable.RelationField_displayPl, 0);
		mDisplaySgId = a.getResourceId(W.styleable.RelationField_displaySg, 0);
		mOppositeCardinality = a.getInt(W.styleable.RelationField_oppositeCardinality, 0);
		mType = a.getInt(W.styleable.RelationField_type, 0);
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
	
	@Override
	public void onAttachedToHierarchy(FieldManager manager) {
		super.onAttachedToHierarchy(manager);
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextId();
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
			mCommonFields = new ArrayList<CommonFieldEntry>();
		}
		
		mCommonFields.add(new CommonFieldEntry(commonField, commonName));
	}
	
	public void registerExtraBuilder(ExtraBuilder builder) {
		if (mBuilders == null) {
			mBuilders = new ArrayList<ExtraBuilder>();
		}
		
		mBuilders.add(builder);
	}
	
	public void registerConstraintBuilder(ConstraintBuilder builder, String column) {
		if (mConstraintsBuilders == null) {
			mConstraintsBuilders = new ArrayList<ConstraintEntry>();
		}
		
		mConstraintsBuilders.add(new ConstraintEntry(builder, column));
		
		builder.registerConstraintDependent(this);
	}
	
	@Override
	protected void dispatchClick(View v) {
		final Intent intent = new Intent(Intent.ACTION_PICK, mContentUri);
		final SQLiteBuilder builder = new SQLiteBuilder();
		boolean sqlTouched = onPrepareSQLBuilder(builder);
		onPrepareIntent(intent);
		if (mConstraintsBuilders != null) {
			for (ConstraintEntry entry : mConstraintsBuilders) {
				if (entry.first.onCreateConstraint(entry.second, builder)) {
					sqlTouched |= true;
				} else {
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
			for (CommonFieldEntry entry : mCommonFields) {
				if (entry.first instanceof RelationOneFieldEdit) {
					bundle.putParcelable(entry.second, ((RelationOneFieldEdit) entry.first).getValue());
				} else if (entry.first instanceof RelationManyFieldEdit) {
					bundle.putParcelableArrayList(entry.second, ((RelationManyFieldEdit) entry.first).getValue());
				}
			}
		}
		if (mBuilders != null) {
			for (ExtraBuilder builder : mBuilders) {
				builder.onCreateExtra(bundle);
			}
		}
		return bundle;
	}
	
	protected void onPrepareIntent(Intent intent) {
		
	}
	
	protected boolean onPrepareSQLBuilder(SQLiteBuilder builder) {
		return false;
	}
	
	private static final class ConstraintEntry extends Pair<ConstraintBuilder, String> {

		public ConstraintEntry(ConstraintBuilder first, String second) {
			super(first, second);
		}
		
	};
	
	private static class CommonFieldEntry extends Pair<RelationFieldEdit<?>, String> {
		
		public CommonFieldEntry(RelationFieldEdit<?> first, String second) {
			super(first, second);
		}

	}
}
