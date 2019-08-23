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

import java.io.Serializable;

/**
 * 查询比较器
 * @author mayanjun
 * @since 0.0.1
 */
public interface SqlComparator extends Serializable {
	
	LogicalOperator getLogicalOperator();
	
	/**
	 * 返回字段名称（属性名称）
	 * @return comparator name
	 * @author mayanjun
	 * @since 1.0.1
	 */
	String getName();
	
	void setName(String name);

	/**
	 * 返回比较运算表达式
	 * @return expression
	 * @author mayanjun
	 * @since 1.0.1
	 */
	String getExpression();
}
