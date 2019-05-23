/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.myjack.api.query;

import org.mayanjun.myjack.api.enums.QueryDeletedMode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * A query object encapsulate all the information about query data from database.
 * Such as fields,where conditions,order by,limits,and pessimistic lock and so on.
 * @author mayanjun
 * @since 0.0.1
 */
public interface Query<T extends Serializable> extends Serializable {

	/**
	 * Add a comparator
	 * @param comparator comparator
	 * @return
	 */
	Query<T> addComparator(SqlComparator comparator);

	/**
	 * Remove a comparator
	 * @param comparator comparator
	 * @return
	 */
	Query<T> removeComparator(SqlComparator comparator);

	/**
	 * Add comparators
	 * @param comparators comparators
	 * @return
	 */
	Query<T> addComparators(List<SqlComparator> comparators);

	/**
	 * Returns comparators
	 * @return the comparators
	 */
	List<SqlComparator> getComparators();

	/**
	 * Set sql limit
	 * @param limit limit
	 * @return
	 */
	Query<T> setLimit(int limit);

	/**
	 * Returns the value of limit
	 * @return the value of limit
	 */
	int getLimit();

	/**
	 * Set the limit
	 * @param offset offset
	 * @param limit limit number
	 * @return
	 */
	Query<T> setLimits(int offset, int limit);

	/**
	 * 0 = offset, 1 = limit
	 * @return limits array, index 0 is offset and 1 is limit
	 */
	int[] getLimits();

	/**
	 * Set the bean type for this query
	 * @param beanType bean type
	 * @return
	 */
	Query<T> setBeanType(Class<T> beanType);

	/**
	 * Returns bean type
	 * @return
	 */
	Class<T> getBeanType();

	/**
	 * Set the excluded fields in SQL SELECT clause
	 * @param excludeFields
	 * @return
	 */
	Query<T> setExcludeFields(List<String> excludeFields);

	/**
	 * Returns the excluded fields
	 * @return the excluded fields
	 */
	List<String> getExcludeFields();

	/**
	 * Set the included fields in SQL SELECT clause
	 * @param includeFields
	 * @return
	 */
	Query<T> setIncludeFields(List<String> includeFields);

	/**
	 * Returns the included fields
	 * @return
	 */
	List<String> getIncludeFields();

	/**
	 * Set the optimistic lock
	 * @return
	 */
	boolean isForUpdate();

	/**
	 * Return if the optimistic lock is used
	 * @param forUpdate if the FOR UPDATE optimistic lock is used
	 * @return
	 */
	Query<T> setForUpdate(boolean forUpdate);

	/**
	 * Returns the query delete mode
	 * @return
	 */
	QueryDeletedMode getQueryDeletedMode();

	/**
	 * Set the query delete mode
	 * @param queryDeletedMode queryDeletedMode
	 * @return the query delete mode
	 */
	Query<T> setQueryDeletedMode(QueryDeletedMode queryDeletedMode);

	/**
	 * Add an sort field
	 * @param sort
	 * @return
	 */
	Query<T> addSort(Sort sort);

	/**
	 * Returns the sort fields
	 * @return
	 */
	Set<Sort> sorts();
}