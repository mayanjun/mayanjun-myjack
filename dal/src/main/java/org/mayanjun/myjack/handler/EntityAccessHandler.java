package org.mayanjun.myjack.handler;


import org.mayanjun.myjack.CallerClassAware;
import org.mayanjun.myjack.core.EntityAccessor;
import org.springframework.core.Ordered;

/**
 * EntityAccessHandler
 *
 * @author mayanjun(26/09/2016)
 */
public interface EntityAccessHandler extends CallerClassAware, EntityAccessor, Ordered {

    boolean supports(Object source);
}
