package org.mayanjun.myjack.core.entity;

import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.enums.DataType;

/**
 * @author mayanjun
 * @since 16/05/2017
 */
public abstract class DeletableEntity extends EditableEntity {

    public DeletableEntity() {
    }

    public DeletableEntity(Long id) {
        super(id);
    }

    @Column(type = DataType.BIT, length = "1", comment = "是否被删除")
    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
