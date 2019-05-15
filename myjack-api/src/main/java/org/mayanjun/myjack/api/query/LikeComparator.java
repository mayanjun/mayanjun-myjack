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
 * EquivalentComparator
 * @author mayanjun
 * @since 0.0.1
 */
public class LikeComparator extends SingleComparator {

	private static final long serialVersionUID = -8168515102185840251L;
	private boolean reverse;

	LikeComparator() {
		super(null, null, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name field name, in fact is bean property name
	 * @param value property value
	 */
	public LikeComparator(String name, Object value) {
		this(name, value, false, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name name
	 * @param value property value
	 * @param reverse represent non-equivalent if true
	 */
	public LikeComparator(String name, Object value, boolean reverse) {
		this(name, value, reverse, null);
	}

	public LikeComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}

	public LikeComparator(String name, Object value, boolean reverse, LogicalOperator lo) {
		super(name, value, lo);
		this.reverse = reverse;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null");
	}

	@Override
	public String getExpression() {
		if(reverse) return "#{name} NOT LIKE #{value}";
		return "#{name} LIKE #{value}";
	}
}
