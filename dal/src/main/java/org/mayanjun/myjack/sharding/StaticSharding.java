package org.mayanjun.myjack.sharding;

import org.mayanjun.myjack.Sharding;

import java.util.Map;
import java.util.Set;

/**
 * StaticSharding
 *
 * @author mayanjun(17/11/2016)
 */
public class StaticSharding implements Sharding {

	private String databaseName;
	private String tableName;

	public StaticSharding(String databaseName, String tableName) {
		this.databaseName = databaseName;
		this.tableName = tableName;
	}

	@Override
	public String getDatabaseName(Object source) {
		return databaseName;
	}

	@Override
	public String getTableName(Object source) {
		return tableName;
	}

	@Override
	public Map<String, Set<String>> getDatabaseNames(Object source) {
		return null;
	}

}
