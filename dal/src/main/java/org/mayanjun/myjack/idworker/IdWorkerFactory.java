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
