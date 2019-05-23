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

package org.mayanjun.myjack.api.entity;

import org.mayanjun.myjack.api.annotation.Column;
import org.mayanjun.myjack.api.annotation.PrimaryKey;
import org.mayanjun.myjack.api.enums.DataType;

import java.util.Date;

/**
 * Represents a entity bean that can be serialized to database.
 * The type of id field is set to Long
 *
 * @author mayanjun
 * @since 0.0.1
 */
public abstract class EntityBean implements PersistableEntity<Long> {

    private static final long serialVersionUID = 4316289291606068158L;

    @Column(type = DataType.DATETIME, comment = "Created Time")
    private Date createdTime;

    @Column(type = DataType.BIGINT, comment = "ID", unsigned = true)
    @PrimaryKey
    private Long id;

    @Column(type = DataType.DATETIME, comment = "Last Modified Time")
    private Date modifiedTime;

    public EntityBean() {
    }

    public EntityBean(Long id) {
        this();
        this.id = id;
    }

    @Override
    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

}
