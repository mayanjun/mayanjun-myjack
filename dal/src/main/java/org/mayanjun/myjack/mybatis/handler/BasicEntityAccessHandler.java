package org.mayanjun.myjack.mybatis.handler;

import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.ShardingEntityAccessor;
import org.mayanjun.myjack.ThreadLocalCallerClassAware;
import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.mayanjun.myjack.core.query.Query;
import org.mayanjun.myjack.handler.EntityAccessHandler;
import org.mayanjun.myjack.mybatis.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * BasicEntityHandler
 *
 * @author mayanjun
 */
public abstract class BasicEntityAccessHandler extends ThreadLocalCallerClassAware
        implements EntityAccessHandler, ShardingEntityAccessor {

	@Override
	public long count(Query<?> query, Sharding sharding) {
		return dao.count(query, sharding);
	}

	@Override
	public long count(Query<?> query) {
		return dao.count(query);
	}

	@Autowired
	private BasicDAO dao;

	@Override
	public int delete(PersistableEntity bean) {
		return dao.delete(bean);
	}

	@Override
	public int delete(PersistableEntity bean, Sharding sharding) {
		return dao.delete(bean, sharding);
	}

	@Override
	public int delete(Query<? extends PersistableEntity> query) {
		return dao.delete(query);
	}

	@Override
	public int delete(Query<? extends PersistableEntity> query, Sharding sharding) {
		return dao.delete(query, sharding);
	}

	@Override
	public <T extends PersistableEntity> T getExclude(PersistableEntity bean, String... excludeFields) {
		return dao.getExclude(bean, excludeFields);
	}

	@Override
	public <T extends PersistableEntity> T getInclude(PersistableEntity bean, String... includeFields) {
		return dao.getInclude(bean, includeFields);
	}

	@Override
	public <T extends PersistableEntity> T getExclude(PersistableEntity bean, boolean forUpdate, String... excludeFields) {
		return dao.getExclude(bean, forUpdate, excludeFields);
	}

	@Override
	public <T extends PersistableEntity> T getInclude(PersistableEntity bean, boolean forUpdate, String... includeFields) {
		return dao.getInclude(bean, forUpdate, includeFields);
	}

	@Override
	public <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding,
													  String... includeFields) {
		return dao.getInclude(bean, sharding, includeFields);
	}

	@Override
	public <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... excludeFields) {
		return dao.getExclude(bean, sharding, forUpdate, excludeFields);
	}

	@Override
	public <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... includeFields) {
		return dao.getInclude(bean, sharding, forUpdate, includeFields);
	}

	@Override
	public <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, String... excludeFields) {
		return dao.getExclude(bean, sharding, excludeFields);
	}

	@Override
	public <T extends PersistableEntity> List<T> query(Query<T> query) {
		return dao.query(query);
	}

	@Override
	public <T extends PersistableEntity> T queryOne(Query<T> query) {
		return dao.queryOne(query);
	}

	@Override
	public <T extends PersistableEntity> List<T> query(Query<T> query, Sharding sharding) {
		return dao.query(query, sharding);
	}

	@Override
	public <T extends PersistableEntity> T queryOne(Query<T> query, Sharding sharding) {
		return dao.queryOne(query, sharding);
	}

	@Override
	public long save(PersistableEntity bean) {
		return dao.save(bean);
	}

	@Override
	public long save(PersistableEntity bean, Sharding sharding) {
		return dao.save(bean, sharding);
	}

	@Override
	public int update(PersistableEntity bean) {
		return dao.update(bean);
	}

	@Override
	public int update(PersistableEntity bean, Query<? extends PersistableEntity> query) {
		return dao.update(bean, query);
	}

	@Override
	public int update(PersistableEntity bean, Sharding sharding) {
		return dao.update(bean, sharding);
	}

	@Override
	public int update(PersistableEntity bean, Sharding sharding, Query<? extends PersistableEntity> query) {
		return dao.update(bean, sharding, query);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public long save(PersistableEntity bean, boolean isAutoIncrementId) {
		return dao.save(bean, isAutoIncrementId);
	}

	@Override
	public long saveOrUpdate(PersistableEntity bean) {
		return dao.saveOrUpdate(bean);
	}

	@Override
	public long saveOrUpdate(PersistableEntity bean, boolean isAutoIncrementId) {
		return dao.saveOrUpdate(bean, isAutoIncrementId);
	}

	@Override
	public long save(PersistableEntity bean, Sharding sharding, boolean isAutoIncrementId) {
		return dao.save(bean, sharding, isAutoIncrementId);
	}

	@Override
	public long saveOrUpdate(PersistableEntity bean, Sharding sharding) {
		return dao.saveOrUpdate(bean, sharding);
	}

	@Override
	public long saveOrUpdate(PersistableEntity bean, Sharding sharding, boolean isAutoIncrementId) {
		return dao.saveOrUpdate(bean, sharding, isAutoIncrementId);
	}
}