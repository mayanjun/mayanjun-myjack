package org.mayanjun.myjack.handler;

import java.util.List;

/**
 * AutoDiscoveryProcessor
 * @param <T> the handler type
 * @param <C> the auto discovery type
 *
 * @author mayanjun
 */
public interface AutoDiscoveryProcessor<T extends EntityAccessHandler, C> {

	/**
	 * Returns the type for discovery of beans
	 * @return
	 */
	Class<C>[] getAutoDiscoveryTypes();

	/**
	 * Return the packages which is the found handlers in.
	 * if no packages specified, this option will be ignored
	 * @return
	 */
	String[] getPackages();

	/**
	 * Return supports handler
	 * @param source
	 * @return null if no handler found
	 */
	T getHandler(Object source);

	/**
	 * Set handlers
	 * @param handlers
	 * @return
	 */
	AutoDiscoveryProcessor<T, C> setHandlers(T... handlers);

	/**
	 * Discover all matched handlers automatically
	 * @return all matched handlers
	 */
	List<T> discoverHandlers();

}
