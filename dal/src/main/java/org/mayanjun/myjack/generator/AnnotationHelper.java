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

import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.annotation.Component;
import org.mayanjun.myjack.core.annotation.PrimaryKey;
import org.mayanjun.myjack.core.annotation.Table;
import org.mayanjun.myjack.util.ClassUtils;
import org.mayanjun.myjack.util.SqlUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper used to get metadata of a table-mapped class
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.5
 */
public class AnnotationHelper {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final Map<Class<?>, Map<String, AnnotationHolder>> ANNOTATIONHOLDERS_CACHE;
    private static final Map<Class<?>, List<AnnotationHolder>> ANNOTATIONHOLDER_LIST_CACHE;
    private static final Map<Class<?>, AnnotationHolder> PRIMARY_ANNOTATIONHOLDER_CACHE;


    static {
        ANNOTATIONHOLDERS_CACHE = new ConcurrentHashMap<Class<?>, Map<String, AnnotationHolder>>(new IdentityHashMap<Class<?>, Map<String, AnnotationHolder>>());
        ANNOTATIONHOLDER_LIST_CACHE = new ConcurrentHashMap<Class<?>, List<AnnotationHolder>>(new IdentityHashMap<Class<?>, List<AnnotationHolder>>());
        PRIMARY_ANNOTATIONHOLDER_CACHE = new ConcurrentHashMap<Class<?>, AnnotationHolder>(new IdentityHashMap<Class<?>, AnnotationHolder>());
    }

    private static final String quote = "`";

    /**
     * Quote field in backquote
     * @param field string source
     * @return quoted string
     */
    public static String quoteField(String field) {
        return quote + field + quote;
    }

    /**
     * Returns the table name
     * @param clazz class to be used
     * @return table name
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if(table == null) throw new IllegalArgumentException("No @Table annotation found on type:" + clazz);
        String tableName = table.value();
        if ("#".equals(tableName) || SqlUtils.isBlank(tableName)) {
            tableName = "t_" + clazz.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    /**
     * Return the column name mapped to the specified field
     * @param field field name
     * @param beanType bean type
     * @return
     */
    public static String getColumnName(String field, Class<?> beanType) {
        AnnotationHolder an = getAnnotationHolder(field, beanType);
        if(an == null) {
            throw new IllegalArgumentException("Could not get AnnotationHolder from type:" + beanType + " with field:" + field);
        }
        return getColumnName(an);
    }

    /**
     * Returns an {@link AnnotationHolder} that corresponds to the specified name by field
     * @param field field name
     * @param beanType class
     * @return null if no annotation found
     */
    public static AnnotationHolder getAnnotationHolder(String field, Class<?> beanType) {
        Map<String, AnnotationHolder> map = getAnnotationHoldersMap(beanType);
        AnnotationHolder holder = map.get(field);
        if(holder == null) {
            holder = smartSearch(field, map);
        }
        return holder;
    }

    /**
     * Smart search for {@link AnnotationHolder}
     * @param field field name
     * @param map
     * @return
     */
    private static AnnotationHolder smartSearch(String field, Map<String, AnnotationHolder> map) {
        int index = field.lastIndexOf(".");
        if(index > 0) {
            String prefixField = field.substring(0, index);
            String suffixField = field.substring(index+1);
            AnnotationHolder ah = map.get(prefixField);
            if(ah != null && suffixField.equals(ah.getColumn().referenceField())) return ah;
            return smartSearch(prefixField, map);
        }
        return null;
    }

    /**
     * Returns the {@link AnnotationHolder} of primary key in the specified key
     * @param beanType the bean type
     * @return
     */
    public static AnnotationHolder getPrimaryAnnotationHolder(Class<?> beanType) {
        AnnotationHolder primary = PRIMARY_ANNOTATIONHOLDER_CACHE.get(beanType);
        if(primary == null) {
            List<AnnotationHolder> list = getAnnotationHolders(beanType);
            for(AnnotationHolder ah : list) {
                if(ah.getPrimaryKey() != null) {
                    primary = ah;
                    PRIMARY_ANNOTATIONHOLDER_CACHE.put(beanType, ah);
                    break;
                }
            }
        }
        return primary;
    }

    /**
     * Parse the column name
     * @param annotationHolder holder object
     * @return
     */
    public static String getColumnName(AnnotationHolder annotationHolder) {
        Column c = annotationHolder.column;
        Field f = annotationHolder.getField();
        String name = c.value();
        if (SqlUtils.isBlank(name) || "#".equals(name)) {
            name = f.getName();
        }
        if (!SqlUtils.isBlank(annotationHolder.getOgnl())) {
            name = annotationHolder.getOgnl() + "." + name;
        }
        return SqlUtils.toHumpString(name);
    }

