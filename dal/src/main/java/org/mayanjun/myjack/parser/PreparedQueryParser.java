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

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.mayanjun.myjack.core.enums.DataType;
import org.mayanjun.myjack.mybatis.DataTypeJdbcTypeAdapter;

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
