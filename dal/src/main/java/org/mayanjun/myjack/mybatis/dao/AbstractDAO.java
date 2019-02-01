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

package org.mayanjun.myjack.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.mayanjun.myjack.IdWorker;
import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.ShardingEntityAccessor;
import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.query.Query;
import org.mayanjun.myjack.mybatis.DatabaseRouter;
import org.mayanjun.myjack.mybatis.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AbstractDAO
 *
 * @author mayanjun(7/5/16)
 */
public abstract class AbstractDAO implements DataBaseRouteDAO, ShardingEntityAccessor {

    @Autowired
    private BasicDAO basicDAO;

    public IdWorker getIdWorker() {
        return basicDAO.getIdWorker();
    }

    public DynamicMapper<PersistableEntity> getMapper(Class<?> beanType, SqlSession sqlSession) {
        return basicDAO.getMapper(beanType, sqlSession);
    }

    @Override
    public int delete(PersistableEntity bean) {
        return basicDAO.delete(bean);
    }

    @Override
    public int delete(PersistableEntity bean, Sharding sharding) {
        return basicDAO.delete(bean, sharding);
    }

    @Override
    public int delete(Query<? extends PersistableEntity> query) {
        return basicDAO.delete(query);
    }

    @Override
    public int delete(Query<? extends PersistableEntity> query, Sharding sharding) {
        return basicDAO.delete(query, sharding);
    }

    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, String... excludeFields) {
        return basicDAO.getExclude(bean, excludeFields);
    }

    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, String... excludeFields) {
        return basicDAO.getExclude(bean, sharding, excludeFields);
    }

    @Override
    public DatabaseRouter getDataBaseRouter() {
        return basicDAO.getDataBaseRouter();
    }

    public DatabaseRouter getRouter() {
        return basicDAO.getRouter();
    }

    @Override
    public <T extends PersistableEntity> List<T> query(Query<T> query) {
        return basicDAO.query(query);
    }

    @Override
    public <T extends PersistableEntity> List<T> query(Query<T> query, Sharding sharding) {
        return basicDAO.query(query, sharding);
    }

    @Override
    public long save(PersistableEntity bean) {
        return basicDAO.save(bean);
    }

    @Override
    public long save(PersistableEntity bean, Sharding sharding) {
        return basicDAO.save(bean, sharding);
    }

    @Override
    public int update(PersistableEntity bean) {
        return basicDAO.update(bean);
    }

    @Override
    public int update(PersistableEntity bean, Query<? extends PersistableEntity> query) {
        return basicDAO.update(bean, query);
    }

    @Override
    public int update(PersistableEntity bean, Sharding sharding) {
        return basicDAO.update(bean, sharding);
    }

    @Override
    public int update(PersistableEntity bean, Sharding sharding, Query<? extends PersistableEntity> query) {
        return basicDAO.update(bean, sharding, query);
    }
}
