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

package org.mayanjun.myjack.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * AssignableBasedAutodiscoveryHandler
 *
 * @author mayanjun(9/20/16)
 */
public abstract class AssignableBasedAutoDiscoveryProcessor<T extends EntityAccessHandler, C> extends SpringAutoDiscoveryProcessor<T, C> {

	private static final Logger LOG = LoggerFactory.getLogger(AssignableBasedAutoDiscoveryProcessor.class);

	@Override
	protected List<T> findHandlers(Class<C> type) {
		Set<String> ps = packageSet();
		List<T> handlers = new ArrayList<T>();
		Map<String, ?> beans = this.applicationContext.getBeansOfType(type);

		if(beans != null && !beans.isEmpty()) {

			Iterator<? extends Map.Entry<String, ?>> iterator = beans.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, ?> entry = iterator.next();
				Object bean = entry.getValue();
				if(ps != null) {
					String pkg = bean.getClass().getPackage().getName();
					if(!ps.contains(pkg)) {
						LOG.info("Handler is not in the specified packages so that it will be ignored: beanId={}, handler={}",entry.getKey(),  bean.getClass().getSimpleName());
						continue;
					}
				}
				handlers.add((T) bean);
				LOG.info("Handler found: beanId={}, handler={}", entry.getKey(), bean.getClass().getSimpleName());
			}
		}
		return handlers;
	}
}
