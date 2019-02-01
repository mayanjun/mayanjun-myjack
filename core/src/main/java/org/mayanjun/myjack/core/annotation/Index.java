package org.mayanjun.myjack.core.annotation;

import org.mayanjun.myjack.core.enums.IndexType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Config the index
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Index {
    String value() default "#";
    IndexColumn[] columns();

    IndexType type() default IndexType.NULL;
}
