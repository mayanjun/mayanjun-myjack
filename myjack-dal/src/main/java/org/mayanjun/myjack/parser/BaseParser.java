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

import org.mayanjun.myjack.api.annotation.Column;
import org.mayanjun.myjack.api.annotation.Table;
import org.mayanjun.myjack.api.enums.DataType;
import org.mayanjun.myjack.api.query.*;
import org.mayanjun.myjack.generator.AnnotationHelper;
import org.mayanjun.myjack.generator.AnnotationHolder;
import org.mayanjun.myjack.util.SqlUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BaseParser
 *
 * @author mayanjun(6/20/16)
 * @since 0.0.5
 */
public abstract class BaseParser implements QueryParser {

    public Pattern FIELD_PATTERN = Pattern.compile("#\\{([a-zA-Z_$0-9]+)\\}");

    private static final ConcurrentHashMap<Class<?>, Map<String, String>> FIELDS_CACHE = new ConcurrentHashMap<Class<?>, Map<String, String>>();

    /**
     *
     * @param beanType
     * @return
     */
    protected Collection<String> getAllFields(Class<?> beanType) {
        Map<String, String> fs = FIELDS_CACHE.get(beanType);
        if(fs != null) return fs.values();
        else fs = new HashMap<String, String>();

        List<AnnotationHolder> ahs = AnnotationHelper.getAnnotationHolders(beanType);
        if(ahs != null && !ahs.isEmpty()) {
            fs = new HashMap<String, String>();
            int size = ahs.size();
            for(int i = 0; i < size; i++) {
                AnnotationHolder ah = ahs.get(i);
                String columnName = AnnotationHelper.getColumnName(ah);
                String name = AnnotationHelper.quoteField(columnName);
                String as = ah.getField().getName();
                String ref = ah.getColumn().referenceField();
                String ognl = ah.getOgnl();
                if(SqlUtils.isNotBlank(ognl)) {
                    as = ognl + "." + ah.getField().getName();
                    name = name + " AS `" + as + "`";
                } else if(SqlUtils.isNotBlank(ref)) {
                    String refAs = as + "." + ref;
                    name = name + " AS `" + refAs + "`";
                    fs.put(refAs, name);
                } else if(!columnName.equals(as)) {
                    name = name + " AS `" + as + "`";
                }
                fs.put(as, name);
            }
            FIELDS_CACHE.put(beanType, fs);
        }
        return fs.values();
    }

    /**
     * Returns select field with specified OGNL field name
     * @param field ognl-style field name
     * @param query query to be parsed
     * @param inWhere if false, the 'AS' clause will be generated
     * @return select field with specified OGNL field name
     */
    protected String getSelectField(String field, Query query, boolean inWhere) {
        Class<?> beanType = query.getBeanType();
        AnnotationHolder ah = AnnotationHelper.getAnnotationHolder(field, beanType);
        if(ah == null) throw new IllegalArgumentException("no field found by name '" + field + "' in class '" + beanType);
        String columnName = AnnotationHelper.getColumnName(ah);
        String fieldName = ah.getField().getName();
        String name = AnnotationHelper.quoteField(columnName);
        if(!inWhere) {
            String ref = ah.getColumn().referenceField();
            if(SqlUtils.isNotBlank(ah.getOgnl())) {
                name = name + " AS `" + ah.getOgnl() + "." + fieldName + "`";
            } else if(SqlUtils.isNotBlank(ref)) {
                name = name + " AS `" + fieldName + "." + ref + "`";
            } else if(!columnName.equals(fieldName)) {
                name = name + " AS `" + fieldName + "`";
            }
        }
        return name;
    }

    public void parseSelectedFields(SQLParameter parameter, Query<? extends Serializable> query) {
        Collection<String> list = getSelectedFields(query);
        String sql = SqlUtils.listToString(list, ",");
        parameter.setSelectedFields(sql);

    }

    public void parseFrom(SQLParameter parameter, Query<? extends Serializable> query) {
        String tableName = AnnotationHelper.getTableName(query.getBeanType());
        parameter.setTableName(tableName);
    }

    /**
     *
     * @param query
     * @return
     */
    protected Collection<String> getSelectedFields(Query<? extends Serializable> query) {
        List<String> includeList = query.getIncludeFields();

        Class<?> beanType = query.getBeanType();
        Collection<String> allFields0 = getAllFields(beanType);

        Collection<String> allFields = new ArrayList<String>(allFields0);


        if(includeList != null && !includeList.isEmpty()) {
            Map<String, String> fs = FIELDS_CACHE.get(beanType);
            List<String> ins = new ArrayList<String>();
            for(String s : includeList) {
                String field = fs.get(s);
                if(field != null) ins.add(field);
            }
            allFields = ins;
        }

        List<String> excludedFields = query.getExcludeFields();
        if(excludedFields != null && !excludedFields.isEmpty()) {
            List<String> excludedParsedFields = new ArrayList<String>();
            for(String name : excludedFields) {
                String parsedName = getSelectField(name, query, false);
                excludedParsedFields.add(parsedName);
            }
            allFields.removeAll(excludedParsedFields);

        }
        return allFields;
    }

