package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum ProjectColumn {
    PROJECT_ID("Id"),PROJECT_NAME("Name");
    String columnName;

    private ProjectColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Project";
}
