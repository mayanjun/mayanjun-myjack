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
