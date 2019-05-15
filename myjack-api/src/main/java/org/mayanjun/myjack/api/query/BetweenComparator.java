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
 * Between 运算符
 * @author mayanjun
 * @since 0.0.1
 */
public class BetweenComparator extends BinaryComparator {

	private static final long serialVersionUID = -9061076704511404714L;
	
	BetweenComparator() {
		super(null, null, null, null);
	}

	public BetweenComparator(String name, Object value1, Object value2) {
		this(name, value1, value2, null);
	}
	
	public BetweenComparator(String name, Object value1, Object value2, LogicalOperator lo) {
		super(name, value1, value2, lo);
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value1 == null) throw new IllegalArgumentException("value1 can not be null");
		if(value2 == null) throw new IllegalArgumentException("value2 can not be null");
	}

	@Override
	public String getExpression() {
		return "#{name} BETWEEN #{value1} AND #{value2}";
	}


}
