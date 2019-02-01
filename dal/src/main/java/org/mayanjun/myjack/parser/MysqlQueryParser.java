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

package org.mayanjun.myjack.parser;

import org.mayanjun.myjack.core.enums.DataType;
import org.mayanjun.myjack.util.SqlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mysql query parser
 * @author mayanjun
 * @since 0.0.5
 */
public class MysqlQueryParser extends BaseParser {

	public MysqlQueryParser() {
	}

	@Override
	public Object renderValue(SQLParameter parameter, Object value, String charset, DataType dt) {
		if(value instanceof Boolean) return (((Boolean)value).booleanValue() ? "1" : "0");
		if(value instanceof Number) return value.toString();
		if(value instanceof Date) {
			return "'" + newDateFormat(dt).format(value) + "'";
		}
		String val = value.toString();
		return SqlUtils.escapeSQLParameter(val, charset, true);
	}

	private SimpleDateFormat newDateFormat(DataType dt) {
		if(dt == DataType.DATE) return new SimpleDateFormat("yyyy-MM-dd");
		if(dt == DataType.TIME) return new SimpleDateFormat("HH:mm:ss");
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}