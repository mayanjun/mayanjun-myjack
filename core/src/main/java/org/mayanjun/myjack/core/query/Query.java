package org.mayanjun.myjack.core.query;

import org.mayanjun.myjack.core.enums.QueryDeletedMode;

import java.io.Serializable;
import java.util.List;

/**
 * Query
 * @author mayanjun
 * @since 0.0.1
 */
public interface Query<T extends Serializable> extends Serializable {

	Query<T> addComparator(SqlComparator comparator);
	Query<T> removeComparator(SqlComparator comparator);
	Query<T> addAllComparators(List<SqlComparator> comparators);
	Query<T> setComparators(List<SqlComparator> comparators);
	List<SqlComparator> getComparators();

	Query<T> setLimit(int limit);
	int getLimit();

	Query<T> setLimits(int offset, int limit);

	/**
	 * 0 = offset, 1 = limit
	 * @return limits array, index 0 is offset and 1 is limit
	 */
	int[] getLimits();

	Query<T> setSort(Sort sort);
	Sort getSort();

	Query<T> setBeanType(Class<T> beanType);
	Class<T> getBeanType();

	Query<T> setExcludeFields(List<String> excludeFields);
	List<String> getExcludeFields();

	Query<T> setIncludeFields(List<String> includeFields);
	List<String> getIncludeFields();

	boolean isForUpdate();
	Query<T> setForUpdate(boolean forUpdate);

	/**
	 * Is query deleted data no matter if this query is a instance of {@link org.mayanjun.myjack.core.entity.DeletableEntity}
	 * @return
	 */
	QueryDeletedMode getQueryDeletedMode();
	Query<T> setQueryDeletedMode(QueryDeletedMode queryDeletedMode);

}