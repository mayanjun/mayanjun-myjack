package org.mayanjun.myjack.core.query;

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
