package org.mayanjun.myjack.core.query;

/**
 * EquivalentComparator
 * @author mayanjun
 * @since 0.0.1
 */
public class EquivalentComparator extends SingleComparator {
	
	private static final long serialVersionUID = -8168515102185840251L;
	private boolean reverse;
	
	EquivalentComparator() {
		super(null, null, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name field name, in fact is bean property name
	 * @param value property value
	 */
	public EquivalentComparator(String name, Object value) {
		this(name, value, false, null);
	}
	
	/**
	 * Construct an EquivalentComparator
	 * @param name name
	 * @param value property value
	 * @param reverse represent non-equivalent if true
	 */
	public EquivalentComparator(String name, Object value, boolean reverse) {
		this(name, value, reverse, null);
	}

	public EquivalentComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}
	
	public EquivalentComparator(String name, Object value, boolean reverse, LogicalOperator lo) {
		super(name, value, lo);
		this.reverse = reverse;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null for field name: '" + name + "'");
	}

	@Override
	public String getExpression() {
		if(reverse) return "#{name}!=#{value}";
		return "#{name}=#{value}";
	}
}
