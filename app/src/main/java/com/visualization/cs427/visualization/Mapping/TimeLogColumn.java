package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum  TimeLogColumn {
    ISSUE_ID("IssueRefIssue"),STATUS("Status"),SPENT_TIME("SpentTime"),ASSIGNED_TIME("AssignedTime");
    String columnName;

    private TimeLogColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Time_log";
}
