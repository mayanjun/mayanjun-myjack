package org.mayanjun.myjack.core.query;

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
