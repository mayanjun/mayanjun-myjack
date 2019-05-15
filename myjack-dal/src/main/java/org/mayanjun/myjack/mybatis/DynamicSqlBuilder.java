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

package org.mayanjun.myjack.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.type.JdbcType;
import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.api.enums.DataType;
import org.mayanjun.myjack.generator.AnnotationHelper;
import org.mayanjun.myjack.generator.AnnotationHolder;
import org.mayanjun.myjack.parser.SQLParameter;
import org.mayanjun.myjack.util.SQLBuilder;
import org.mayanjun.myjack.util.SqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mayanjun.myjack.generator.AnnotationHelper.*;

/**
 * MyBatisDynamicSqlBuilder
 *
 * @author mayanjun(6/22/16)
 * @since 0.0.5
 */
public class DynamicSqlBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicSqlBuilder.class);

    public String buildQuery(@Param(DynamicMapper.PARAM_NAME)final SQLParameter parameter, Sharding sharding) throws Exception {
        if(sharding != null) {
            String tn = sharding.getTableName(parameter.getQuery());
            if(StringUtils.isNotBlank(tn)) parameter.setTableName(tn);
        }
        String sql = parameter.getSql();
        if(LOG.isDebugEnabled())  LOG.debug("Query SQL=" + sql);
        return sql;
    }

    public String buildCount(@Param(DynamicMapper.PARAM_NAME)final SQLParameter parameter, Sharding sharding) throws Exception {
        if(sharding != null) {
            String tn = sharding.getTableName(parameter.getQuery());
            if(StringUtils.isNotBlank(tn)) parameter.setTableName(tn);
        }
        String sql = parameter.getCountSql();
        if(LOG.isDebugEnabled())  LOG.debug("Count Query SQL=" + sql);
        return sql;
    }

    public String buildInsert(@Param(DynamicMapper.PARAM_NAME)final Object bean, Sharding sharding) throws Exception {
        SqlValues sqlValues = getSqlValues(bean, sharding, DynamicMapper.PARAM_NAME);
        String sql = buildInsertInternal(sqlValues, bean);
        if(LOG.isDebugEnabled())  LOG.debug("Insert SQL=" + sql);
        return sql;
    }

    public String buildQueryUpdate(@Param(DynamicMapper.PARAM_NAME)SQLParameter parameter, Sharding sharding) {
        String entityName = parameter.getEntityName();
        Object bean = parameter.get(entityName);
        SqlValues sqlValues = getSqlValues(bean, sharding, DynamicMapper.PARAM_NAME + "." + entityName);
        String sql = buildUpdateInternal(sqlValues, bean, parameter.getWhereClause());
        if(LOG.isDebugEnabled())  LOG.debug("Query Update SQL=" + sql);
        return sql;
    }

    public String buildUpdate(@Param(DynamicMapper.PARAM_NAME)final Object bean, Sharding sharding) throws Exception {
        SqlValues sqlValues = getSqlValues(bean, sharding, DynamicMapper.PARAM_NAME);
        String sql = buildUpdateInternal(sqlValues, bean, null);
        if(LOG.isDebugEnabled())  LOG.debug("Update SQL=" + sql);
        return sql;
    }

    public String buildDelete(@Param(DynamicMapper.PARAM_NAME)final Object bean, Sharding sharding) throws Exception {
        SqlValues values = getSqlPrimaryValue(bean, sharding, DynamicMapper.PARAM_NAME);
        String sql = SQLBuilder.custom()
                .deleteFrom(quoteField(values.tableName))
                .where(values.primaryFieldKey + "=" + values.getPrimaryFieldValue)
                .build();
        if(LOG.isDebugEnabled())  LOG.debug("Delete SQL=" + sql);
        return sql;
    }

    public String buildQueryDelete(@Param(DynamicMapper.PARAM_NAME)SQLParameter parameter, Sharding sharding) {
        String tableName = null;
        if(sharding != null) {
            tableName = sharding.getTableName(parameter.getQuery());
        }
        if(StringUtils.isBlank(tableName)) tableName = AnnotationHelper.getTableName(parameter.getResultType());
        String sql = SQLBuilder.custom()
                .deleteFrom(quoteField(tableName))
                .append(parameter.getWhereClause())
                .build();
        if(LOG.isDebugEnabled())  LOG.debug("Query Delete SQL=" + sql);
        return sql;
    }

    protected String buildInsertInternal(SqlValues sqlValues, Object bean) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + quoteField(sqlValues.tableName) + "(");
        Map<String, String> valuesMap = sqlValues.valueMap;

        if(valuesMap.isEmpty()) {
            String message = "All of fields in bean " + bean.getClass() + " is null";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }

        int size = valuesMap.size();
        int count = 0;
        StringBuilder values = new StringBuilder();

        // append id field first
        if(sqlValues.primaryFieldKey != null) {
            sql.append(sqlValues.primaryFieldKey + ",");
            values.append(sqlValues.getPrimaryFieldValue + ",");
        }

        for(Map.Entry<String, String> entry : valuesMap.entrySet()) {
            ++count;
            sql.append(entry.getKey());
            values.append(entry.getValue());
            if(count < size) {
                sql.append(",");
                values.append(",");
            }
        }
        sql.append(") VALUES(").append(values).append(")");
        String ret = sql.toString();
        if(LOG.isDebugEnabled()) {
            LOG.debug(ret);
        }
        return ret;
    }

    protected String buildUpdateInternal(SqlValues sqlValues, Object bean, String where) {
        Map<String, String> valuesMap = sqlValues.valueMap;

        if(valuesMap.isEmpty()) {
            String message = "All of fields in bean " + bean.getClass() + " is null(No value to be updated)";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }

        StringBuilder sql = new StringBuilder("UPDATE " + quoteField(sqlValues.tableName) + " SET ");

        int size = valuesMap.size();
        int count = 0;
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String, String> entry : valuesMap.entrySet()) {
            ++count;
            sql.append(entry.getKey()).append("=").append(entry.getValue());
            if(count < size) {
                sql.append(",");
                values.append(",");
            }
        }
        if(StringUtils.isNotBlank(where)) {
            sql.append(" " + where);
        } else {
            if(sqlValues.primaryFieldKey == null) {
                String message = "No valid value for primary key:" + bean.getClass() + ", pk=" + sqlValues.primaryFieldKey;
                LOG.warn(message);
                throw new IllegalArgumentException(message);
            }
            sql.append(" WHERE " + sqlValues.primaryFieldKey + "=" + sqlValues.getPrimaryFieldValue);
        }

        if(LOG.isDebugEnabled()) {
            LOG.debug(sql.toString());
        }

        return sql.toString();
    }



    protected SqlValues getSqlValues(Object bean, Sharding sharding, String paramName) {

        SqlValues values = new SqlValues();
        Class<?> beanType = bean.getClass();

        if(sharding != null) {
            String tn = sharding.getTableName(bean);
            if(StringUtils.isNotBlank(tn)) values.tableName = tn;
        }

        if(values.tableName == null) values.tableName = AnnotationHelper.getTableName(bean.getClass());

        Map<String, AnnotationHolder> map = getAnnotationHoldersMap(beanType);

        Map<String, String> valuesMap = new HashMap<String, String>();

        for(Map.Entry<String, AnnotationHolder> entry : map.entrySet()) {
            String ognl = entry.getKey();
            AnnotationHolder ah = entry.getValue();

            // 当referenceField存在的话, 字段的ognl表达式应该是自身的ognl+referenceField
            String ref = ah.getColumn().referenceField();
            if(SqlUtils.isNotBlank(ref)) {
                ognl = ognl + "." + ref;
            }
            Object value = null;
            try {
                value = Ognl.getValue(ognl, bean);
            } catch (Exception e) {}

            if(value != null) {
                fillValueMap(values, ah, valuesMap, paramName, ognl);
            }
        }
        values.valueMap = valuesMap;
        return values;

    }

    /**
     *
     * @param bean
     * @param sharding
     * @param paramName
     * @param fields
     * @return
     */
    protected SqlValues getSqlValues(Object bean, Sharding sharding, String paramName, List<String> fields) {

        SqlValues values = new SqlValues();
        Class<?> beanType = bean.getClass();

        if(sharding != null) {
            String tn = sharding.getTableName(bean);
            if(StringUtils.isNotBlank(tn)) values.tableName = tn;
        }

        if(values.tableName == null) values.tableName = AnnotationHelper.getTableName(bean.getClass());

        Map<String, AnnotationHolder> map = getAnnotationHoldersMap(beanType);

        Map<String, String> valuesMap = new HashMap<String, String>();

        for(String field : fields) {
            String ognl = field;
            AnnotationHolder ah = map.get(ognl);
            if(ah == null) continue;

            // 当referenceField存在的话, 字段的ognl表达式应该是自身的ognl+referenceField
            String ref = ah.getColumn().referenceField();
            if(SqlUtils.isNotBlank(ref)) {
                ognl = ognl + "." + ref;
            }

            Object value = null;
            try {
                value = Ognl.getValue(ognl, bean);
            } catch (Exception e) {}
            if(value != null) {
                fillValueMap(values, ah, valuesMap, paramName, ognl);
            }
        }
        values.valueMap = valuesMap;
        return values;
    }

    protected SqlValues getSqlPrimaryValue(Object bean, Sharding sharding, String paramName) {
        SqlValues values = new SqlValues();
        Class<?> beanType = bean.getClass();

        if(sharding != null) {
            String tn = sharding.getTableName(bean);
            if(StringUtils.isNotBlank(tn)) values.tableName = tn;
        }
        if(values.tableName == null) values.tableName = AnnotationHelper.getTableName(bean.getClass());

        AnnotationHolder primaryHolder = AnnotationHelper.getPrimaryAnnotationHolder(beanType);
        if(primaryHolder == null) {
            String message = "No primary field found:" + bean.getClass();
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }

        String fieldName = quoteField(getColumnName(primaryHolder));
        // set compatibility with mybatis
        DataType type = primaryHolder.getColumn().type();
        if(type == DataType.DATETIME) type = DataType.TIMESTAMP;
        String ognl = primaryHolder.getField().getName();

        Object value = null;
        try {
            value = Ognl.getValue(ognl, bean);
        } catch (Exception e) {}

        if(value == null) {
            String message = "The value of primary field is null: " + bean.getClass().getName() + "." + primaryHolder.getField().getName();
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }

        if(primaryHolder.getOgnl() != null) ognl = primaryHolder.getOgnl() + "." + ognl;
        String jdbcType = "";
        JdbcType t = DataTypeJdbcTypeAdapter.jdbcType(type);
        if(t != null) {
            jdbcType = ",jdbcType=" + t.name();
        }
        String fieldValue = "#{" + (StringUtils.isNotBlank(paramName) ? paramName + "." : "") + ognl + jdbcType + "}";

        values.primaryFieldKey = fieldName;
        values.getPrimaryFieldValue = fieldValue;

        return values;

    }


    /**
     *
     * @param values
     * @param ah
     * @param valuesMap
     * @param paramName
     * @param ognl 调用者必须传回一个正确的ognl
     */
    private void fillValueMap(SqlValues values, AnnotationHolder ah, Map<String, String> valuesMap, String paramName, String ognl) {
        String fieldName = quoteField(getColumnName(ah));
        // set compatibility with mybatis
        JdbcType type = DataTypeJdbcTypeAdapter.jdbcType(ah.getColumn().type());
        String jdbcType = "";
        if(type != null) {
            jdbcType = ",jdbcType=" + type.name();
        }

        String fieldValue = "#{"+ (StringUtils.isNotBlank(paramName) ? paramName + "." : "") + ognl + jdbcType + "}";
        /**
         * 顶层主键
         */
        if(ah.getOgnl() == null && ah.getPrimaryKey() != null)  {
            values.primaryFieldKey = fieldName;
            values.getPrimaryFieldValue = fieldValue;
        } else {
            valuesMap.put(fieldName, fieldValue);
        }
    }

    public static class SqlValues {
        public Map<String, String> valueMap;
        public String tableName;
        public String primaryFieldKey;
        public String getPrimaryFieldValue;
    }

}
