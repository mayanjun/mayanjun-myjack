package org.mayanjun.myjack.core.query;

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
