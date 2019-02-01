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

package org.mayanjun.myjack.core.enums;

/**
 * DataType
 *
 * @author mayanjun(8/19/15)
 */
public enum DataType {
    INT("0", true),
    TINYINT("0", true),
    BIGINT("0", true),
    SMALLINT("0", true),
    MEDIUMINT("0", true),

    FLOAT("0", true),
    DOUBLE("0", true),
    BIT("b'0'", false),

    CHAR("''", true),
    VARCHAR("''", false),
    TINYTEXT(null, false),
    TEXT(null, false),
    MEDIUMTEXT(null, false),
    LONGTEXT(null, false),

    BLOB(null, false),
    TINYBLOB(null, false),
    MEDIUMBLOB(null, false),
    LONGBLOB(null, false),
    BINARY("''", false),
    VARBINARY("''", false),
    ENUM(null, false),
    SET(null, false),

    DATE("'0000-00-00'", false),
    DATETIME("'0000-00-00 00:00:00'", false),
    TIMESTAMP("'0000-00-00 00:00:00'", false),
    TIME("'00:00:00'", false),
    YEAR("'0000'", false);

    private String defaultValue;
    private boolean numeric;

    DataType(String defaultValue, boolean numeric) {
        this.defaultValue = defaultValue;
        this.numeric = numeric;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isNumeric() {
        return numeric;
    }
}
