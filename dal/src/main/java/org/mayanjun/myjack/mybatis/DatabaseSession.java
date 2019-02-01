package org.mayanjun.myjack.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.support.TransactionTemplate;

public class DatabaseSession {

    private String name;

    private SqlSession session;

    private TransactionTemplate transaction;

    public DatabaseSession(String name, SqlSession session) {
        this.name = name;
        this.session = session;
    }

    public DatabaseSession(String name, SqlSession session, TransactionTemplate transaction) {
        this.name = name;
        this.session = session;
        this.transaction = transaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SqlSession getSession() {
        return session;
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }

    public TransactionTemplate getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionTemplate transaction) {
        this.transaction = transaction;
    }
}
