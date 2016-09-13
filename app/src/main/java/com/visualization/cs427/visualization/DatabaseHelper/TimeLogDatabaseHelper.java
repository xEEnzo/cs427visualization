package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.TimeLogColumn;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toan on 9/11/2016.
 */
public class TimeLogDatabaseHelper extends DatabaseHelper {

    public TimeLogDatabaseHelper(Context context) {
        super(context);
    }

    @Override
    protected Entity getEntityFromCursor(Cursor cursor) throws DatabaseException {
        return null;
    }

    public String getTimeSpent(IssueEntity issueEntity, int status) throws DatabaseException {
        String query = "SELECT * FROM " + TimeLogColumn.TABLE_NAME + " WHERE " + TimeLogColumn.ISSUE_ID.getColumnName() + " = ? and "
                + TimeLogColumn.STATUS + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{issueEntity.getId(), "" + status});
        if (!cursor.moveToFirst()) {
            throw new DatabaseException();
        }
        return cursor.getString(cursor.getColumnIndex(TimeLogColumn.SPENT_TIME.getColumnName()));
    }


    public Timestamp getTimeAssigned(IssueEntity issueEntity, int status) throws DatabaseException {
        String query = "SELECT * FROM " + TimeLogColumn.TABLE_NAME + " WHERE " + TimeLogColumn.ISSUE_ID.getColumnName() + " = ? and "
                + TimeLogColumn.STATUS + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{issueEntity.getId(), "" + (status)});
        if (!cursor.moveToFirst()) {
            throw new DatabaseException();
        }
        String rs = cursor.getString(cursor.getColumnIndex(TimeLogColumn.ASSIGNED_TIME.getColumnName()));
        Timestamp timestamp = Timestamp.valueOf(rs);
        return timestamp;
    }
}
