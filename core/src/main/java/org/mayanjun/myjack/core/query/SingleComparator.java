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
