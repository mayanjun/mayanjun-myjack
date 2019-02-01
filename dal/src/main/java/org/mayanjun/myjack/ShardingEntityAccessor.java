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
