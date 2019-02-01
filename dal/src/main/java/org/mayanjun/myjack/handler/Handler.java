package org.mayanjun.myjack.handler;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Handler
 *
 * @author mayanjun(9/19/16)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Component
public @interface Handler {

	/**
	 * The value may indicate a suggestion for a logical component name,
	 * to be turned into a Spring bean in case of an autodetected component.
	 * @return the suggested component name, if any
	 */
	String value() default "";

}
