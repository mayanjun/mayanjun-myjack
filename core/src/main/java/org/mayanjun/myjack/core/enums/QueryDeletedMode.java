package org.mayanjun.myjack.core.enums;

/**
 * @author mayanjun
 * @since 2018/6/28
 */
public enum QueryDeletedMode {

    /**
     * 查询结果中包含已删除的数据
     */
    WITH_DELETED,

    /**
     * 查询结果中不包含已删除的数据
     */
    WITHOUT_DELETED,

    /**
     * 查询结果中只查删除的数据
     */
    ONLY_DELETED

}
