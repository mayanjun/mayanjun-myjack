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
 * 逻辑操作符
 * @author mayanjun
 * @since 0.0.1
 */
public abstract class LogicalComparator implements SqlComparator {
	
	private static final long serialVersionUID = 8618374265562880660L;
	private LogicalOperator lo;
	private String name;

	LogicalComparator(String name, LogicalOperator lo) {
		this.name = name;
		this.lo = lo;
	}

	@Override
	public LogicalOperator getLogicalOperator() {
		return this.lo;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
		
	}
}
