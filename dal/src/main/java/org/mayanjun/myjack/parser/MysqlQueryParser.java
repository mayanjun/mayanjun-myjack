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