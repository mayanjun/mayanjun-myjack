package org.mayanjun.myjack.parser;

import org.mayanjun.myjack.core.query.Query;

import java.io.Serializable;

/**
 * Query Parser
 * @author mayanjun
 * @since 0.0.5
 */
public interface QueryParser {

	/**
	 * 解析Query
	 * @param query
	 * @return
	 * @author mayanjun
	 * @since 1.0.1
	 */
	<T extends Serializable> SQLParameter<T> parse(Query<T> query);
}
