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
