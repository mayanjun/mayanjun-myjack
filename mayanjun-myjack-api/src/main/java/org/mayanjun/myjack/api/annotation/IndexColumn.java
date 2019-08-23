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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引字段信息
 * Index column
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface IndexColumn {

    /**
     * 需要索引的字段名称，注意是类字段的名称不是数据库字段名称。如果这里的字段名称写错了，导出SQL语句时会抛出找不到字段的异常。
     * Specifies the name of field to which the column mapped to be set as index。A field not found exception
     * will be thrown when exporting DDL if this value is incorrect.
     * @return index name
     */
    String value();

    /**
     * 指定索引长度
     * Specifies the length of index
     * @return index length
     */
    int length() default 0;
}
