package org.mayanjun.myjack.core.entity;

import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.annotation.PrimaryKey;
import org.mayanjun.myjack.core.enums.DataType;

import java.util.Date;

/**
 * Represents a entity bean that can be serialized to database
 *
 * @author mayanjun
 * @since 0.0.1
 */
public abstract class EntityBean implements PersistableEntity<Long> {

    private static final long serialVersionUID = 4316289291606068158L;

    @Column(type = DataType.DATETIME, comment = "信息创建时间")
    private Date createdTime;

    @Column(type = DataType.BIGINT, comment = "ID", unsigned = true)
    @PrimaryKey
    private Long id;

    @Column(type = DataType.DATETIME, comment = "信息最后一次修改时间")
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
