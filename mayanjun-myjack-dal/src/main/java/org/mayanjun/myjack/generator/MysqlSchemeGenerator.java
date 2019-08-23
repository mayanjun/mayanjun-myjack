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

package org.mayanjun.myjack.generator;


import org.apache.commons.lang3.StringUtils;
import org.mayanjun.myjack.api.annotation.Column;
import org.mayanjun.myjack.api.annotation.Index;
import org.mayanjun.myjack.api.annotation.IndexColumn;
import org.mayanjun.myjack.api.annotation.Table;
import org.mayanjun.myjack.api.enums.IndexType;
import org.mayanjun.myjack.util.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Generate the DDL for mysql
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
public class MysqlSchemeGenerator {

    private boolean format = false;


    public MysqlSchemeGenerator(boolean format) {
        this.format = format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public String generate(Class<?> clazz) {
        return generate(clazz, null);
    }


    /**
     * Generate the DDL for mysql
     * @param clazz
     * @return
     */
    public String generate(Class<?> clazz, String customTableName) {
        StringBuffer sb = new StringBuffer();
        if (!clazz.isAnnotationPresent(Table.class))
            throw new IllegalArgumentException("Class '" + clazz.getCanonicalName() + "' is not @Table annotation present");

        Table table = clazz.getAnnotation(Table.class);
        preHandleTableAnnotation(sb, table, clazz, customTableName);
        handleTableDefinitionAnnotation(sb, table, clazz);
        postHandleTableAnnotation(sb, table, clazz);

        sb.append("\r\n");

        return sb.toString();
    }

    public String generate(Class<?> clazz, String customTableName, boolean generateDrop) {
        String ddl = generate(clazz, customTableName);
        if(generateDrop) {
            String tableName;
            if(StringUtils.isNotBlank(customTableName)) tableName = customTableName;
            else tableName = AnnotationHelper.getTableName(clazz);
            String drop = "DROP TABLE IF EXISTS `" + tableName + "`;\r\n";
            ddl = drop + ddl;
        }
        return ddl;
    }

    protected void preHandleTableAnnotation(StringBuffer stringBuffer, Table table, Class<?> clazz, String customTableName) {
        stringBuffer.append("CREATE TABLE ");
        if (table.isTemporary()) stringBuffer.append("TEMPORARY ");
        if (table.ifNotExists()) stringBuffer.append("IF NOT EXISTS ");
        String tableName = null;
        if(StringUtils.isNotBlank(customTableName)) tableName = customTableName;
        else tableName = AnnotationHelper.getTableName(clazz);
        tableName = AnnotationHelper.quoteField(tableName);
        stringBuffer.append(tableName);
        stringBuffer.append(" (");
        if (format) stringBuffer.append("\r\n");
    }

    protected void handleTableDefinitionAnnotation(StringBuffer stringBuffer, Table table, Class<?> clazz) {

        AnnotationHolder primaryHolder = null;

        String cd = "";
        List<AnnotationHolder> holders = AnnotationHelper.getAnnotationHolders(clazz);

        if (holders != null && !holders.isEmpty()) {
            int size = holders.size();
            for (int i = 0; i < size; i++) {
                AnnotationHolder ah = holders.get(i);
                if (ah.getPrimaryKey() != null && ah.getColumn() != null && ah.getOgnl() == null) {
                    primaryHolder = ah;
                } else {
                    if (!SqlUtils.isBlank(cd)) cd += ",\r\n";
                    cd += renderColumn(ah, false, false);
                }
            }

            if (primaryHolder != null) {
                cd = renderColumn(primaryHolder, true, table.autoIncrement()) + (SqlUtils.isBlank(cd) ? "" : ",\r\n") + cd;
            }

            stringBuffer.append(cd);
        }

        // handle primary key
        if (primaryHolder != null) {
            String pk = ",\r\n";
            if (format) pk += "\t";
            pk += "PRIMARY KEY(" + AnnotationHelper.quoteField(AnnotationHelper.getColumnName(primaryHolder)) + ")";
            stringBuffer.append(pk);
        }

        // handle indexes
        Index[] indexes = table.indexes();
        if (indexes != null && indexes.length > 0) {
            for (int i = 0; i < indexes.length; i++) {
                Index idx = indexes[i];
                String name = idx.value();
                if ("#".equals(name)) {
                    name = "idx_" + Math.abs(UUID.randomUUID().toString().hashCode());
                }
                String idxDef = ",\r\n";
                if (format) idxDef += "\t";

                String idxColString = toString(idx.columns(), clazz);
                if (SqlUtils.isBlank(idxColString)) continue;
                idxDef += (idx.type() == IndexType.NULL ? "" : idx.type().name() + " ") + "KEY " + AnnotationHelper.quoteField(name) + " " + "(" + idxColString + ")";

                stringBuffer.append(idxDef);
            }
        }
    }

    private String toString(IndexColumn[] ics, Class<?> beanType) {
        String s = "";
        if (ics != null && ics.length > 0) {
            for (int i = 0; i < ics.length; i++) {
                String colName = AnnotationHelper.getColumnName(ics[i].value(), beanType);
                s += AnnotationHelper.quoteField(colName);

				int indexLen = ics[i].length();
                if (indexLen > 0) {
					if(indexLen > 32) indexLen = 32;
					s += "(" + indexLen + ")";
				} else {
					AnnotationHolder h = AnnotationHelper.getAnnotationHolder(ics[i].value(), beanType);
					String len = h.getColumn().length();
					if(isNumber(len)) {
						int l = Integer.parseInt(len);
						if(l > 32) s += "(" + 32 + ")";
					}
				}
                if (i < ics.length - 1) s += ",";
            }
        }
        return s;
    }

    private boolean isNumber(String s) {
		if(s == null || s.length() == 0) return false;
		for(char c : s.toCharArray()) {
			if(!Character.isDigit(c)) return false;
		}
		return true;
	}

    private String renderColumn(AnnotationHolder ah, boolean isPrimaryKey, boolean autoIncrement) {
        Column column = ah.getColumn();

        String colDef = "";
        if (format) colDef += "\t";
        colDef += AnnotationHelper.quoteField(AnnotationHelper.getColumnName(ah));
        colDef += " " + column.type();

        String defaultValue = column.defaultValue();
        if(SqlUtils.isBlank(defaultValue) || "#".equals(defaultValue)) defaultValue = ah.getColumn().type().getDefaultValue();

        List<String> names = getEnumNames(ah.getField().getType());
        if(names != null && !names.isEmpty()) defaultValue = "'" + names.get(0) + "'";

        if (SqlUtils.isBlank(column.length())) {
            String len = "";

            switch (ah.getColumn().type()) {
                case VARCHAR: len = "255"; break;
                case VARBINARY: len = "256"; break;
                case ENUM:
                    len = SqlUtils.listToString(names, ",", "'");
                    break;
            }
            if(SqlUtils.isNotBlank(len)) colDef += "(" + len + ")";
        }else {
            colDef += "(" + column.length() + ")";
        }

        // render options
        if(column.unsigned() && column.type().isNumeric()) colDef += " UNSIGNED";

        // render not null
        if (column.notNull()) colDef += " NOT NULL";

        if (isPrimaryKey && autoIncrement) colDef += " AUTO_INCREMENT";

        if (! autoIncrement) {
            if (SqlUtils.isNotBlank(defaultValue)) {
                colDef += " DEFAULT " + defaultValue;
            }
        }

        if (!SqlUtils.isBlank(column.comment())) colDef += " COMMENT " + SqlUtils.escapeSQLParameter(column.comment(), AnnotationHelper.DEFAULT_CHARSET, false);

        return colDef;
    }

    private List<String> getEnumNames(Class<?> enumType) {
        if(enumType.isEnum()) {
            Class<Enum> ft = (Class<Enum>)enumType;
            Enum es[] = ft.getEnumConstants();
            if(es != null && es.length > 0) {
                List<String> names = new ArrayList<String>();
                for(Enum e : es) names.add(e.name());
                return names;
            }
        }
        return null;
    }

    protected void postHandleTableAnnotation(StringBuffer stringBuffer, Table table, Class<?> clazz) {
        if (format) stringBuffer.append("\r\n");
        stringBuffer.append(") ");
        String engine = "InnoDB";
        if (!SqlUtils.isBlank(table.engine())) {
            engine = table.engine();
        }
        stringBuffer.append("ENGINE=" + engine);
        if (SqlUtils.isBlank(table.charset())) stringBuffer.append(" DEFAULT CHARSET=utf8");
        else stringBuffer.append(" DEFAULT CHARSET=" + table.charset());

        if (SqlUtils.isBlank(table.collate())) stringBuffer.append(" COLLATE=utf8_bin");
        else stringBuffer.append(" COLLATE=" + table.collate());

        if (!SqlUtils.isBlank(table.comment())) stringBuffer.append(" COMMENT=" + SqlUtils.escapeSQLParameter(table.comment(),table.charset(), false));
        stringBuffer.append(";");
    }

}
