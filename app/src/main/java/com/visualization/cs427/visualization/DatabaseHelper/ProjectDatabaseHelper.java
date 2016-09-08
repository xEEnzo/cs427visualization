package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.ProjectColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class ProjectDatabaseHelper extends DatabaseHelper {

    public ProjectDatabaseHelper(Context context) {
        super(context);
    }

    public List<ProjectEntity> getAll () throws DatabaseException {
        String query = "SELECT * FROM " + ProjectColumn.TABLE_NAME;
        Cursor result = database.rawQuery(query, null);
        List<ProjectEntity> entities = new ArrayList<>();
        if (!result.moveToFirst()){
            throw new DatabaseException();
        }
        do {
            entities.add(getEntityFromCursor(result));
        } while (result.moveToNext());
        return entities;
    }

    @Override
    protected ProjectEntity getEntityFromCursor(Cursor cursor) {
        return new ProjectEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(ProjectColumn.PROJECT_ID.getColumnName()))),
                cursor.getString(cursor.getColumnIndex(ProjectColumn.PROJECT_NAME.getColumnName())));
    }

}
