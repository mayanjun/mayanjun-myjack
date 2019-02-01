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

package org.mayanjun.myjack.parser;

import org.mayanjun.myjack.core.query.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SqlParameter
 *
 * @author mayanjun(6/20/16)
 * @since 0.0.5
 */
public class SQLParameter<T extends Serializable> extends HashMap<String, Object> implements Serializable {

    private String selectedFields;
    private String tableName;
    private String whereClause;
    private String orderClause;
	private String limitClause;

    private AtomicInteger fieldCount = new AtomicInteger(0);
    private Class<T> resultType;
    private Query<T> query;

    private String entityName;

    public SQLParameter(Query<T> query) {
        super();
        this.query = query;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntity(T entity) {
        if(entityName == null) entityName = newParameterName();
        this.put(entityName, entity);
    }

    public Query<T> getQuery() {
        return query;
    }

    public Class<T> getResultType() {
        return resultType;
    }

    public void setResultType(Class<T> resultType) {
        this.resultType = resultType;
    }

    public SQLParameter addParameter(String name, Object value) {
        this.put(name, value);
        return this;
    }

    public SQLParameter removeParameter(String name) {
        this.remove(name);
        return this;
    }

    public String newParameterName() {
        return "val" + (fieldCount.getAndIncrement());
    }

    @Override
    public String toString() {
        return "SQLParameter:" + getSql();
    }

    public String getSql() {
    	StringBuilder sb = new StringBuilder("SELECT ")
				.append(this.selectedFields)
				.append(" FROM ")
				.append(tableName)
				.append(whereClause);
        if(orderClause != null) sb.append(orderClause);
		if(limitClause != null) sb.append(limitClause);
		if(query.isForUpdate()) sb.append(" FOR UPDATE");
		return sb.toString();
    }

    public String getCountSql() {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*)")
                .append(" FROM ")
                .append(tableName)
                .append(whereClause);
        if(query.isForUpdate()) sb.append(" FOR UPDATE");
        return sb.toString();
    }

    public String getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(String selectedFields) {
        this.selectedFields = selectedFields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	public String getLimitClause() {
		return limitClause;
	}

	public void setLimitClause(String limitClause) {
		this.limitClause = limitClause;
	}
}
