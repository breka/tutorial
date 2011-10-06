package org.imogene.android.common.filter;

import org.imogene.android.common.ClientFilter;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.util.FormatHelper;

public class DateFilter extends ClientFilter {
	
	public static final Creator<DateFilter> FILTER_CREATOR = new DefaultCreator<DateFilter>() {
		@Override
		protected DateFilter newFilter() {
			return new DateFilter();
		}
		
		@Override
		protected DateFilter newFilter(ClientFilterCursor c) {
			return new DateFilter(c);
		}
	};
	
	public enum DateOperator {
		UNDEF(OPERATOR_UNDEF),
		EQUAL(DATE_OPERATOR_EQUAL),
		AFTER(DATE_OPERATOR_AFTER),
		BEFORE(DATE_OPERATOR_BEFORE),
		BETWEEN(DATE_OPERATOR_BETWEEN);
		
		private final String operator;
		
		private DateOperator(String op) {
			operator = op;
		}
		
		public String operator() {
			return operator;
		}
		
		public static DateOperator fromOp(String str) {
			for (DateOperator o : values())
				if (o.operator().equals(str))
					return o;
			return UNDEF;
		}
	}
	
	private DateOperator mOperator;
	private Long mEqual;
	private Long mBefore;
	private Long mAfter;
	
	private DateFilter(ClientFilterCursor c) {
		super(c);
		init();
	}
	
	private DateFilter() {
		super();
		init();
	}
	
	private void init() {
		mOperator = DateOperator.fromOp(getOperator());
		String str = getFieldValue();
		if (str == null)
			return;
		switch (mOperator) {
		case BETWEEN:
			String[] values = str.split(";");
			if (values.length == 2) {
				mAfter = FormatHelper.toLong(values[0]);
				mBefore = FormatHelper.toLong(values[1]);
			}
			return;
		case BEFORE:
			mBefore = FormatHelper.toLong(str);
			return;
		case AFTER:
			mAfter = FormatHelper.toLong(str);
			return;
		case EQUAL:
			mEqual = FormatHelper.toLong(str);
			return;
		}
	}
	
	@Override
	protected void preCommit() {
		super.preCommit();
		switch (mOperator) {
		case BETWEEN:
			StringBuilder builder = new StringBuilder();
			builder.append(mAfter != null ? mAfter.toString() : null).append(';');
			builder.append(mBefore != null ? mBefore.toString() : null);
			setOperator(mOperator.operator());
			setFieldValue(builder.toString());
			return;
		case BEFORE:
			setOperator(mOperator.operator());
			setFieldValue(mBefore != null ? mBefore.toString() : null);
			return;
		case AFTER:
			setOperator(mOperator.operator());
			setFieldValue(mAfter != null ? mAfter.toString() : null);
			return;
		case EQUAL:
			setOperator(mOperator.operator());
			setFieldValue(mEqual != null ? mEqual.toString() : null);
			return;
		}
		setOperator(DateOperator.UNDEF.operator());
		setFieldValue(null);
	}

	public DateOperator getDateOperator() {
		return mOperator;
	}

	public Long getEqual() {
		return mEqual;
	}

	public Long getBefore() {
		return mBefore;
	}
	
	public Long getAfter() {
		return mAfter;
	}
	
	public void setDateOperator(DateOperator op) {
		mOperator = op;
	}
	
	public void setEqual(Long equal) {
		mEqual = equal;
	}
	
	public void setBefore(Long before) {
		mBefore = before;
	}
	
	public void setAfter(Long after) {
		mAfter = after;
	}
}
