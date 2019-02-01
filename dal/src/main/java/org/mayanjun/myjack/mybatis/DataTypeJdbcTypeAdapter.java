package org.mayanjun.myjack.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.mayanjun.myjack.core.enums.DataType;

/**
 * DataTypeJdbcTypeAdapter
 *
 * @author mayanjun(8/13/16)
 */
public class DataTypeJdbcTypeAdapter {

	public static JdbcType jdbcType(DataType dataType) {
		JdbcType type = null;
		// adapt for mybatis
		switch (dataType) {
			case DATETIME:
				type = JdbcType.TIMESTAMP;
				break;
			case TEXT:
			case LONGTEXT:
			case MEDIUMTEXT:
			case TINYTEXT:
				type = JdbcType.LONGVARCHAR;
				break;
			case BIGINT:
				type = JdbcType.BIGINT;
				break;
			case BINARY:
				type = JdbcType.BINARY;
				break;
			case BIT:
				type = JdbcType.BIT;
				break;
			case BLOB:
			case TINYBLOB:
			case LONGBLOB:
			case MEDIUMBLOB:
				type = JdbcType.BLOB;
				break;
			case CHAR:
				type = JdbcType.CHAR;
				break;
			case DATE:
				type = JdbcType.DATE;
				break;
			case DOUBLE:
				type = JdbcType.DOUBLE;
				break;
			case ENUM:
			case SET:
			case YEAR:
				type = null;
				break;
			case FLOAT:
				type = JdbcType.FLOAT;
				break;
			case INT:
			case TINYINT:
			case MEDIUMINT:
			case SMALLINT:
				type = JdbcType.INTEGER;
				break;
			case VARBINARY:
				type = JdbcType.VARBINARY;
				break;
			case TIME:
				type = JdbcType.TIME;
				break;
			case TIMESTAMP:
				type = JdbcType.TIMESTAMP;
				break;
			case VARCHAR:
				type = JdbcType.VARCHAR;
				break;
		}

		return type;
	}

}
