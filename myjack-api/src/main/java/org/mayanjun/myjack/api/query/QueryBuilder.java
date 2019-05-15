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

import org.mayanjun.myjack.api.entity.PersistableEntity;
import org.mayanjun.myjack.api.enums.QueryDeletedMode;

import java.util.Arrays;
import java.util.Set;

/**
 * QueryBuilder
 * @author mayanjun
 * @since 0.0.1
 */
public class QueryBuilder<T extends PersistableEntity> {

	protected Query query;

	private boolean startGroup;
	
	private QueryBuilder(Class<T> beanType) {
		if(beanType == null) throw new IllegalArgumentException("bean type can not be null");
		this.query = new StandardQuery<T>();
		this.query.setBeanType(beanType);
	}
	
	public static <T extends PersistableEntity> QueryBuilder<T> custom(Class<T> beanType) {
		return new QueryBuilder(beanType);
	}

	public QueryBuilder<T> andLike(String name, Object value) {
		this.query.addComparator(new LikeComparator(name, value, false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> andLike(String name, Object value, boolean reverse) {
		this.query.addComparator(new LikeComparator(name, value, reverse, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> andIn(String name, Object value[]) {
		this.query.addComparator(new InComparator(name, value, false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> andNotIn(String name, Object value[]) {
		this.query.addComparator(new InComparator(name, value, true, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> andIn(String name, Object value[], boolean reverse) {
		this.query.addComparator(new InComparator(name, value, reverse, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andEquivalent(String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> andNotEquivalent(String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, true, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andEquivalent(String name, Object value, boolean reverse) {
		this.query.addComparator(new EquivalentComparator(name, value, reverse, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andGreaterThan(String name, Object value) {
		this.query.addComparator(new GreaterThanComparator(name, value, false, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andGreaterThan(String name, Object value, boolean include) {
		this.query.addComparator(new GreaterThanComparator(name, value, include, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andLessThan(String name, Object value) {
		this.query.addComparator(new LessThanComparator(name, value, false, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andLessThan(String name, Object value, boolean include) {
		this.query.addComparator(new LessThanComparator(name, value, include, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder<T> andBetween(String name, Object value1, Object value2) {
		this.query.addComparator(new BetweenComparator(name, value1, value2, LogicalOperator.AND));
		return this;
	}


	/////// OR

	public QueryBuilder<T> orLike(String name, Object value) {
		this.query.addComparator(new LikeComparator(name, value, false, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orNotLike(String name, Object value) {
		this.query.addComparator(new LikeComparator(name, value, true, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orLike(String name, Object value, boolean reverse) {
		this.query.addComparator(new LikeComparator(name, value, reverse, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orIn(String name, Object value[]) {
		this.query.addComparator(new InComparator(name, value, false, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orNotIn(String name, Object value[]) {
		this.query.addComparator(new InComparator(name, value, true, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orIn(String name, Object value[], boolean reverse) {
		this.query.addComparator(new InComparator(name, value, reverse, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orEquivalent(String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, false, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> orNotEquivalent(String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, true, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orEquivalent(String name, Object value, boolean reverse) {
		this.query.addComparator(new EquivalentComparator(name, value, reverse, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orGreaterThan(String name, Object value) {
		this.query.addComparator(new GreaterThanComparator(name, value, false, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orGreaterThan(String name, Object value, boolean include) {
		this.query.addComparator(new GreaterThanComparator(name, value, include, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orLessThan(String name, Object value) {
		this.query.addComparator(new LessThanComparator(name, value, false, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orLessThan(String name, Object value, boolean include) {
		this.query.addComparator(new LessThanComparator(name, value, include, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orBetween(String name, Object value1, Object value2) {
		this.query.addComparator(new BetweenComparator(name, value1, value2, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder<T> orderBy(String orderField, SortDirection direction) {
		this.query.addSort(new Sort(orderField, direction));
		return this;
	}

	public QueryBuilder<T> orderBy(Sort ... sort) {
		if (sort != null && sort.length > 0) {
			for (Sort s : sort) this.query.addSort(s);
		}
		return this;
	}
	
	public QueryBuilder<T> limit(int limit) {
		this.query.setLimit(limit);
		return this;
	}

	public QueryBuilder<T> limit(int offset, int limit) {
		this.query.setLimits(offset, limit);
		return this;
	}

	public QueryBuilder<T> orGroup() {
		this.query.addComparator(new GroupComparator(true, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder<T> andGroup() {
		this.query.addComparator(new GroupComparator(true, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> endGroup() {
		this.query.addComparator(new GroupComparator(false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder<T> includeFields(String... fields) {
		this.query.setIncludeFields(Arrays.asList(fields));
		return this;
	}

	public QueryBuilder<T> excludeFields(String... fields) {
		this.query.setExcludeFields(Arrays.asList(fields));
		return this;
	}

	public QueryBuilder<T> forUpdate() {
		this.query.setForUpdate(true);
		return this;
	}

	public QueryBuilder<T> forUpdate(boolean forUpdate) {
		this.query.setForUpdate(forUpdate);
		return this;
	}

	public QueryBuilder<T> queryDeletedMode(QueryDeletedMode mode) {
		this.query.setQueryDeletedMode(mode);
		return this;
	}

    public QueryBuilder<T> queryWithDeleted() {
        this.query.setQueryDeletedMode(QueryDeletedMode.WITH_DELETED);
        return this;
    }

    public QueryBuilder<T> queryWithoutDeleted() {
        this.query.setQueryDeletedMode(QueryDeletedMode.WITHOUT_DELETED);
        return this;
    }

    public QueryBuilder<T> queryOnlyDeleted() {
        this.query.setQueryDeletedMode(QueryDeletedMode.ONLY_DELETED);
        return this;
    }

	public Query<T> build() {
		Set<Sort> sorts = query.sorts();
		if(sorts == null || sorts.isEmpty()) query.addSort(new Sort("id", SortDirection.DESC));
		return this.query;
	}
}
