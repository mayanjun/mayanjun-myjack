package org.mayanjun.myjack.starter;

import org.mayanjun.myjack.idworker.StandaloneWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@SuppressWarnings("ALL")
@ConfigurationProperties(prefix = "myjack")
public class MyJackConfig {

    /**
     * IdWorker的实现类
     */
    private String idWorkerType = StandaloneWorker.class.getName();

    /**
     * ZooKeeper 主机列表，用来初始化ZK客户端
     */
    private String zkHosts = "127.0.0.1:2181";

    /**
     * ZooKeeper 应用名称空间
     */
    private String zkNamespace = "/apps/myjack";

    private List<DataSourceConfig> datasourceConfigs;

    public String getIdWorkerType() {
        return idWorkerType;
    }

    public void setIdWorkerType(String idWorkerType) {
        this.idWorkerType = idWorkerType;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public String getZkNamespace() {
        return zkNamespace;
    }

    public void setZkNamespace(String zkNamespace) {
        this.zkNamespace = zkNamespace;
    }

    public List<DataSourceConfig> getDatasourceConfigs() {
        return datasourceConfigs;
    }

    public void setDatasourceConfigs(List<DataSourceConfig> datasourceConfigs) {
        this.datasourceConfigs = datasourceConfigs;
    }

    public static class DataSourceConfig {
        private String name = "master";
        private String driverClassName = "com.mysql.jdbc.Driver";
        private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/myjack";
        private String username = "root";
        private String password = "123456";
        private boolean autoCommit = true;
        private String validationQuery = "select 1 from dual";
        private int minimumIdle = 0;
        private int maximumPoolSize = 50;
        private String mybatisConfigLocation = "classpath:default-mybatis-config.xml";
        private String isolationLevelName = "ISOLATION_READ_COMMITTED";
        private int transactionTimeout = 3000;
        private String propagationBehaviorName = "PROPAGATION_REQUIRED";
        private String connectionProperties = "createDatabaseIfNotExist=true;characterEncoding=utf-8;useUnicode=true;allowMultiQueries=true;zeroDateTimeBehavior=convertToNull";

        public int getTransactionTimeout() {
            return transactionTimeout;
        }

        public void setTransactionTimeout(int transactionTimeout) {
            this.transactionTimeout = transactionTimeout;
        }

        public String getIsolationLevelName() {
            return isolationLevelName;
        }

        public void setIsolationLevelName(String isolationLevelName) {
            this.isolationLevelName = isolationLevelName;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isAutoCommit() {
            return autoCommit;
        }

        public void setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public String getConnectionProperties() {
            return connectionProperties;
        }

        public void setConnectionProperties(String connectionProperties) {
            this.connectionProperties = connectionProperties;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(int minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public String getMybatisConfigLocation() {
            return mybatisConfigLocation;
        }

        public void setMybatisConfigLocation(String mybatisConfigLocation) {
            this.mybatisConfigLocation = mybatisConfigLocation;
        }

        public String getPropagationBehaviorName() {
            return propagationBehaviorName;
        }

        public void setPropagationBehaviorName(String propagationBehaviorName) {
            this.propagationBehaviorName = propagationBehaviorName;
        }
    }
}