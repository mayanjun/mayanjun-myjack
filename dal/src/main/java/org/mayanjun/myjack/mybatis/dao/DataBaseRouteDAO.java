package org.mayanjun.myjack.mybatis.dao;


import org.mayanjun.myjack.core.EntityAccessor;
import org.mayanjun.myjack.mybatis.DatabaseRouter;

/**
 * DataBaseRouteDAO
 *
 * @author mayanjun(6/24/16)
 */
public interface DataBaseRouteDAO extends EntityAccessor {

    DatabaseRouter getDataBaseRouter();
}
