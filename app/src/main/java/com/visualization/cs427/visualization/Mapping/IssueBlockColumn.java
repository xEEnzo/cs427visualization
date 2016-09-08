package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum  IssueBlockColumn {
    ISSUE_BLOCKER("BlockerRefIssue"),ISSUE_BLOCKED("BlockedRefIssue");
    String columnName;

    private IssueBlockColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Issue_block";
}
