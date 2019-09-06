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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SpringAutoDiscoveryProcessor
 *
 * @author mayanjun(9/20/16)
 */
public abstract class SpringAutoDiscoveryProcessor<T extends EntityAccessHandler, C> implements AutoDiscoveryProcessor<T, C>, InitializingBean, ApplicationContextAware {

	protected ApplicationContext applicationContext;

	private List<T> handlers = new CopyOnWriteArrayList<T>();

	private volatile boolean handlersDiscovered;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<T> hs = discoverHandlers();
		if(!CollectionUtils.isEmpty(hs)) {
			this.handlers.addAll(hs);
		}
	}

	protected abstract List<T> findHandlers(Class<C> type);

	@Override
	public List<T> discoverHandlers() {
		synchronized (this) {
			List<T> handlers = new ArrayList<T>();
			if(handlersDiscovered) return handlers;

			Class<C>[] types = getAutoDiscoveryTypes();
			if(types != null && types.length > 0) {
				for(Class<C> type : types) {
					List<T> hs = findHandlers(type);
					if(!CollectionUtils.isEmpty(hs)) handlers.addAll(hs);
				}
			}
			if(!handlers.isEmpty()) {
				Collections.sort(handlers, new OrderComparator());
			}
			handlersDiscovered = true;
			return handlers;
		}
	}

	@Override
	public T getHandler(Object source) {
		for(T handler : handlers) if(handler.supports(source)) return handler;
		return null;
	}

	@Override
	public AutoDiscoveryProcessor<T, C> setHandlers(T... handlers) {
		if(handlers != null && handlers.length > 0) {
			this.handlers.addAll(Arrays.asList(handlers));
		}
		return this;
	}

	public List<T> getHandlers() {
		return handlers;
	}

	protected Set<String> packageSet() {
		String[] packages = getPackages();
		Set<String> ps = null;
		if(packages != null && packages.length > 0) {
			ps = new TreeSet<String>(Arrays.asList(packages));
		}
		return ps;
	}
}
