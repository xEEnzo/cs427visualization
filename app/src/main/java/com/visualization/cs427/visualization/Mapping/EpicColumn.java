package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum  EpicColumn {
    EPIC_ID("Id"),EPIC_NAME("Name"),EPIC_COLOR("Color"),PROJECT_ID("ProjectId");
    String columnName;

    private EpicColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Epic";
}
