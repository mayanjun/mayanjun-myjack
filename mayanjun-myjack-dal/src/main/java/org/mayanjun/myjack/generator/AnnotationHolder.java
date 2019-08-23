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

import org.mayanjun.myjack.api.annotation.Column;
import org.mayanjun.myjack.api.annotation.PrimaryKey;

import java.lang.reflect.Field;

/**
 * Holds all annotations on a field
 *
 * @author mayanjun
 * @since 0.0.5
 */
public class AnnotationHolder {

        Column column;
        PrimaryKey primaryKey;
        private Field field;
        private String ognl;

        AnnotationHolder(Column column, Field field, PrimaryKey primaryKey, String ognl) {
            this.column = column;
            this.field = field;
            this.primaryKey = primaryKey;
            this.ognl = ognl;
        }

        public Column getColumn() {
            return column;
        }

        public PrimaryKey getPrimaryKey() {
            return primaryKey;
        }

        public Field getField() {
            return field;
        }

        public String getOgnl() {
            return ognl;
        }
    }