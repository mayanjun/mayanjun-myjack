package org.mayanjun.myjack.core.query;

import java.io.Serializable;

/**
 * Sort
 * @author mayanjun
 * @since 0.0.1
 */
public class Sort implements Serializable {
	
	private static final long serialVersionUID = -180616073336552137L;

	public Sort() {
	}

	public Sort(String name, SortDirection direction) {
		this.name = name;
		this.direction = direction;
	}

	private String name;
	
	private SortDirection direction;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortDirection getDirection() {
		return direction;
	}

	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}
	
}