    /**
     * Returns the ognl field name
     * @param annotationHolder annotationHolder object
     * @return the ognl field name
     */
    public static String getOgnlName(AnnotationHolder annotationHolder) {
        Field f = annotationHolder.getField();
        String ognl = annotationHolder.getOgnl();
        if(SqlUtils.isBlank(ognl)) return f.getName();
        return ognl + "." + f.getName();
    }

    /**
     * Returns the {@link AnnotationHolder} map
     * @param cls bean type
     * @return Returns the {@link AnnotationHolder} map
     */
    public static Map<String, AnnotationHolder> getAnnotationHoldersMap(Class<?> cls) {
        Map<String, AnnotationHolder> map = ANNOTATIONHOLDERS_CACHE.get(cls);
        if(map == null) {
            map = getAllColumnAnnotationHoldersInternal(cls, null);
            ANNOTATIONHOLDERS_CACHE.put(cls, map);
        }
        return map;
    }

    /**
     * Returns {@link AnnotationHolder} list for a bean type
     * @param cls bean type
     * @return AnnotationHolder list
     */
    public static List<AnnotationHolder> getAnnotationHolders(Class<?> cls) {
        Map<String, AnnotationHolder> map = getAnnotationHoldersMap(cls);
        List<AnnotationHolder> list = ANNOTATIONHOLDER_LIST_CACHE.get(cls);
        if(list == null) {
            list = new ArrayList<AnnotationHolder>(map.values());
            if(!list.isEmpty()) {
                Collections.sort(list, new Comparator<AnnotationHolder>() {
                    @Override
                    public int compare(AnnotationHolder o1, AnnotationHolder o2) {
                        String s1 = "";
                        String s2 = "";
                        if (!SqlUtils.isBlank(o1.getColumn().length())) s1 = o1.getColumn().length();
                        if (!SqlUtils.isBlank(o2.getColumn().length())) s2 = o2.getColumn().length();
                        int i1 = 0;
                        try {
                            i1 = Integer.parseInt(s1);
                        } catch (NumberFormatException e) {
                        }
                        int i2 = 0;
                        try {
                            i2 = Integer.parseInt(s2);
                        } catch (NumberFormatException e) {
                        }
                        return i1 > i2 ? 1 : (i1 < i2 ? -1 : 0);
                    }
                });
            }
            ANNOTATIONHOLDER_LIST_CACHE.put(cls, list);
        }

        return list;
    }

    /**
     * Returns a readonly map that contains all of annotation holders of a class.
     * @param cls bean type
     * @param ognl Ognl name
     * @return
     */
    private static Map<String, AnnotationHolder> getAllColumnAnnotationHoldersInternal(Class<?> cls, String ognl) {
        ReadOnlyMap<String, AnnotationHolder> holders = new ReadOnlyMap<String, AnnotationHolder>();
        Collection<Field> fields = ClassUtils.getAllFields(cls);

        boolean meetPrimaryKey = false;
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            Field f = iterator.next();
            Column column = f.getAnnotation(Column.class);
            PrimaryKey pk = f.getAnnotation(PrimaryKey.class);
            if (column != null) {
                AnnotationHolder ah = new AnnotationHolder(column, f, pk, ognl);
                holders.putInternal(getOgnlName(ah), ah);
                if (pk != null) {
                    if (meetPrimaryKey)
                        throw new IllegalArgumentException("Duplicated primary key(declared more than one @PrimaryKey annotation in " + cls.getCanonicalName() + ")");
                    meetPrimaryKey = true;
                }

                String referenceField = column.referenceField();
                if (!SqlUtils.isBlank(referenceField)) {
                    Class<?> refClass = f.getType();
                    Collection<Field> refFields = ClassUtils.getAllFields(refClass);
                    boolean found = false;
                    if (refFields != null && !refFields.isEmpty()) {
                        for (Field rf : refFields) {
                            if (referenceField.equals(rf.getName())) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found)
                        throw new IllegalArgumentException("Reference field specified to '" + column.referenceField() + "' but no such field found in " + refClass);
                }
            } else {
                Component component = f.getAnnotation(Component.class);
                String og = ognl;
                if (og != null) og += "." + f.getName();
                else og = f.getName();
                if (component != null) {
                    Map<String, AnnotationHolder> comHolders = getAllColumnAnnotationHoldersInternal(f.getType(), og);
                    if (!comHolders.isEmpty()) {
                        String excludes[] = component.excludes();
                        if (excludes != null && excludes.length > 0) {
                            List<String> ess = Arrays.asList(excludes);
                            Iterator<Map.Entry<String, AnnotationHolder>> it = comHolders.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, AnnotationHolder> entry = it.next();
                                if (ess.contains(entry.getValue().getField().getName())) it.remove();
                            }
                        }
                        holders.putAllInternal(comHolders);
                    }
                }
            }
        }
        return holders;
    }
}
