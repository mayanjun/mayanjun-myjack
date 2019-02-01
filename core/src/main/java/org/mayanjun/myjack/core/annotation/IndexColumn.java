package org.mayanjun.myjack.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Index column
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface IndexColumn {

    /**
     * Specifies the name of field to which the column mapped to be set as index
     * @return index name
     */
    String value();

    /**
     * Specifies the length of index
     * @return index length
     */
    int length() default 0;
}
