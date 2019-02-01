package org.mayanjun.myjack.util;

import org.apache.commons.codec.binary.Base64;
import org.mayanjun.myjack.core.query.Query;
import org.mayanjun.myjack.parser.MysqlQueryParser;

/**
 * QuerySerializer
 *
 * @author mayanjun(30/11/2016)
 */
public class QuerySerializer {

	public static final MysqlQueryParser MYSQL_QUERY_PARSER = new MysqlQueryParser();

	public static String serialize(Query<?> query) {
		if(query == null) return null;
		byte[] bytes = ObjectSerializer.serialize(query);
		return Base64.encodeBase64String(bytes);
	}

	public static Query<?> unserialize(String encodedQuery) {
		try {
			byte bs[] = Base64.decodeBase64(encodedQuery);
			return (Query<?>)ObjectSerializer.unserialize(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toSQL(Query<?> query) {
		return MYSQL_QUERY_PARSER.parse(query).getSql();
	}

}
