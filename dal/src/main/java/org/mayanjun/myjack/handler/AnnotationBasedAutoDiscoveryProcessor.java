package org.mayanjun.myjack.handler;

import org.mayanjun.myjack.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AnnotationBasedAutoDiscoveryProcessor
 *
 * @author mayanjun(9/20/16)
 */
public abstract class AnnotationBasedAutoDiscoveryProcessor<T extends EntityAccessHandler, C extends Annotation> extends SpringAutoDiscoveryProcessor<T, C> {

	private static final Logger LOG = LoggerFactory.getLogger(AnnotationBasedAutoDiscoveryProcessor.class);

	@Override
	protected List<T> findHandlers(Class<C> type) {
		Set<String> ps = packageSet();
		List<T> handlers = new ArrayList<T>();

		Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(type);

		if(beans != null && !beans.isEmpty()) {
			Iterator<Map.Entry<String, Object>> iterator = beans.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				Object bean = entry.getValue();
				if(ps != null) {
					String pkg = bean.getClass().getPackage().getName();
					if(!ps.contains(pkg)) {
						LOG.info("Handler is not in the specified packages so that it will be ignored:this={} beanId={}, handler={}", this.getClass().getSimpleName(), entry.getKey(),  bean.getClass().getSimpleName());
						continue;
					}
				}
				T handler;
				try {
					handler = (T) bean;
					Class<?> realHandlerType = ClassUtils.getFirstParameterizedType(this.getClass());
					if(realHandlerType.isAssignableFrom(handler.getClass())) {
						handlers.add(handler);
						LOG.info("Handler found: this={}, beanId={}, handler={}", this.getClass().getSimpleName(), entry.getKey(), bean.getClass().getSimpleName());
					} else {
						LOG.warn("Handler type mismatch: this={}, beanId={}, handler={}", this.getClass().getSimpleName(), entry.getKey(), bean.getClass().getSimpleName());
					}
				} catch (ClassCastException e) {
					LOG.warn("Handler type mismatch and this handler({}) will be discarded: {}", bean.getClass().getSimpleName(), e.getMessage());
				}
			}
		}
		return handlers;
	}
}
