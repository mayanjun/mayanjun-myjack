package org.mayanjun.myjack.core.query;

import java.io.Serializable;

/**
 * 查询比较器
 * @author mayanjun
 * @since 0.0.1
 */
public interface SqlComparator extends Serializable {
	
	LogicalOperator DEFAULT_LOGICAL_OPERATOR = LogicalOperator.AND;

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
	 * 返回比比较符
	 * @return expression
	 * @author mayanjun
	 * @since 1.0.1
	 */
	String getExpression();
}
