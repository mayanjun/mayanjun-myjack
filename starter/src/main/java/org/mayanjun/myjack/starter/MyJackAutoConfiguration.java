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

package org.mayanjun.myjack.starter;

import com.zaxxer.hikari.HikariDataSource;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.mayanjun.myjack.IdWorker;
import org.mayanjun.myjack.core.Assert;
import org.mayanjun.myjack.idworker.StandaloneWorker;
import org.mayanjun.myjack.mybatis.DatabaseRouter;
import org.mayanjun.myjack.mybatis.DatabaseSession;
import org.mayanjun.myjack.mybatis.ThreadLocalDatabaseRouter;
import org.mayanjun.myjack.mybatis.BasicDAO;
import org.mayanjun.myjack.support.PropertiesFactoryBean;
import org.mayanjun.myjack.zookeeper.ZooKeeperBasedIdWorker;
import org.mayanjun.myjack.zookeeper.ZooKeeperClientFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@EnableConfigurationProperties(MyJackConfig.class)
@ConditionalOnClass(BasicDAO.class)
@AutoConfigureAfter({MybatisAutoConfiguration.class})
public class MyJackAutoConfiguration implements ApplicationRunner, ResourceLoaderAware {

    private static final Logger LOG = LoggerFactory.getLogger(MyJackAutoConfiguration.class);

    @Autowired
    private MyJackConfig config;

    private ResourceLoader resourceLoader;

    private List<SqlSessionTemplate> sqlSessionTemplates = new ArrayList<SqlSessionTemplate>();

    @Bean
    @ConditionalOnMissingBean(IdWorker.class)
    public IdWorker idWorker() throws Exception {
        LOG.info("Starting auto config IdWorker...");
        String type = config.getIdWorkerType();
        if (StandaloneWorker.class.getName().equals(type)) {
            LOG.info("Using StandaloneWorker...");
            return new StandaloneWorker();
        } else if (ZooKeeperBasedIdWorker.class.getName().equals(type)) {
            LOG.info("Using ZooKeeperBasedIdWorker...");
            String namespace = config.getZkNamespace();
            if (StringUtils.isBlank(namespace) || !namespace.startsWith("/")) namespace = "/apps/myjack/idconfig";

            ZkClient client = createZkClient();
            ZooKeeperBasedIdWorker idWorker = new ZooKeeperBasedIdWorker();
            idWorker.setZkClient(client);
            idWorker.setNamespace(namespace);
            idWorker.afterPropertiesSet();
            return idWorker;
        }

        LOG.info("Using default NULL id-worker");
        return new IdWorker() {
            @Override
            public Long next() {
                return null;
            }
        };
    }

    private ZkClient createZkClient() throws Exception {
        Assert.notBlank(config.getZkHosts(), "zkHosts not be empty: for initializing ZooKeeperBasedIdWorker");
        ZooKeeperClientFactory factory = new ZooKeeperClientFactory(config.getZkHosts());
        factory.init();
        return factory.getObject();
    }

    @Bean
    @ConditionalOnMissingBean(DatabaseRouter.class)
    public DatabaseRouter dataBaseRouter() throws Exception {
        if (config.getDatasourceConfigs() == null || config.getDatasourceConfigs().isEmpty()) {
            List<MyJackConfig.DataSourceConfig> datasourceConfigs = new ArrayList<MyJackConfig.DataSourceConfig>();
            datasourceConfigs.add(new MyJackConfig.DataSourceConfig());
            config.setDatasourceConfigs(datasourceConfigs);
        }
        Assert.notEmpty(config.getDatasourceConfigs(), "Datasource config(s) can not be empty");

        ThreadLocalDatabaseRouter databaseRouter = new ThreadLocalDatabaseRouter();

        for (MyJackConfig.DataSourceConfig config : config.getDatasourceConfigs()) {
            DataSource dataSource = createDataSource(config);
            SqlSessionFactoryBean sqlSessionFactoryBean = createSqlSessionFactoryBean(dataSource, config);

            // create sql session template
            SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
            sqlSessionTemplates.add(template);

            // create transactionTemplate
            DataSourceTransactionManager manager = new DataSourceTransactionManager();
            manager.setDataSource(dataSource);
            manager.afterPropertiesSet();

            TransactionTemplate transactionTemplate = new TransactionTemplate();
            transactionTemplate.setIsolationLevelName(config.getIsolationLevelName());
            transactionTemplate.setPropagationBehaviorName(config.getPropagationBehaviorName());
            transactionTemplate.setTransactionManager(manager);
            transactionTemplate.afterPropertiesSet();

            String databaseSessionName = config.getName();
            Assert.notBlank(databaseSessionName, "The database session name can not be empty");
            databaseRouter.addDatabaseSession(new DatabaseSession(config.getName(), template, transactionTemplate));

        }
        databaseRouter.afterPropertiesSet();
        return databaseRouter;
    }

    private SqlSessionFactoryBean createSqlSessionFactoryBean(DataSource dataSource, MyJackConfig.DataSourceConfig config) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        if (StringUtils.isNotBlank(config.getMybatisConfigLocation())) {
            sqlSessionFactoryBean.setConfigLocation(resourceLoader.getResource(config.getMybatisConfigLocation()));
        }
        sqlSessionFactoryBean.afterPropertiesSet();
        return sqlSessionFactoryBean;
    }

    private DataSource createDataSource(MyJackConfig.DataSourceConfig config) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setJdbcUrl(config.getJdbcUrl());
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setAutoCommit(config.isAutoCommit());
        dataSource.setMinimumIdle(config.getMinimumIdle());
        dataSource.setMaximumPoolSize(config.getMaximumPoolSize());
        dataSource.setConnectionInitSql(config.getValidationQuery());

        // set connection properties
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setConnectionProperties(config.getConnectionProperties());
        dataSource.setDataSourceProperties(propertiesFactoryBean.getObject());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(BasicDAO.class)
    public BasicDAO basicDAO() throws Exception {
        BasicDAO dao = new BasicDAO();
        dao.setIdWorker(idWorker());
        dao.setRouter(dataBaseRouter());
        return dao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (sqlSessionTemplates != null && !sqlSessionTemplates.isEmpty()) {
            for (SqlSessionTemplate template : sqlSessionTemplates) {
                template.getConfiguration().getMappedStatements();
            }
        }
        LOG.info("All mapped statement build successfully");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
