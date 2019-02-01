package org.mayanjun.myjack.mybatis.handler;

import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * GenericEntityAccessHandler
 *
 * @author mayanjun(24/10/2016)
 */
public abstract class GenericEntityAccessHandler<T extends PersistableEntity> extends BasicEntityAccessHandler {

	private Class beanType = null;

	private Class<T> getHandlerType() {
		if(this.beanType != null) return beanType;

		Class cls = this.getClass();
		Type t = cls.getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;

		Type ats[] = pt.getActualTypeArguments();
		Class<T> beanType = (Class<T>) ats[0];
		this.beanType = beanType;
		return beanType;
	}

	@Override
	public boolean supports(Object source) {
		Class ht = getHandlerType();
		if(ht.isAssignableFrom(source.getClass())) return true;
		if(source instanceof Query) {
			Class c = ((Query) source).getBeanType();
			if(ht.isAssignableFrom(c)) return true;
		}
		return false;
	}
}
