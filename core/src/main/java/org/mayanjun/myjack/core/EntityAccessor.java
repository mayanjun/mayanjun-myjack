package org.mayanjun.myjack.core;

import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.query.Query;

import java.util.List;

/**
 * Standard EntityAccessor
 *
 * @author mayanjun(2/26/16)
 * @since 0.0.5
 */
public interface EntityAccessor {

    /**
     * Query entities
     * @param query
     * @param <T>
     * @return
     */
    <T extends PersistableEntity> List<T> query(Query<T> query);

    /**
     * Returns only one entity
     * @param query
     * @param <T>
     * @return
     */
    <T extends PersistableEntity> T queryOne(Query<T> query);

    /**
     * Update a bean. All of Non-Null fields will be updated
     * @param bean
     * @return
     */
    int update(PersistableEntity bean);

    /**
     * Update beans with query
     * @param bean
     * @param query
     * @return
     */
    int update(PersistableEntity bean, Query<? extends PersistableEntity> query);

    /**
     * Save a bean
     * @param bean
     * @param isAutoIncrementId true if used an auto-increment id
     * @return
     */
    long save(PersistableEntity bean, boolean isAutoIncrementId);

    /**
     * Save a bean
     * @param bean
     * @return
     */
    long save(PersistableEntity bean);

    /**
     * Save or update a bean
     * @param bean
     * @return
     */
    long saveOrUpdate(PersistableEntity bean);

    /**
     * Save or update bean
     * @param bean
     * @param isAutoIncrementId
     * @return
     */
    long saveOrUpdate(PersistableEntity bean, boolean isAutoIncrementId);

    /**
     * Delete a bean
     * @param bean
     * @return
     */
    int delete(PersistableEntity bean);

    /**
     * Delete beans with a query
     * @param query
     * @return
     */
    int delete(Query<? extends PersistableEntity> query);

    /**
     * Get a bean that the specified fields is excluded
     * @param bean
     * @param excludeFields
     * @param <T>
     * @return
     */
    <T extends PersistableEntity> T getExclude(PersistableEntity bean, String... excludeFields);

    /**
     * Get a bean that only the specified fields is included
     * @param bean
     * @param includeFields
     * @param <T>
     * @return
     */
	<T extends PersistableEntity> T getInclude(PersistableEntity bean, String... includeFields);

    /**
     * Get a bean that the specified fields is excluded
     * @param bean
     * @param forUpdate pessimistic lock
     * @param excludeFields
     * @param <T>
     * @return
     */
    <T extends PersistableEntity> T getExclude(PersistableEntity bean, boolean forUpdate, String... excludeFields);

    /**
     * Get a bean that only the specified fields is included
     * @param bean
     * @param forUpdate pessimistic lock
     * @param includeFields
     * @param <T>
     * @return
     */
    <T extends PersistableEntity> T getInclude(PersistableEntity bean, boolean forUpdate, String... includeFields);

    /**
     * Count rows
     * @param query
     * @return
     */
    long count(Query<?> query);
}
