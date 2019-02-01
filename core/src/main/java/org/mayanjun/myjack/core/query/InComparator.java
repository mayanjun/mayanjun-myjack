package org.mayanjun.myjack.core.query;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * EquivalentComparator
 * @author mayanjun
 * @since 0.0.1
 */
public class InComparator extends SingleComparator {

	private static final long serialVersionUID = -8168515102185840251L;
	private boolean reverse;

	InComparator() {
		super(null, null, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name field name, in fact is bean property name
	 * @param value property value
	 */
	public InComparator(String name, Object value) {
		this(name, value, false, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name name
	 * @param value property value
	 * @param reverse represent non-equivalent if true
	 */
	public InComparator(String name, Object value, boolean reverse) {
		this(name, value, reverse, null);
	}

	public InComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}

	public InComparator(String name, Object value, boolean reverse, LogicalOperator lo) {
		super(name, value, lo);
		this.reverse = reverse;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null");
		if(!value.getClass().isArray()
				&& !(value instanceof Collection)) throw new IllegalArgumentException("value must be array or Collection");

		int len = 0;
		if(value.getClass().isArray()) {
			len = Array.getLength(value);
		}else {
			len = ((Collection<?>) value).size();
		}
		if(len == 0) throw new IllegalArgumentException("collection can not be empty");
	}

	@Override
	public String getExpression() {
		if(reverse) return "#{name} NOT IN (#{value})";
		return "#{name} IN (#{value})";
	}
}
