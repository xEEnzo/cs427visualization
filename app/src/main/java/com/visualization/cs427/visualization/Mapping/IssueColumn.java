package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum IssueColumn {
    ISSUE_ID("Id"),ISSUE_NAME("Name"),ISSUE_TYPE("Type"),ISSUE_SUMMARY("Summary"), ISSUE_POINT("StoryPoint"), ISSUE_DESCRIPTION("Description"),
    ISSUE_ASSIGNEE("AssigneeRefContributor"), ISSUE_EPIC("EpicRefEpic"), ISSUE_PROCESS_STATUS("ProcessStatus"), ISSUE_LOCATION_STATUS("LocationStatus"),
    ISSUE_PROJECT("ProjectIdRefProject");
    String columnName;

    private IssueColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Issue";
}
