package org.mayanjun.myjack.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DistributedLock
 *
 * @author mayanjun(7/21/16)
 */
public class DistributedLock extends ZooKeeperBase implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);

    public static final String LOCK_NODE_NAME_PREFIX = "lock";

    private ZkClient zkClient;

    private ReentrantLock lock = new ReentrantLock();
    private Condition invalid = lock.newCondition();

    private Timer exceptionSignalTimer = new Timer(true);
    private volatile boolean exceptionSignalTimerFired;
    private volatile boolean exceptionBlocked = true;

    private String lockNode;
    /**
     * 锁名称, 不同的业务可以使用不同的锁名称
     */
    private String name;

    private String lockRootNode;
    private String lockNodePath;

    private boolean lockAcquired;

    /**
     * 请求锁异常时,多少时间后重试
     */
    private long retryPeriodOnException = 10000;

    private IZkDataListener nodeListener = new IZkDataListener() {
        @Override
        public void handleDataChange(String dataPath, Object data) throws Exception {
            // nothing to do
        }

        /**
         * Signal to blocking thread that acquiring lock to reacquire again
         * @param dataPath
         * @throws Exception
         */
        @Override
        public void handleDataDeleted(String dataPath) throws Exception {
            signalToInvalidThread("Signal for acquired fail sent success");
        }
    };

    @Required
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Required
    public void setName(String name) {
        Assert.hasText(name, "Lock name can not be empty");
        this.name = name;
    }

    /**
     * 尝试获得锁, 如果获得锁失败, 线程会阻塞
     * @throws InterruptedException 如果跑出异常则获取锁失败, 需要重新获取
     */
    public void lock() throws InterruptedException {
        try {
            lock.lockInterruptibly();
            for(;;) { // 自旋锁
                try {
                    deleteGhostNode();
                    if(this.lockNode == null) {
                        // 创建临时节点
                        lockNode = zkClient.createEphemeralSequential(lockNodePath, null);
                        LOG.info("Sequential lock node created: lockNode={}", lockNode);
                    }

                    List<String> children = zkClient.getChildren(this.lockRootNode);
                    Collections.sort(children); // important
                    String nodeToSearch = lockNode.substring(lockNode.lastIndexOf("/") + 1);
                    int index = Collections.binarySearch(children, nodeToSearch);
                    if(index > 0) { // 不是最小节点
                        String smallerNode = children.get(index - 1);
                        String nodeToMonitor = this.lockRootNode + "/" + smallerNode;
                        zkClient.subscribeDataChanges(nodeToMonitor, nodeListener);
                        LOG.info("Acquire lock fail, monitoring smaller node:{}", smallerNode);
                        invalid.await();
                    } else {
                        break;
                    }
                } catch (ZkNoNodeException e) {
                    Throwable throwable = e.getCause();
                    if(throwable instanceof KeeperException.NoNodeException) {
                        zkClient.createPersistent(this.lockRootNode, true);
                    }
                    exceptionAwait(e);
                } catch (Throwable e) {
                    exceptionAwait(e);
                }
            } // end for

            lockAcquired = true;
            LOG.info("Acquire lock success: lock={}, node={}", name, this.lockNode);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 有时候会出现lock节点导致所有节点无法获取锁, 必须删除
     */
    private void deleteGhostNode() {
        try {
            zkClient.delete(lockNodePath);
        } catch (Throwable e) {}
    }

    private void exceptionAwait(Throwable e) throws InterruptedException {
        LOG.info("Acquired fail:("+ e.getClass().getSimpleName() +")" + e.getMessage());

        /**
         * 如果发生异常阻塞需要另行干预
         * 发生异常的阻塞需要额外的唤醒
         */
        exceptionBlocked = true;
        if(!exceptionSignalTimerFired) {
            exceptionSignalTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    /**
                     * Signal to blocking thread that got exception when acquiring lock to reacquire again
                     */
                    if(exceptionBlocked) signalToInvalidThread("Signal for exception blocking sent success");
                }
            }, retryPeriodOnException, retryPeriodOnException);
            exceptionSignalTimerFired = true;
        }
        invalid.await();
        LOG.info("Exception lock signal received");
    }

    /**
     * 解锁操作
     */
    public void unlock() {
        if(lockAcquired) {
            // 此时已经放弃了自己持有的锁
            zkClient.delete(lockNode);
            this.lockNode = null;
            lockAcquired = false;
        }
    }

    private void preCreateNode() throws Exception {
        boolean exists = zkClient.exists(this.lockRootNode);
        if(!exists) {
            zkClient.createPersistent(this.lockRootNode, true);
        }
    }

    private void signalToInvalidThread(String log) {
        try {
            lock.lock();
            this.invalid.signal();
            this.exceptionBlocked = false;
            LOG.info(log);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 请求锁异常时,多少时间后重试
     * @param retryPeriodOnException 重试超时时间, 毫秒
     */
    public void setRetryPeriodOnException(long retryPeriodOnException) {
        this.retryPeriodOnException = retryPeriodOnException;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.zkClient.close();
    }

    @PreDestroy
    void destroy() throws Exception {
        this.zkClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.lockRootNode = clearNodePath(this.namespace + "/" + name);
        this.lockNodePath = clearNodePath(lockRootNode + "/" + LOCK_NODE_NAME_PREFIX);
        LOG.info("Zookeeper Node: lockRootNode={}, lockNodePath={}", lockRootNode, lockNodePath);
		preCreateNode();
    }
}