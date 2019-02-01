package org.mayanjun.myjack.idworker;

import org.mayanjun.myjack.IdWorker;

/**
 * StandaloneWorker
 *
 * @author mayanjun(5/1/16)
 */
public class StandaloneWorker implements IdWorker {

    private IdWorkerHandler handler;

    public StandaloneWorker(int ... indexes) {
        handler = new IdWorkerHandler(indexes);
    }

    public int getMaxIndex() {
        return IdWorkerHandler.MAX_WORKER_INDEX;
    }

    @Override
    public Long next() {
        return this.handler.nextId();
    }

}
