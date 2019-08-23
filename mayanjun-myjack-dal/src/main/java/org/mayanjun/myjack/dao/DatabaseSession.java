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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Database session
 */
public class DatabaseSession {

    /**
     * Session name
     */
    private String name;

    /**
     * Underlying session. Here is a mybatis implementation
     */
    private SqlSession sqlSession;

    /**
     * Transaction
     */
    private TransactionTemplate transaction;

    /**
     * Test if a Mapper class is registered
     * @param type mybatis mapper type
     * @return
     */
    public boolean hasMapper(Class<?> type) {
        return sqlSession.getConfiguration().hasMapper(type);
    }

    /**
     * Return a registered mapper
     * @param type mapper class
     * @param <T> mapper type
     * @return Mapper object
     */
    public <T> T getMapper(Class<T> type) {
        if (!sqlSession.getConfiguration().hasMapper(type)) {
            sqlSession.getConfiguration().addMapper(type);
        }
        return sqlSession.getMapper(type);
    }

    /**
     * Construct an instance of DatabaseSession
     * @param name database session name
     * @param sqlSession mybatis sql session
     */
    public DatabaseSession(String name, SqlSession sqlSession) {
        this.name = name;
        this.sqlSession = sqlSession;
    }

    /**
     * Construct an instance of DatabaseSession
     * @param name database session name
     * @param sqlSession mybatis sql session
     * @param transaction transaction
     */
    public DatabaseSession(String name, SqlSession sqlSession, TransactionTemplate transaction) {
        this.name = name;
        this.sqlSession = sqlSession;
        this.transaction = transaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public TransactionTemplate getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionTemplate transaction) {
        this.transaction = transaction;
    }
}
