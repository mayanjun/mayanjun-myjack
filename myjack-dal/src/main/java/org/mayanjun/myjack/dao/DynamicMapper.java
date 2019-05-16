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

import org.apache.ibatis.annotations.*;
import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.parser.SQLParameter;

import java.io.Serializable;
import java.util.List;

/**
 * MyBatisDynamicMapper
 *
 * @author mayanjun(6/22/16)
 * @since 0.0.5
 */
public interface DynamicMapper<T extends Serializable> {

    String PARAM_NAME = "bean";

    @SelectProvider(type = DynamicSqlBuilder.class, method = "buildQuery")
    List<T> query(@Param(PARAM_NAME) SQLParameter<T> parameter, Sharding sharding);

    @SelectProvider(type = DynamicSqlBuilder.class, method = "buildCount")
    long count(@Param(PARAM_NAME) SQLParameter<?> parameter, Sharding sharding);

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = PARAM_NAME + ".id")
    @InsertProvider(type = DynamicSqlBuilder.class, method = "buildInsert")
    int insert(@Param(PARAM_NAME) T bean, Sharding sharding);

    @UpdateProvider(type = DynamicSqlBuilder.class, method = "buildUpdate")
    int update(@Param(PARAM_NAME) T bean, Sharding sharding);

    @DeleteProvider(type = DynamicSqlBuilder.class, method = "buildDelete")
    int delete(@Param(PARAM_NAME) T bean, Sharding sharding);

    @UpdateProvider(type = DynamicSqlBuilder.class, method = "buildQueryUpdate")
    int queryUpdate(@Param(PARAM_NAME) SQLParameter<T> parameter, Sharding sharding);

    @DeleteProvider(type = DynamicSqlBuilder.class, method = "buildQueryDelete")
    int queryDelete(@Param(PARAM_NAME) SQLParameter<T> parameter, Sharding sharding);

}
