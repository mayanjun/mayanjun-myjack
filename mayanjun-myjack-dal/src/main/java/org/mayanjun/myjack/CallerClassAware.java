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

package org.mayanjun.myjack;

/**
 * Interface to be implemented by beans that wish to be aware of their caller class
 *
 * @author mayanjun(7/15/16)
 */
public interface CallerClassAware {

    /**
     * Callback that supplies the caller class to a bean instance.
     * @param caller caller
     */
    void setCaller(Class<?> caller);

}
