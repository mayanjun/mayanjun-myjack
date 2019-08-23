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

package org.mayanjun.myjack.util;

/**
 * SQL
 *
 * @author mayanjun(6/30/16)
 */
public class SQLBuilder {

    private StringBuilder sql;
    private boolean setPresented;

    private SQLBuilder() {
        this.sql = new StringBuilder();
    }

    public static SQLBuilder custom() {
        return new SQLBuilder();
    }

    public SQLBuilder select(String sql) {
        this.sql.append(" SELECT ").append(sql);
        return this;
    }

    public SQLBuilder append(String sql) {
        this.sql.append(sql);
        return this;
    }

    public SQLBuilder and(String sql) {
        this.sql.append(" AND ").append(sql);
        return this;
    }

    public SQLBuilder or(String sql) {
        this.sql.append(" OR ").append(sql);
        return this;
    }

    public SQLBuilder in(String sql) {
        this.sql.append(" IN ").append(sql);
        return this;
    }

    public SQLBuilder between(String sql) {
        this.sql.append(" BETWEEN ").append(sql);
        return this;
    }

    public SQLBuilder from(String sql) {
        this.sql.append(" FROM ").append(sql);
        return this;
    }

    public SQLBuilder where(String sql) {
        this.sql.append(" WHERE ").append(sql);
        return this;
    }

    public SQLBuilder groupBy(String sql) {
        this.sql.append(" GROUP BY ").append(sql);
        return this;
    }

    public SQLBuilder orderBy(String sql) {
        this.sql.append(" ORDER BY ").append(sql);
        return this;
    }

    public SQLBuilder limit(String sql) {
        this.sql.append(" LIMIT ").append(sql);
        return this;
    }

    public SQLBuilder forUpdate() {
        this.sql.append(" FOR UPDATE ");
        return this;
    }

    public SQLBuilder lockInShareMode() {
        this.sql.append(" LOCK IN SHARE MODE ");
        return this;
    }

    public SQLBuilder update(String sql) {
        this.sql.append(" UPDATE ").append(sql);
        return this;
    }

    public SQLBuilder set(String sql) {
        if(setPresented) this.sql.append(" ,").append(sql);
        else {
            this.sql.append(" SET ").append(sql);
            setPresented = true;
        }
        return this;
    }

    public SQLBuilder insert(String sql) {
        this.sql.append(" INSERT ").append(sql);
        return this;
    }

    public SQLBuilder into(String sql) {
        this.sql.append(" INTO ").append(sql);
        return this;
    }

    public SQLBuilder insertInto(String sql) {
        this.sql.append(" INSERT INTO ").append(sql);
        return this;
    }

    public SQLBuilder deleteFrom(String sql) {
        this.sql.append(" DELETE FROM ").append(sql);
        return this;
    }

    public SQLBuilder partition(String sql) {
        this.sql.append(" PARTITION ").append(sql);
        return this;
    }

    public String build() {
        return this.sql.toString().trim();
    }
}
