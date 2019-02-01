package org.mayanjun.myjack;

/**
 * AbstractCallerClassAware
 *
 * @author mayanjun(7/15/16)
 */
public class ThreadLocalCallerClassAware implements CallerClassAware {

    private ThreadLocal<Class<?>> classThreadLocal = new ThreadLocal<Class<?>>();

    @Override
    public void setCaller(Class<?> caller) {
        if(caller == null) classThreadLocal.remove();
        classThreadLocal.set(caller);
    }

    public Class<?> getCallerClass() {
        return this.classThreadLocal.get();
    }

}
