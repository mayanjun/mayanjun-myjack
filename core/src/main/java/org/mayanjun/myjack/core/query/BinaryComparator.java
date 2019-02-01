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
