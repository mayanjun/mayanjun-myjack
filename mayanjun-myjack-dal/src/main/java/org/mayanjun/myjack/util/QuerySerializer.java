/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.myjack.util;

import org.apache.commons.codec.binary.Base64;
import org.mayanjun.myjack.api.query.Query;
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

	public static Query<?> deserialize(String encodedQuery) {
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
