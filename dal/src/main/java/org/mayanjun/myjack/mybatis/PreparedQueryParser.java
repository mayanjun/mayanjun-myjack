package org.mayanjun.myjack.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.mayanjun.myjack.core.enums.DataType;
import org.mayanjun.myjack.parser.BaseParser;
import org.mayanjun.myjack.parser.SQLParameter;

/**
 * MyBatisQueryParser
 *
 * @author mayanjun(6/20/16)
 * @since 0.0.5
 */
public class PreparedQueryParser extends BaseParser {

    private String paramName;

    public PreparedQueryParser(String paramName) {
        if(StringUtils.isNotBlank(paramName)) {
            this.paramName = paramName + ".";
        } else {
            this.paramName = "";
        }
    }

    @Override
    public Object renderValue(SQLParameter parameter, Object value, String charset, DataType dt) {
        String newFieldName = parameter.newParameterName();
        JdbcType type = DataTypeJdbcTypeAdapter.jdbcType(dt);
        String jdbcType = "";
        if(type != null) {
            jdbcType = ",jdbcType=" + type.name();
        }

        String expression = "#{"+ this.paramName + newFieldName + jdbcType + "}";
        parameter.addParameter(newFieldName, value);
        return expression;
    }
}
