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
	 * @return the type for discovery of beans
	 */
	Class<C>[] getAutoDiscoveryTypes();

	/**
	 * Return the packages which is the found handlers in.
	 * if no packages specified, this option will be ignored
	 * @return the packages which is the found handlers in.
	 */
	String[] getPackages();

	/**
	 * Return supports handler
	 * @param source source
	 * @return null if no handler found
	 */
	T getHandler(Object source);

	/**
	 * Set handlers
	 * @param handlers handlers
	 * @return self
	 */
	AutoDiscoveryProcessor<T, C> setHandlers(T... handlers);

	/**
	 * Discover all matched handlers automatically
	 * @return all matched handlers
	 */
	List<T> discoverHandlers();

}
