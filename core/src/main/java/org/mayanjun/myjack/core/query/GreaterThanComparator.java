package org.mayanjun.myjack.core.query;

/**
 * 大于运算符
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
