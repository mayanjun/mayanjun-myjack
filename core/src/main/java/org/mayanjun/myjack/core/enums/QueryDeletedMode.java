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
