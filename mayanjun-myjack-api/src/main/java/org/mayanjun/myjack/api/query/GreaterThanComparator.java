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

/**
 * Grate than or equals comparator
 * @author mayanjun
 * @since 0.0.1
 */
public class GreaterThanComparator extends SingleComparator {
	
	private static final long serialVersionUID = 5502031398198625944L;
	private boolean include;
	
	GreaterThanComparator() {
		super(null, null, null);
	}
	
	public GreaterThanComparator(String name, Object value, boolean include) {
		this(name, value, include, null);
	}

	public GreaterThanComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}
	
	public GreaterThanComparator(String name, Object value, boolean include, LogicalOperator lo) {
		super(name, value, lo);
		this.include = include;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null");
	}

	@Override
	public String getExpression() {
		if(include) return "#{name}>=#{value}";
		return "#{name}>#{value}";
	}
}
