package org.imogene.android.database.sqlite.stmt;

import java.util.Arrays;
import java.util.List;

import org.imogene.android.database.sqlite.stmt.QueryBuilder.InternalQueryBuilderWrapper;
import org.imogene.android.database.sqlite.stmt.query.Between;
import org.imogene.android.database.sqlite.stmt.query.Clause;
import org.imogene.android.database.sqlite.stmt.query.Exists;
import org.imogene.android.database.sqlite.stmt.query.In;
import org.imogene.android.database.sqlite.stmt.query.InSubQuery;
import org.imogene.android.database.sqlite.stmt.query.IsNotNull;
import org.imogene.android.database.sqlite.stmt.query.IsNull;
import org.imogene.android.database.sqlite.stmt.query.ManyClause;
import org.imogene.android.database.sqlite.stmt.query.NeedsFutureClause;
import org.imogene.android.database.sqlite.stmt.query.Not;
import org.imogene.android.database.sqlite.stmt.query.Raw;
import org.imogene.android.database.sqlite.stmt.query.SimpleComparison;

import android.provider.BaseColumns;

public class Where implements Clause {

	private static final int START_CLAUSE_SIZE = 4;

	private Clause[] clauseStack = new Clause[START_CLAUSE_SIZE];
	private int clauseStackLevel = 0;
	private NeedsFutureClause needsFuture = null;

	public Where and() {
		addNeedsFuture(new ManyClause(pop("AND"), ManyClause.AND_OPERATION));
		return this;
	}

	public Where and(Where first, Where second, Where... others) {
		Clause[] clauses = buildClauseArray(others, "AND");
		Clause secondClause = pop("AND");
		Clause firstClause = pop("AND");
		addClause(new ManyClause(firstClause, secondClause, clauses, ManyClause.AND_OPERATION));
		return this;
	}

	public Where and(int numClauses) {
		if (numClauses == 0) {
			throw new IllegalArgumentException("Must have at least one clause in and(numClauses)");
		}
		Clause[] clauses = new Clause[numClauses];
		for (int i = numClauses - 1; i >= 0; i--) {
			clauses[i] = pop("AND");
		}
		addClause(new ManyClause(clauses, ManyClause.AND_OPERATION));
		return this;
	}

	public Where between(String columnName, Object low, Object high) {
		addClause(new Between(columnName, low, high));
		return this;
	}

