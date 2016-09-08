package com.visualization.cs427.visualization.Mapping;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public enum ContributorColumn {
    CONTRIBUTOR_ID("Id"),CONTRIBUTOR_NAME("Name");
    String columnName;

    private ContributorColumn(String columnName) {
        this.columnName = columnName;
    }

    public  String getColumnName() {
        return columnName;
    }

    public static final String TABLE_NAME = "Contributor";
}
