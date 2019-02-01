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