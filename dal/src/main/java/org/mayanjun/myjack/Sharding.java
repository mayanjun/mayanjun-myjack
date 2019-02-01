package org.mayanjun.myjack;

import java.util.Map;
import java.util.Set;

/**
 * Sharding
 *
 * @author mayanjun(6/27/16)
 */
public interface Sharding {

    String getDatabaseName(Object source);

    String getTableName(Object source);

    Map<String, Set<String>> getDatabaseNames(Object source);

}
