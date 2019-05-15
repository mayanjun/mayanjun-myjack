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

package org.mayanjun.myjack.api.query;

/**
 * GroupComparator
 *
 * @author mayanjun(11/14/15)
 * @since 0.0.1
 */
public class GroupComparator extends LogicalComparator {

    public static final String START = "(";

    public static final String END = ")";

    private boolean start;

    public boolean isStart() {
        return start;
    }

    public GroupComparator(boolean start, LogicalOperator lo) {
        super("", lo);
        this.start = start;

    }

    @Override
    public String getExpression() {
        return this.start ? START : END;
    }

}
