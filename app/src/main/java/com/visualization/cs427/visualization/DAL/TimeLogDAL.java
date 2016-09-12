package com.visualization.cs427.visualization.DAL;

import android.content.Context;

import com.visualization.cs427.visualization.DatabaseHelper.TimeLogDatabaseHelper;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

/**
 * Created by manything on 9/13/16.
 */
public class TimeLogDAL {
    private static  TimeLogDAL _instance;
    public static TimeLogDAL getInstance() {
        if (_instance == null) {
            _instance = new TimeLogDAL();
        }
        return _instance;
    }

    public String getTimeSpent(Context context,IssueEntity issueEntity, int status) throws DatabaseException {
        TimeLogDatabaseHelper dbh = null;
        try {
            dbh = new TimeLogDatabaseHelper(context);
            return dbh.getTimeSpent(issueEntity, status);
        } finally {
            if (dbh != null) {
                dbh.closeConnection();
            }
        }
    }
}
