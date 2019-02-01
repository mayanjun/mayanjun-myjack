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

package org.mayanjun.myjack.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.mayanjun.myjack.IdWorker;
import org.mayanjun.myjack.idworker.IdWorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.net.InetAddress;

/**
 * ZooKeeperIdWorkerBroker
 *
 * @author mayanjun(6/28/16)
 */
public class ZooKeeperBasedIdWorker extends ZooKeeperBase implements IdWorker, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperBasedIdWorker.class);

    private ZkClient zkClient;

    private String indexesNode;

	private IdWorker worker;

	private static int WORKER_LENGTH = IdWorkerFactory.MAX_HANDLER_ID + 1;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.indexesNode = namespace + "/idworker/indexes";
        LOG.info("Using indexesNode:" + indexesNode);

        int index = acquireWorkerIndex();
        Assert.isTrue(index >= 0, "Worker index must be a positive integer, but " + index + " is returned");
        LOG.info("----- WorkerIndex acquired:{} -----", index);
        initWorker(index);
    }

    /**
     * NOTE: DO NOT use both PLAN A and PLAN B at the same time
     * @return
     */
    private int acquireWorkerIndex() {
        Assert.notNull(zkClient, "ZK client can not be initialized");
        if(zkClient != null) {
            if(!zkClient.exists(indexesNode)) {
                zkClient.createPersistent(indexesNode, true);
            }

            int mask = 0x0f;
            int count = 0;
            for(;;) {
                int index = count & mask;
                String path = indexesNode + "/index" + index;
                if(!zkClient.exists(path)) {
                    try {
                        zkClient.createEphemeral(path, InetAddress.getLocalHost().getHostName());
                        LOG.info("Acquire worker index from ZK success:{}", index);
                        return index;
                    } catch (Exception e) {
                        LOG.error("try creating index path fail: " + path, e);
                    }
                }
                if(count >= WORKER_LENGTH * 10) {
                    LOG.warn("Can not acquire a index from ZK, retry 10 times");
                    break;
                }
                count++;
            }
        }
        return -1;
    }

    @Required
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

	@Override
	public Long next() {
		return worker.next();
	}

	private void initWorker(int ...index) {
		worker = IdWorkerFactory.create(index);
	}
}