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

package org.mayanjun.myjack.dao;

import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSession;
import org.mayanjun.myjack.IdWorker;
import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.ShardingEntityAccessor;
import org.mayanjun.myjack.api.annotation.Index;
import org.mayanjun.myjack.api.annotation.IndexColumn;
import org.mayanjun.myjack.api.annotation.Table;
import org.mayanjun.myjack.api.entity.DeletableEntity;
import org.mayanjun.myjack.api.entity.PersistableEntity;
import org.mayanjun.myjack.api.enums.IndexType;
import org.mayanjun.myjack.api.enums.QueryDeletedMode;
import org.mayanjun.myjack.api.query.EquivalentComparator;
import org.mayanjun.myjack.api.query.LogicalOperator;
import org.mayanjun.myjack.api.query.Query;
import org.mayanjun.myjack.api.query.QueryBuilder;
import org.mayanjun.myjack.parser.PreparedQueryParser;
import org.mayanjun.myjack.parser.QueryParser;
import org.mayanjun.myjack.parser.SQLParameter;
import org.mayanjun.myjack.sharding.StaticSharding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A default implementation of {@link org.mayanjun.myjack.api.EntityAccessor}
 *
 * @author mayanjun(6/24/16)
 */
public class DynamicDAO implements DataBaseRouteAccessor, ShardingEntityAccessor, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDAO.class);

    private DatabaseRouter router;
    private IdWorker idWorker;
    private Map<Class<?>, Class<?>> entityMapperClassesCache = new IdentityHashMap<Class<?>, Class<?>>();

    private QueryParser parser = new PreparedQueryParser(DynamicMapper.PARAM_NAME);
    private Sharding defaultSharding = new DefaultSharding();

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(router, "router must be set");
    }

    public void setRouter(DatabaseRouter router) {
        this.router = router;
    }

    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Override
    public DatabaseRouter getDataBaseRouter() {
        return router;
    }


    @Override
    public <T extends PersistableEntity> List<T> query(Query<T> query) {
        return query(query, null);
    }

    @Override
    public <T extends PersistableEntity> T queryOne(Query<T> query) {
        return queryOne(query, defaultSharding);
    }


    @Override
    public int update(PersistableEntity bean) {
        return update(bean, defaultSharding);
    }

    @Override
    public int update(PersistableEntity bean, Query<? extends PersistableEntity> query) {
        return update(bean, defaultSharding, query);
    }

    @Override
    public long save(PersistableEntity bean, boolean isAutoIncrementId) {
        return save(bean, defaultSharding, isAutoIncrementId);
    }

    @Override
    public long save(PersistableEntity bean) {
        return save(bean, defaultSharding);
    }

    @Override
    public long saveOrUpdate(PersistableEntity bean) {
        return saveOrUpdate(bean, defaultSharding);
    }

    @Override
    public long saveOrUpdate(PersistableEntity bean, boolean isAutoIncrementId) {
        return saveOrUpdate(bean);
    }

    @Override
    public int delete(PersistableEntity bean) {
        return delete(bean, defaultSharding);
    }

    @Override
    public int delete(Query<? extends PersistableEntity> query) {
        return delete(query, defaultSharding);
    }


    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, String... excludeFields) {
        return getExclude(bean, null, excludeFields);
    }

    @Override
    public <T extends PersistableEntity> T getInclude(PersistableEntity bean, String... includeFields) {
        return getInclude(bean, null, includeFields);
    }

    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, boolean forUpdate, String... excludeFields) {
        return getExclude(bean, null, false, excludeFields);
    }

    @Override
    public <T extends PersistableEntity> T getInclude(PersistableEntity bean, boolean forUpdate, String... includeFields) {
        return getInclude(bean, null, false, includeFields);
    }


    @Override
    public <T extends PersistableEntity> List<T> query(Query<T> query, Sharding sharding) {
        setDeletableQuery(query);
        SQLParameter<T> parameter = parser.parse(query);
        SqlSession sqlSession = getSqlSession(sharding, parameter);
        DynamicMapper<T> mapper = (DynamicMapper<T>) getMapper(query.getBeanType(), sqlSession);
        return mapper.query(parameter, sharding);
    }

    @Override
    public long count(Query<?> query, Sharding sharding) {
        setDeletableQuery(query);
        SQLParameter<?> parameter = parser.parse(query);
        SqlSession sqlSession = getSqlSession(sharding, parameter);
        DynamicMapper<?> mapper = getMapper(query.getBeanType(), sqlSession);
        return mapper.count(parameter, sharding);
    }

    @Override
    public long count(Query<?> query) {
        return count(query, null);
    }

    @Override
    public <T extends PersistableEntity> T queryOne(Query<T> query, Sharding sharding) {
        //setDeletableQuery(query);
        if(query.getLimit() > 1) query.setLimit(1);
        List<T> list = query(query, sharding);
        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Set query
     * @param query
     */
    private  void setDeletableQuery(Query<?> query) {
        Class<?> beanType = query.getBeanType();
        if(DeletableEntity.class.isAssignableFrom(beanType)) {
            QueryDeletedMode mode = query.getQueryDeletedMode();
            if (mode == null) {
                query.addComparator(new EquivalentComparator("deleted", false, LogicalOperator.AND));
                return;
            }

            switch (mode) {
                case ONLY_DELETED:
                    query.addComparator(new EquivalentComparator("deleted", true, LogicalOperator.AND));
                    break;
                case WITHOUT_DELETED:
                    query.addComparator(new EquivalentComparator("deleted", false, LogicalOperator.AND));
                    break;
                default:
                    break;
            }
        }
    }

    private SqlSession getSqlSession(Sharding sharding, Object source) {
        //SqlSession sqlSession = getDataBaseRouter().getMasterDataBaseSqlSession(); // forced to use MASTER DB
        SqlSession sqlSession = getDataBaseRouter().getDatabaseSession(sharding, source).getSqlSession();
        Assert.notNull(sqlSession, "Can not get SqlSession");
        return sqlSession;
    }

    @Override
    public int update(PersistableEntity bean, Sharding sharding) {
        bean.setCreatedTime(null);
        bean.setModifiedTime(new Date());

        SqlSession sqlSession = getSqlSession(sharding, bean);

        DynamicMapper<PersistableEntity> mapper = getMapper(bean.getClass(), sqlSession);
        return mapper.update(bean, sharding);
    }

    @Override
    public int update(PersistableEntity bean, Sharding sharding, Query<? extends PersistableEntity> query) {
        bean.setCreatedTime(null);
        bean.setModifiedTime(new Date());

        SQLParameter<PersistableEntity> parameter = (SQLParameter<PersistableEntity>)parser.parse(query);
        parameter.setEntity(bean);
        SqlSession sqlSession = getSqlSession(sharding, bean);
        DynamicMapper<PersistableEntity> mapper = getMapper(bean.getClass(), sqlSession);
        return mapper.queryUpdate(parameter, sharding);
    }

    @Override
    public long save(PersistableEntity bean, Sharding sharding) {
        return save(bean, sharding, false);
    }

    @Override
    public long save(PersistableEntity bean, Sharding sharding, boolean isAutoIncrementId) {
        Date now = new Date();
        if(bean.getCreatedTime() == null) bean.setCreatedTime(now);
        if(bean.getModifiedTime() == null) bean.setModifiedTime(now);

        Long id = null;
        if (! isAutoIncrementId) {
            try {
                id = (Long) bean.getId();
            } catch (Exception e){}

            if(id == null) { // generate id
                if (this.idWorker != null) {
                    id = this.idWorker.next();
                }
            }
        }
        bean.setId(id);
        SqlSession sqlSession = getSqlSession(sharding, bean);
        DynamicMapper<PersistableEntity> mapper = getMapper(bean.getClass(), sqlSession);

        int ret = mapper.insert(bean, sharding);
        if(ret <= 0) return 0;

        Serializable dbid = bean.getId();
        if (dbid instanceof Number) {
            id = ((Number) dbid).longValue();
        } else {
            id = Long.valueOf(ret);
        }
        return id;
    }

    @Override
    public long saveOrUpdate(final PersistableEntity bean, final Sharding sharding) {
        return saveOrUpdate(bean, sharding, false);
    }

    @Override
    public long saveOrUpdate(final PersistableEntity bean, final Sharding sharding, final boolean isAutoIncrementId) {
        Serializable originId = bean.getId();
        try {
            return save(bean, sharding, isAutoIncrementId);
        } catch (DuplicateKeyException e) {
            try {
                bean.setId(originId);
                final Query<PersistableEntity> query = createUniqueQuery(bean);
                if (query == null) throw new IllegalArgumentException("Can not create query for unique-query: bean=" + bean);

                TransactionTemplate transactionTemplate = getDataBaseRouter().getDatabaseSession(sharding, bean).getTransaction();
                if (transactionTemplate != null) {
                    return transactionTemplate.execute(new TransactionCallback<Integer>() {
                        @Override
                        public Integer doInTransaction(TransactionStatus transactionStatus) {
                            query.setForUpdate(true);
                            return saveOrUpdateUpdate(query, bean, sharding);
                        }
                    });
                } else {
                    // UNSAFE 有可能会导致更新丢失问题
                    return saveOrUpdateUpdate(query, bean, sharding);
                }
            } catch (Exception e1) {
                throw new IllegalArgumentException(e1);
            }
        }
    }

    private int saveOrUpdateUpdate(Query<PersistableEntity> query, PersistableEntity bean, Sharding sharding) {
        PersistableEntity b = queryOne(query);
        if (b != null) {
            bean.setId(b.getId());
            return update(bean, sharding);
        } else {
            throw new IllegalArgumentException("Can not update bean for unique-query: bean=" + bean);
        }
    }


    public <T extends PersistableEntity> Query<T> createUniqueQuery(T bean) throws Exception {
        Class<T> c = (Class<T>)  bean.getClass();
        QueryBuilder<T> builder = QueryBuilder.custom(c);
        boolean valueSet = false;

        if (bean.getId() != null) {
            valueSet = true;
            builder.andEquivalent("id", bean.getId());
        }

        Table table = c.getAnnotation(Table.class);
        Index indexes[] = table.indexes();
        for (Index index : indexes) {
            IndexType type = index.type();
            if (type == IndexType.UNIQUE) {
                IndexColumn columns[] = index.columns();
                for (IndexColumn ic : columns) {
                    String name = ic.value();
                    Object value = BeanUtilsBean2.getInstance().getPropertyUtils().getProperty(bean, ic.value());
                    if (value != null) {
                        valueSet = true;
                        builder.andEquivalent(name, value);
                    }
                }
            }
        }
        if (valueSet) return builder.build();
        return null;
    }

    @Override
    public int delete(PersistableEntity bean, Sharding sharding) {
        SqlSession sqlSession = getSqlSession(sharding, bean);
        DynamicMapper<PersistableEntity> mapper = getMapper(bean.getClass(), sqlSession);
        if(bean instanceof DeletableEntity) {
            // logical delete
            try {
                Serializable id = bean.getId();
                DeletableEntity updateBean = (DeletableEntity)bean.getClass().getConstructor(id.getClass()).newInstance(id);
                updateBean.setDeleted(true);
                return mapper.update(updateBean, sharding);
            } catch (Exception e) {
                throw new RuntimeException("Can not create instance for class:" + bean.getClass(), e);
            }
        } else {
            return mapper.delete(bean, sharding);
        }
    }

    @Override
    public int delete(Query<? extends PersistableEntity> query, Sharding sharding) {
        Class<? extends PersistableEntity> beanType = query.getBeanType();
        if(DeletableEntity.class.isAssignableFrom(beanType)) { // process logical delete
            try {
                DeletableEntity updateBean = (DeletableEntity)beanType.newInstance();
                updateBean.setDeleted(true);
                return update(updateBean, query);
            } catch (Exception e) {
                throw new RuntimeException("Can not create instance for class:" + beanType, e);
            }
        } else {
            SQLParameter<PersistableEntity> parameter = (SQLParameter<PersistableEntity>)parser.parse(query);
            SqlSession sqlSession = getSqlSession(sharding, parameter);
            DynamicMapper<PersistableEntity> mapper = getMapper(beanType, sqlSession);
            return mapper.queryDelete(parameter, sharding);
        }

    }


    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, String... excludeFields) {
        return getExclude(bean, sharding, false, excludeFields);
    }

    @Override
    public <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding, String... includeFields) {
        return getInclude(bean, sharding, false, includeFields);
    }

    @Override
    public <T extends PersistableEntity> T getExclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... excludeFields) {
        Query<T> query = QueryBuilder.custom((Class<T>) bean.getClass()).andEquivalent("id", bean.getId()).excludeFields(excludeFields).limit(1).build();
        query.setForUpdate(forUpdate);
        return queryOneInternal(bean, query, sharding);
    }

    @Override
    public <T extends PersistableEntity> T getInclude(PersistableEntity bean, Sharding sharding, boolean forUpdate, String... includeFields) {
        Query<T> query = QueryBuilder.custom((Class<T>) bean.getClass()).andEquivalent("id", bean.getId()).includeFields(includeFields).limit(1).build();
        return queryOneInternal(bean, query, sharding);
    }

    private <T extends PersistableEntity> T queryOneInternal(PersistableEntity bean, Query<T> query, Sharding sharding) {
        StaticSharding staticSharding = null;
        if(sharding != null) staticSharding = new StaticSharding(sharding.getDatabaseName(bean), sharding.getTableName(bean));
        List<T> list = this.query(query, staticSharding);
        if (list != null && list.size() > 0) return list.get(0);
        return null;
    }


    public Class<?> createDynamicMapperClass(Class<?> beanType) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        ClassPath classPath = new ClassClassPath(this.getClass());
        pool.insertClassPath(classPath);
        CtClass superClass = pool.get(DynamicMapper.class.getName());
        CtClass ctClass = pool.makeInterface(beanType.getName() + "GeneratedMapper", superClass);

        SignatureAttribute.ClassSignature cs = new SignatureAttribute.ClassSignature(null, null,
                // Set interface and its generic params
                new SignatureAttribute.ClassType[]{new SignatureAttribute.ClassType(DynamicMapper.class.getName(),
                        new SignatureAttribute.TypeArgument[]{new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(beanType.getName()))}
                )});
        ctClass.setGenericSignature(cs.encode());

        ClassFile ccFile = ctClass.getClassFile();
        ConstPool constPool = ccFile.getConstPool();
        // add annotation
        AnnotationsAttribute bodyAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation mapperAnno = new Annotation(Mapper.class.getName(), constPool);
        bodyAttr.addAnnotation(mapperAnno);
        ccFile.addAttribute(bodyAttr);

        /*byte[] byteArr = ctClass.toBytecode();
        FileOutputStream fos = new FileOutputStream(new File("/Users/mayanjun/Desktop/StudentMapper.class"));
        fos.write(byteArr);
        fos.close();*/

        return ctClass.toClass();
    }


    public DynamicMapper<PersistableEntity> getMapper(Class<?> beanType, SqlSession sqlSession) {
        Class<?> mapperClass = entityMapperClassesCache.get(beanType);
        try {
            if (mapperClass == null) {
                synchronized (beanType) {
                    Class<?> mapperClassGen = entityMapperClassesCache.get(beanType);
                    if (mapperClassGen == null) {
                        mapperClassGen = createDynamicMapperClass(beanType);
                        // register to dao
                        mapperClass = mapperClassGen;
                        sqlSession.getConfiguration().addMapper(mapperClass);
                        entityMapperClassesCache.put(beanType, mapperClass);
                        LOG.info("Mapper class generated for class(double check) {} <===> {}", beanType, mapperClass);
                    } else {
                        mapperClass = mapperClassGen;
                    }
                }
            }
            Assert.notNull(mapperClass, "Mapper class not found");
            DynamicMapper<PersistableEntity> mapper = (DynamicMapper<PersistableEntity>) sqlSession.getMapper(mapperClass);
            return mapper;
        } catch (BindingException e) {
            if (mapperClass != null) sqlSession.getConfiguration().addMapper(mapperClass);
            DynamicMapper<PersistableEntity> mapper = (DynamicMapper<PersistableEntity>) sqlSession.getMapper(mapperClass);
            return mapper;
        } catch (Throwable e) {
            String message = "No mapper interface found for bean type " + beanType;
            RuntimeException exception = new RuntimeException(message, e);
            LOG.error(message, exception);
            throw exception;
        }
    }

    private static class DefaultSharding implements Sharding {

        @Override
        public String getDatabaseName(Object source) {
            return null;
        }

        @Override
        public String getTableName(Object source) {
            return null;
        }

        @Override
        public Map<String, Set<String>> getDatabaseNames(Object source) {
            return null;
        }

    }

    public IdWorker getIdWorker() {
        return idWorker;
    }

    public DatabaseRouter getRouter() {
        return router;
    }
}
