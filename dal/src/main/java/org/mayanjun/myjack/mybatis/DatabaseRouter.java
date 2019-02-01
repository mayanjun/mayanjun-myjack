package org.mayanjun.myjack.mybatis;

import org.mayanjun.myjack.Sharding;

import java.util.List;

/**
 * DatabaseRouter
 *
 * @author mayanjun(6/24/16)
 */
public interface DatabaseRouter {

    /**
     * 获取正在使用的 sqlSession. 如果找不到则默认获取第一个
     * @return
     */
    DatabaseSession getDatabaseSession();

    /**
     * 是否共享此session
     * @param session
     */
    void shareDatabaseSession(DatabaseSession session);

    List<DatabaseSession> getDatabaseSessions();

    /**
     * 通过名字获取session
     * @param name
     * @return
     */
    DatabaseSession getDatabaseSession(String name);

    /**
     * 通过Sharding对象获取session
     * @param sharding
     * @param source
     * @return
     */
    DatabaseSession getDatabaseSession(Sharding sharding, Object source);
}
