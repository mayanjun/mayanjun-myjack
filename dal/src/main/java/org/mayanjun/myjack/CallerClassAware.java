package org.mayanjun.myjack;

/**
 * Interface to be implemented by beans that wish to be aware of their caller class
 *
 * @author mayanjun(7/15/16)
 */
public interface CallerClassAware {

    /**
     * Callback that supplies the caller class to a bean instance.
     * @param caller
     */
    void setCaller(Class<?> caller);

}