	public Where eq(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.EQUAL_TO_OPERATION));
		return this;
	}

	public Where ge(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION));
		return this;
	}

	public Where gt(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.GREATER_THAN_OPERATION));
		return this;
	}

	public Where in(String columnName, Iterable<?> objects) {
		addClause(new In(columnName, objects, true));
		return this;
	}

	public Where notIn(String columnName, Iterable<?> objects) {
		addClause(new In(columnName, objects, false));
		return this;
	}

	public Where in(String columnName, Object... objects) {
		return in(true, columnName, objects);
	}

	public Where notIn(String columnName, Object... objects) {
		return in(false, columnName, objects);
	}

	public Where in(String columnName, QueryBuilder subQueryBuilder) {
		return in(true, columnName, subQueryBuilder);
	}

	public Where notIn(String columnName, QueryBuilder subQueryBuilder) {
		return in(false, columnName, subQueryBuilder);
	}

	public Where exists(QueryBuilder subQueryBuilder) {
		// we do this to turn off the automatic addition of the ID column in the
		// select column list
		subQueryBuilder.enableInnerQuery();
		addClause(new Exists(new InternalQueryBuilderWrapper(subQueryBuilder)));
		return this;
	}

	public Where isNull(String columnName) {
		addClause(new IsNull(columnName));
		return this;
	}

	public Where isNotNull(String columnName) {
		addClause(new IsNotNull(columnName));
		return this;
	}

	public Where le(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION));
		return this;
	}

	public Where lt(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.LESS_THAN_OPERATION));
		return this;
	}

	public Where like(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.LIKE_OPERATION));
		return this;
	}

	public Where ne(String columnName, Object value) {
		addClause(new SimpleComparison(columnName, value, SimpleComparison.NOT_EQUAL_TO_OPERATION));
		return this;
	}

	public Where not() {
		addNeedsFuture(new Not());
		return this;
	}

	public Where not(Where comparison) {
		addClause(new Not(pop("NOT")));
		return this;
	}

	public Where or() {
		addNeedsFuture(new ManyClause(pop("OR"), ManyClause.OR_OPERATION));
		return this;
	}

	public Where or(Where left, Where right, Where... others) {
		Clause[] clauses = buildClauseArray(others, "OR");
		Clause secondClause = pop("OR");
		Clause firstClause = pop("OR");
		addClause(new ManyClause(firstClause, secondClause, clauses, ManyClause.OR_OPERATION));
		return this;
	}

	public Where or(int numClauses) {
		if (numClauses == 0) {
			throw new IllegalArgumentException("Must have at least one clause in or(numClauses)");
		}
		Clause[] clauses = new Clause[numClauses];
		for (int i = numClauses - 1; i >= 0; i--) {
			clauses[i] = pop("OR");
		}
		addClause(new ManyClause(clauses, ManyClause.OR_OPERATION));
		return this;
	}

	public Where idEq(Object id) {
		addClause(new SimpleComparison(BaseColumns._ID, id, SimpleComparison.EQUAL_TO_OPERATION));
		return this;
	}

	public Where raw(String rawStatement, Object... args) {
		addClause(new Raw(rawStatement, args));
		return this;
	}

	public Where rawComparison(String columnName, String rawOperator, Object value) {
		addClause(new SimpleComparison(columnName, value, rawOperator));
		return this;
	}
	
	public Where clause(Clause clause) {
		addClause(clause);
		return this;
	}

	private Where in(boolean in, String columnName, Object... objects) {
		if (objects.length == 1) {
			if (objects[0].getClass().isArray()) {
				throw new IllegalArgumentException("Object argument to " + (in ? "IN" : "notId")
						+ " seems to be an array within an array");
			}
			if (objects[0] instanceof Where) {
				throw new IllegalArgumentException("Object argument to " + (in ? "IN" : "notId")
						+ " seems to be a Where object, did you mean the QueryBuilder?");
			}
		}
		addClause(new In(columnName, objects, in));
		return this;
	}

	private Where in(boolean in, String columnName, QueryBuilder subQueryBuilder) {
		if (subQueryBuilder.getSelectColumnCount() != 1) {
			throw new IllegalArgumentException("Inner query must have only 1 select column specified instead of "
					+ subQueryBuilder.getSelectColumnCount() + ": "
					+ Arrays.toString(subQueryBuilder.getSelectColumns().toArray(new String[0])));
		}
		// we do this to turn off the automatic addition of the ID column in the
		// select column list
		subQueryBuilder.enableInnerQuery();
		addClause(new InSubQuery(columnName, new InternalQueryBuilderWrapper(subQueryBuilder), in));
		return this;
	}

	@Override
	public void appendSql(String tableName, StringBuilder sb, List<Object> columnArgList) {
		if (clauseStackLevel == 0) {
			throw new IllegalStateException("No where clauses defined.  Did you miss a where operation?");
		}
		if (clauseStackLevel != 1) {
			throw new IllegalStateException(
					"Both the \"left-hand\" and \"right-hand\" clauses have been defined.  Did you miss an AND or OR?");
		}

		// we don't pop here because we may want to run the query multiple times
		peek().appendSql(tableName, sb, columnArgList);
	}

	private Clause[] buildClauseArray(Where[] others, String label) {
		Clause[] clauses;
		if (others.length == 0) {
			clauses = null;
		} else {
			clauses = new Clause[others.length];
			// fill in reverse order
			for (int i = others.length - 1; i >= 0; i--) {
				clauses[i] = pop(label);
			}
		}
		return clauses;
	}

	private void addNeedsFuture(NeedsFutureClause clause) {
		if (needsFuture != null) {
			throw new IllegalStateException(needsFuture + " is already waiting for a future clause, can't add: " + clause);
		}
		needsFuture = clause;
		push(clause);
	}

	private void addClause(Clause clause) {
		if (needsFuture == null) {
			push(clause);
		} else {
			// we have a binary statement which was called before the right
			// clause was defined
			needsFuture.setMissingClause(clause);
			needsFuture = null;
		}
	}

	private void push(Clause clause) {
		// if the stack is full then we need to grow it
		if (clauseStackLevel == clauseStack.length) {
			// double its size each time
			Clause[] newStack = new Clause[clauseStackLevel * 2];
			// copy the entries over to the new stack
			for (int i = 0; i < clauseStackLevel; i++) {
				newStack[i] = clauseStack[i];
				// to help gc
				clauseStack[i] = null;
			}
			clauseStack = newStack;
		}
		clauseStack[clauseStackLevel++] = clause;
	}

	private Clause pop(String label) {
		if (clauseStackLevel == 0) {
			throw new IllegalStateException("Expecting there to be a clause already defined for '" + label + "' operation");
		}
		Clause clause = clauseStack[--clauseStackLevel];
		// to help gc
		clauseStack[clauseStackLevel] = null;
		return clause;
	}

	private Clause peek() {
		return clauseStack[clauseStackLevel - 1];
	}

}
