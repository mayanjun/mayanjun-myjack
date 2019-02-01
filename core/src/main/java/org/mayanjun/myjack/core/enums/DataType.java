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
