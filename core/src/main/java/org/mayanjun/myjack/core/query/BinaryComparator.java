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
 * 双目运算符
 * @author mayanjun
 * @since 0.0.1
 */
public abstract class BinaryComparator extends LogicalComparator {
	
	private static final long serialVersionUID = 1021481104444393516L;
	private Object value1;
	private Object value2;
	
	BinaryComparator(String name, Object value1, Object value2, LogicalOperator lo) {
		super(name, lo);
		this.value1 = value1;
		this.value2 = value2;
	}

	public Object getValue1() {
		return this.value1;
	}
	
	public Object getValue2() {
		return this.value2;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}
}