    public void parseWhere(SQLParameter parameter, Query query) {
        StringBuilder where = new StringBuilder();
        if(query != null) {

            List<SqlComparator> comparators = query.getComparators();
            boolean hasComparators = (comparators != null && !comparators.isEmpty());
            if(hasComparators) {
                where.append(" WHERE ");
                for(int i = 0; i < comparators.size(); i++) {
                    SqlComparator c = comparators.get(i);
                    if(i > 0) {
                        if(c instanceof GroupComparator) {
                            if(!((GroupComparator) c).isStart()) {
                                where.append(c.getExpression());
                                continue;
                            }
                        }
                        if(c instanceof LogicalComparator) {
                            SqlComparator prev = comparators.get(i - 1);
                            if(!(prev instanceof GroupComparator) || !((GroupComparator) prev).isStart()) {
                                LogicalOperator lo = c.getLogicalOperator();
                                if(lo == null) lo = SqlComparator.DEFAULT_LOGICAL_OPERATOR;
                                where.append(" " + lo + " ");
                            }
                        }
                    }
                    Table table = (Table) query.getBeanType().getAnnotation(Table.class);
                    String charset = AnnotationHelper.DEFAULT_CHARSET;
                    if(table != null) {
                        charset = table.charset();
                    }
                    String comString = renderComparator(parameter, c, query, charset);
                    where.append(comString);
                }
            }
            parameter.setWhereClause(where.toString());
        }
    }

    public void parseOrderClause(SQLParameter parameter, Query query) {
        StringBuilder sb = new StringBuilder();
        Set<Sort> sorts = query.sorts();
        if (sorts != null && !sorts.isEmpty()) {
            for (Sort sort : sorts) {
                if(sort != null && !SqlUtils.isBlank(sort.getName())) {
                    sb.append(SqlUtils.escapeSQLField(sort.getName(), false) + "");
                    if(sort.getDirection() != null) sb.append(" " + sort.getDirection().name());
                    sb.append(",");
                }
            }
            int len = sb.length();
            if (len > 0) {
                if (sb.charAt(len - 1) == ',') sb.deleteCharAt(len - 1);
                sb.insert(0, " ORDER BY ");
            }
            parameter.setOrderClause(sb.toString());
        }
    }

	public void parseLimitClause(SQLParameter parameter, Query query) {
		int limits[]  = query.getLimits();

		int offset = limits[0];
		int limit = limits[1];

		if (limit > 0) {
		    if(offset <= 0) parameter.setLimitClause(" LIMIT " + limit);
		    else parameter.setLimitClause(" LIMIT " + offset + "," + limit);
        }
	}

    public String renderComparator(SQLParameter parameter, SqlComparator comparator, Query<?> query, String charset){
        String expression = comparator.getExpression();
        Matcher m = FIELD_PATTERN.matcher(expression);

        DataType dataType = null;

        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String field = m.group(1);
            if("name".equals(field)) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(field, comparator.getClass());
                    Method mmm = pd.getReadMethod();
                    String value = (String) mmm.invoke(comparator);
                    String selectedField = getSelectField(value.toString(), query, true);
                    String sss = SqlUtils.escapeSQLField(selectedField, false);
                    m.appendReplacement(sb, sss);

                    AnnotationHolder ah = AnnotationHelper.getAnnotationHolder(value, query.getBeanType());
                    Column col = ah.getColumn();
                    dataType = col.type();

                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Object value = renderFieldValue(parameter, field, comparator, query, charset, dataType);
                m.appendReplacement(sb, value.toString());
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Parse value for a field
     * @param name java field name
     * @param comparator current SqlComparator
     * @param query query
     * @param charset charset to be used
     * @param dataType JDBC data type
     * @return
     */
    public Object renderFieldValue(SQLParameter parameter, String name, SqlComparator comparator, Query query, String charset, DataType dataType){
        try {
            PropertyDescriptor pd = new PropertyDescriptor(name, comparator.getClass());
            Method mmm = pd.getReadMethod();
            Object value = mmm.invoke(comparator);

            if(comparator instanceof InComparator) {
                Collection<Object> coll = null;
                if(value.getClass().isArray()) {
                    int len = Array.getLength(value);
                    coll = new ArrayList<Object>();
                    Class<?> comType = value.getClass().getComponentType();
                    if(comType.isPrimitive()) {
                        String type = comType.getName();
                        String methodName = SqlUtils.toHumpString("get." + type);
                        Method method = Array.class.getMethod(methodName, Object.class, int.class);
                        for(int i = 0; i < len; i++) {
                            Object comValue = method.invoke(Array.class, value, i);
                            coll.add(comValue);
                        }

                    } else {
                        Object[] objs = (Object[]) value;
                        coll = Arrays.asList(objs);
                    }
                } else {
                    coll = (Collection<Object>)(((InComparator) comparator).getValue());
                }
                return renderCollection(parameter, coll, charset, dataType);
            } else {
                return renderValue(parameter, value, charset, dataType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object renderCollection(SQLParameter parameter, Collection list, String charset, DataType dataType) {
        int count = 0;
        int len = list.size();
        String ret = "";
        for(Object value : list) {
            ret += renderValue(parameter, value, charset, dataType);
            if(count < len - 1) ret += ",";
            count++;
        }
        return ret;
    }

    public abstract Object renderValue(SQLParameter parameter, Object value, String charset, DataType dt);

    @Override
    public <T extends Serializable> SQLParameter<T> parse(Query<T> query) {
        SQLParameter<T> parameter = new SQLParameter<T>(query);
        parameter.setResultType(query.getBeanType());
        parseSelectedFields(parameter, query);
        parseFrom(parameter, query);
        parseWhere(parameter, query);
		parseOrderClause(parameter, query);
		parseLimitClause(parameter, query);
        return parameter;
    }
}