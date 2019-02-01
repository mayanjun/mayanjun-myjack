package org.mayanjun.myjack.mybatis;

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
