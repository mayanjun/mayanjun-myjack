package org.mayanjun.myjack.generator;

import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.annotation.PrimaryKey;

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