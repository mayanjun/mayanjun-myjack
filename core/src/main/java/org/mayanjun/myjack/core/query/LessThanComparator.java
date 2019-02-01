package org.mayanjun.myjack.core.query;

/**
 * 小于运算符
 * @author mayanjun
 * @since 0.0.1
 */
public class LessThanComparator extends SingleComparator {
	
	private static final long serialVersionUID = 3411107833263861597L;
	private boolean include;
	
	LessThanComparator() {
		super(null, null, null);
	}
	
	public LessThanComparator(String name, Object value, boolean include) {
		this(name, value, include, null);
	}

	public LessThanComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}
	
	public LessThanComparator(String name, Object value, boolean include, LogicalOperator lo) {
		super(name, value, lo);
		this.include = include;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null");
	}

	@Override
	public String getExpression() {
		if(include) return "#{name}<=#{value}";
		return "#{name}<#{value}";
	}
}
