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
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ZooKeeperClientFactory
 *
 * @author mayanjun(6/8/16)
 */
public class ZooKeeperClientFactory implements FactoryBean<ZkClient> {

    private static final int DEFAULT_SESSION_TIMEOUT = 30000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

    private String hosts;
    private int connectionTimeout = -1;
    private int sessionTimeout = -1;
    private ZkSerializer serializer;
    private List<ZooKeeperACL> acl;

    public ZooKeeperClientFactory() {
    }

    public ZooKeeperClientFactory(String hosts) {
        this.hosts = hosts;
    }

    public void setAcl(List<ZooKeeperACL> acl) {
        this.acl = acl;
    }

    public void setSerializer(ZkSerializer serializer) {
        this.serializer = serializer;
    }

    @PostConstruct
    public void init() {
        if (sessionTimeout < 0) this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
        if (connectionTimeout < 0) this.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        if (serializer == null) serializer = new JsonZkSerializer();
    }

    @Override
    public ZkClient getObject() throws Exception {
        ZkClient client = new ZkClient(hosts, sessionTimeout, connectionTimeout, serializer);
        if(!CollectionUtils.isEmpty(acl)) {
            for(ZooKeeperACL authInfo : acl) {
                client.addAuthInfo(authInfo.getScheme(), authInfo.getAuthBytes());
            }
        }
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return ZkClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
