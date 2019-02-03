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

package org.mayanjun.myjack.mybatis;

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
