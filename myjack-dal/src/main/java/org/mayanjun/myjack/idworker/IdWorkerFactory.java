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

package org.mayanjun.myjack.idworker;

import org.mayanjun.myjack.IdWorker;

/**
 * IdWorkerBuilder
 *
 * @author mayanjun(5/1/16)
 */
public class IdWorkerFactory {

    public static final long EPOCH = 1457258545962L;

    public static final int MIN_HANDLER_ID = IdWorkerHandler.MIN_WORKER_INDEX;

    public static final int MAX_HANDLER_ID = IdWorkerHandler.MAX_WORKER_INDEX;

    public static IdWorker create(int ... indexes) {
        return new StandaloneWorker(indexes);
    }
}
