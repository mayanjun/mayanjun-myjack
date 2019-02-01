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
