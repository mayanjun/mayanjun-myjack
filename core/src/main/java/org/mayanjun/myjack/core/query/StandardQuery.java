package org.mayanjun.myjack.core.query;

import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.enums.QueryDeletedMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query
 * @author mayanjun
 * @since 0.0.1
 */
public class StandardQuery<T extends PersistableEntity> implements Query<T> {

	private static final long serialVersionUID = -656396755847643907L;
	private List<SqlComparator> comparators;
	private int[] limits;
	private Sort sort;
	private Class<T> beanType;
	private List<String> excludeFields;
	private List<String> includeFields;

	private boolean forUpdate;
	private QueryDeletedMode queryDeletedMode;
	
	public StandardQuery() {
		this.comparators = new ArrayList<SqlComparator>();
		this.limits = new int[2];
	}

	@Override
	public Query<T> addComparator(SqlComparator comparator) {
		if(comparator != null) this.comparators.add(comparator);
		return this;
	}

	@Override
	public Query<T> removeComparator(SqlComparator comparator) {
		if(comparator != null) this.comparators.remove(comparator);
		return this;
	}

	@Override
	public Query<T> addAllComparators(List<SqlComparator> comparators) {
		if(comparators != null && !comparators.isEmpty()) {
			this.comparators.addAll(comparators);
		}
		return this;
	}

	@Override
	public Query<T> setComparators(List<SqlComparator> comparators) {
		if(comparators != null) this.comparators = comparators;
		return this;
	}

	@Override
	public List<SqlComparator> getComparators() {
		return this.comparators;
	}

	@Override
	public Query<T> setLimit(int limit) {
		return setLimits(0, limit);
	}

	@Override
	public int getLimit() {
		return this.limits[1];
	}

	@Override
	public Query<T> setLimits(int offset, int limit) {
		this.limits[0] = offset;
		this.limits[1] = limit;
		return this;
	}

	@Override
	public int[] getLimits() {
		return limits;
	}

	@Override
	public Query<T> setSort(Sort sort) {
		this.sort = sort;
		return this;
	}

	@Override
	public Sort getSort() {
		return this.sort;
	}

	@Override
	public Query<T> setBeanType(Class<T> beanType) {
		this.beanType = beanType;
		return this;
	}

	@Override
	public Class<T> getBeanType() {
		return this.beanType;
	}

	@Override
	public Query<T> setExcludeFields(List<String> excludeFields) {
		this.excludeFields = excludeFields;
		return this;
	}

	@Override
	public List<String> getExcludeFields() {
		return this.excludeFields;
	}

	@Override
	public Query<T> setIncludeFields(List<String> includeFields) {
		this.includeFields = includeFields;
		return this;
	}

	@Override
	public List<String> getIncludeFields() {
		return this.includeFields;
	}

	@Override
	public boolean isForUpdate() {
		return this.forUpdate;
	}

	@Override
	public Query<T> setForUpdate(boolean forUpdate) {
		this.forUpdate = forUpdate;
		return this;
	}

	@Override
	public QueryDeletedMode getQueryDeletedMode() {
		return queryDeletedMode;
	}

	@Override
	public Query<T> setQueryDeletedMode(QueryDeletedMode queryDeletedMode) {
		this.queryDeletedMode = queryDeletedMode;
		return this;
	}

}
