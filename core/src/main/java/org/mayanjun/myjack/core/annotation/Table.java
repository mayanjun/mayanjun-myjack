package org.mayanjun.myjack.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table used to annotate a class to mapping a class to a database table
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * Specifies the table name, by default the t_className used
     * @return table name
     */
    String value() default "";

    /**
     * Specifies whether the table is temporary
     * @return true if this table is temporary
     */
    boolean isTemporary() default false;

    /**
     * The 'IF NOT EXISTS' phase will be used if this value is set to true
     * @return true if IF NOT EXISTS is present
     */
    boolean ifNotExists() default true;

    /**
     * Specifies the definitions of index
     * @return index definitions
     */
    Index[] indexes() default {};

    /**
     * Specifies the store engine
     * @return engine definition
     */
    String engine() default "";

    /**
     * Specifies the default charset
     * @return charset name
     */
    String charset() default "utf8";

    /**
     * Specifies comment
     * @return table comments
     */
    String comment() default "";

    String collate() default "utf8_bin";

    /**
     * Auto increment id
     * @return
     */
    boolean autoIncrement() default false;
}
