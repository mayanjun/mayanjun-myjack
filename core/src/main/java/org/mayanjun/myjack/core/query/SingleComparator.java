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

package org.mayanjun.myjack.core.query;

/**
 * 单目运算符
 * @author mayanjun
 * @since 0.0.1
 */
public abstract class SingleComparator extends LogicalComparator {
	
	private static final long serialVersionUID = -4773452192176253990L;
	private Object value;
	
	SingleComparator(String name, Object value, LogicalOperator lo) {
		super(name, lo);
		//if(value == null) throw new IllegalArgumentException("value can not be null");
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
