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

package org.mayanjun.myjack.api.annotation;

import org.mayanjun.myjack.api.enums.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Column used to annotate a field that mapped to a column of table in database
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * The column name, by default uses the field name
     * @return column name
     */
    String value() default "";

    /**
     * The data type of database
     * @return data type
     */
    DataType type() default DataType.VARCHAR;

    /**
     * Specifies whether the numeric data type is unsigned
     * @return boolean
     */
    boolean unsigned() default false;

    /**
     * Specifies the DDL length expression
     * @return length definition
     */
    String length() default "";

    /**
     * Specifies whether the NULL value be allowed on this column
     * @return true if null value not be allowed
     */
    boolean notNull() default true;

    /**
     * Specified the default value to this column
     * @return the default value for this column
     */
    String defaultValue() default "";

    /**
     * Specified the comment
     * @return comments for column
     */
    String comment() default "";

    /**
     * Specifies the reference field when a column field is referenced to an EntityBean.
     * When the referenceField is specified, the query parse will generate a select field with 'AS'
     * clause for this field. For example, consider the following declare:
     * <pre>
     *     &#x00040;Column(type = DataType.BIGINT, comment = "项目ID", referenceField = "id")
     *     private Subject subject;
     * </pre>
     * the selected field will be: subject AS `subject.id`
     * @return reference field name
     */
    String referenceField() default "";
}
