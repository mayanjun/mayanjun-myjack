package org.mayanjun.myjack.handler;

import java.lang.annotation.Annotation;

/**
 * HandlerAutoDiscoveryProcessor
 *
 * @author mayanjun(9/20/16)
 */
public class HandlerAutoDiscoveryProcessor<T extends EntityAccessHandler, C extends Annotation> extends AnnotationBasedAutoDiscoveryProcessor<T, C> {

	@Override
	public String[] getPackages() {
		return null;
	}

	@Override
	public Class<C>[] getAutoDiscoveryTypes() {
		return new Class[]{Handler.class};
	}
}