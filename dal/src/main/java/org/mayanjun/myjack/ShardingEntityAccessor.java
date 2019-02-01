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

package org.mayanjun.myjack;

import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.query.Query;

import java.util.List;

/**
 * ShardingEntityAccessor
 *
 * @author mayanjun(6/30/16)
 */
public interface ShardingEntityAccessor {

    <T extends PersistableEntity> List<T> query(Query<T> query, Sharding sharding);

    <T extends PersistableEntity> T queryOne(Query<T> query, Sharding sharding);

    int update(PersistableEntity bean, Sharding sharding);
    int update(PersistableEntity bean, Sharding sharding, Query<? extends PersistableEntity> query);

    long save(PersistableEntity bean, Sharding sharding);

    long save(PersistableEntity bean, Sharding sharding, boolean isAutoIncrementId);

    /**
     * 保存一个对象，如果发生 DuplicatedException 则会将操作转化为更新操作
     * @param bean
     * @param sharding
     * @return
     */
    long saveOrUpdate(PersistableEntity bean, Sharding sharding);

    long saveOrUpdate(PersistableEntity bean, Sharding sharding, boolean isAutoIncrementId);

    int delete(PersistableEntity bean, Sharding sharding);
    int delete(Query<? extends PersistableEntity> query, Sharding sharding);
    <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, String... excludeFields);
    <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding, String... includeFields);

    <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... excludeFields);
    <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... includeFields);

    long count(Query<?> query, Sharding sharding);
}
