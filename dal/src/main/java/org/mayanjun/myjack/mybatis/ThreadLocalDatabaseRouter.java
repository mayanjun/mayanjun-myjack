package org.mayanjun.myjack.mybatis;

import org.mayanjun.myjack.Sharding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ThreadLocalDatabaseRouter
 *
 * @author mayanjun(6/24/16)
 */
public class ThreadLocalDatabaseRouter implements DatabaseRouter, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalDatabaseRouter.class);

    private List<DatabaseSession> databaseSessions;

    private Map<String, DatabaseSession> databaseSessionMap;

    private ThreadLocal<DatabaseSession> databaseSessionThreadLocal;


    private DatabaseSession masterDatabaseSession;

    public ThreadLocalDatabaseRouter() {
        this.databaseSessions = new ArrayList<DatabaseSession>();
        this.databaseSessionMap = new HashMap<String, DatabaseSession>();
        this.databaseSessionThreadLocal = new ThreadLocal<DatabaseSession>();
    }

    public ThreadLocalDatabaseRouter addDatabaseSession(DatabaseSession session) {
        databaseSessions.add(session);
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(databaseSessions != null && !databaseSessions.isEmpty(), "The database sessions must not be empty");
        masterDatabaseSession = databaseSessions.get(0);
        for (DatabaseSession session : databaseSessions) {
            databaseSessionMap.put(session.getName(), session);
        }
    }

    @Override
    public DatabaseSession getDatabaseSession() {
        DatabaseSession session = databaseSessionThreadLocal.get();
        if (session != null) return session;
        return masterDatabaseSession;
    }

    @Override
    public void shareDatabaseSession(DatabaseSession session) {
        databaseSessionThreadLocal.remove();
        databaseSessionThreadLocal.set(session);
    }

    @Override
    public List<DatabaseSession> getDatabaseSessions() {
        return databaseSessions;
    }

    @Override
    public DatabaseSession getDatabaseSession(String name) {
        DatabaseSession session = databaseSessionMap.get(name);
        if (session != null) return session;
        return getDatabaseSession();
    }

    @Override
    public DatabaseSession getDatabaseSession(Sharding sharding, Object source) {
        if (sharding != null) {
            return getDatabaseSession(sharding.getDatabaseName(source));
        } else {
            return getDatabaseSession();
        }
    }
}
