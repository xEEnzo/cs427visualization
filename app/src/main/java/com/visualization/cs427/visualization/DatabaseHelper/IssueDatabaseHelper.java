package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteTransactionListener;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.IssueBlockColumn;
import com.visualization.cs427.visualization.Mapping.IssueColumn;
import com.visualization.cs427.visualization.Mapping.TimeLogColumn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class IssueDatabaseHelper extends DatabaseHelper {
    private Context context;

    public IssueDatabaseHelper(Context context) {
        super(context);
        this.context = context;
    }

    public List<IssueEntity> getIssueByProjectID(String projectID) throws DatabaseException {
        List<IssueEntity> issueEntities = new ArrayList<>();
        String query = "SELECT * FROM " + IssueColumn.TABLE_NAME + " WHERE " + IssueColumn.ISSUE_PROJECT.getColumnName() + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{projectID});
        if (!cursor.moveToFirst()) {
            throw new DatabaseException();
        }
        do {
            issueEntities.add(getEntityFromCursor(cursor));
        } while (cursor.moveToNext());
        for (IssueEntity issueEntity : issueEntities) {
            // get blocked
            StringBuilder queryBlocked = new StringBuilder();
            queryBlocked.append("SELECT * FROM " + IssueBlockColumn.TABLE_NAME + " left join " + IssueColumn.TABLE_NAME);
            queryBlocked.append(" on " + IssueBlockColumn.ISSUE_BLOCKED.getColumnName() + " = " + IssueColumn.ISSUE_ID.getColumnName());
            queryBlocked.append(" WHERE " + IssueBlockColumn.ISSUE_BLOCKER.getColumnName() + " = ?");
            Cursor cursorBlocked = database.rawQuery(queryBlocked.toString(), new String[]{issueEntity.getId()});
            List<IssueEntity> blocked = new ArrayList<>();
            if (!cursorBlocked.moveToFirst()) {
                issueEntity.setBlocked(blocked);
            } else {
                do {
                    IssueEntity entity = getEntityFromCursor(cursorBlocked);
                    blocked.add(entity);
                } while (cursorBlocked.moveToNext());
                issueEntity.setBlocked(blocked);
            }
            // get blocker
            StringBuilder queryBlocker = new StringBuilder();
            queryBlocker.append("SELECT * FROM " + IssueBlockColumn.TABLE_NAME + " left join " + IssueColumn.TABLE_NAME);
            queryBlocker.append(" on " + IssueBlockColumn.ISSUE_BLOCKER.getColumnName() + " = " + IssueColumn.ISSUE_ID.getColumnName());
            queryBlocker.append(" WHERE " + IssueBlockColumn.ISSUE_BLOCKED.getColumnName() + " = ?");
            Cursor cursorBlocker = database.rawQuery(queryBlocker.toString(), new String[]{issueEntity.getId()});
            List<IssueEntity> blocker = new ArrayList<>();
            if (!cursorBlocker.moveToFirst()) {
                issueEntity.setBlocker(blocker);
            } else {
                do {
                    IssueEntity entity = getEntityFromCursor(cursorBlocker);
                    blocker.add(entity);
                } while (cursorBlocker.moveToNext());
                issueEntity.setBlocker(blocker);
            }
        }
        return issueEntities;
    }

    public void updateLocationOfIssue(IssueEntity issueEntity) throws DatabaseException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IssueColumn.ISSUE_LOCATION_STATUS.getColumnName(), issueEntity.getLocationStatus());
        try {
            database.beginTransaction();
            int rowAffect = database.update(IssueColumn.TABLE_NAME, contentValues, IssueColumn.ISSUE_ID.getColumnName() + "=?", new String[]{issueEntity.getId()});
            if (rowAffect == 0) {
                throw new DatabaseException();
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

    }

    public void insertNewIssue (IssueEntity issueEntity, ProjectEntity projectEntity) throws DatabaseException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IssueColumn.ISSUE_NAME.getColumnName(), issueEntity.getName());
        contentValues.put(IssueColumn.ISSUE_TYPE.getColumnName(), issueEntity.getType());
        contentValues.put(IssueColumn.ISSUE_POINT.getColumnName(), issueEntity.getPoint());
        contentValues.put(IssueColumn.ISSUE_DESCRIPTION.getColumnName(), issueEntity.getDescription());
        if (issueEntity.getAssignee() != null) {
            contentValues.put(IssueColumn.ISSUE_ASSIGNEE.getColumnName(), issueEntity.getAssignee().getId());
        }
        contentValues.put(IssueColumn.ISSUE_EPIC.getColumnName(), issueEntity.getEpic().getId());
        contentValues.put(IssueColumn.ISSUE_PROCESS_STATUS.getColumnName(), issueEntity.getProcessStatus());
        contentValues.put(IssueColumn.ISSUE_LOCATION_STATUS.getColumnName(), issueEntity.getLocationStatus());
        contentValues.put(IssueColumn.ISSUE_PROJECT.getColumnName(), projectEntity.getId());
        try {
            database.beginTransaction();
            long id = database.insert(IssueColumn.TABLE_NAME, IssueColumn.ISSUE_ASSIGNEE.getColumnName(), contentValues);
            if (id == -1) {
                throw new DatabaseException();
            }
            issueEntity.setId(String.valueOf(id));
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        insertToIssueBlock(issueEntity);
    }

    public void insertToIssueBlock(IssueEntity issueEntity) throws DatabaseException {
        if (issueEntity.getBlocked() != null){
            List<IssueEntity> blocked = issueEntity.getBlocked();
            for (IssueEntity entity : blocked){
                ContentValues contentValues = new ContentValues();
                contentValues.put(IssueBlockColumn.ISSUE_BLOCKER.getColumnName(), entity.getId());
                contentValues.put(IssueBlockColumn.ISSUE_BLOCKED.getColumnName(), issueEntity.getId());
                try {
                    database.beginTransaction();
                    long id = database.insert(IssueBlockColumn.TABLE_NAME, null, contentValues);
                    if (id == -1) {
                        throw new DatabaseException();
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            }
        }
        if (issueEntity.getBlocker()!= null){
            List<IssueEntity> blocker = issueEntity.getBlocked();
            for (IssueEntity entity : blocker){
                ContentValues contentValues = new ContentValues();
                contentValues.put(IssueBlockColumn.ISSUE_BLOCKER.getColumnName(), issueEntity.getId());
                contentValues.put(IssueBlockColumn.ISSUE_BLOCKED.getColumnName(), entity.getId());
                try {
                    database.beginTransaction();
                    long id = database.insert(IssueBlockColumn.TABLE_NAME, null, contentValues);
                    if (id == -1) {
                        throw new DatabaseException();
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            }
        }
    }

    public List<IssueEntity> updateIssueLocation(IssueEntity entity, String projectID) throws DatabaseException {
        updateLocationOfIssue(entity);
        return getIssueByProjectID(projectID);
    }

    public List<IssueEntity> createNewIssue(IssueEntity entity, ProjectEntity projectEntity) throws DatabaseException {
        insertNewIssue(entity, projectEntity);
        return getIssueByProjectID(projectEntity.getId());
    }



    @Override
    protected IssueEntity getEntityFromCursor(Cursor cursor) throws DatabaseException {
        EpicDatabaseHelper epicDatabaseHelper = null;
        ContributorDatabaseHelper contributorDatabaseHelper = null;
        EpicEntity epic = null;
        ContributorEntity assignee = null;
        try {
            epicDatabaseHelper = new EpicDatabaseHelper(context);
            contributorDatabaseHelper = new ContributorDatabaseHelper(context);
            if (!cursor.isNull(cursor.getColumnIndex(IssueColumn.ISSUE_ASSIGNEE.getColumnName()))) {
                String contributorID = String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_ASSIGNEE.getColumnName())));
                assignee = contributorDatabaseHelper.getbyID(contributorID);
            }
            String epicID = String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_EPIC.getColumnName())));
            if (epicID != null) {
                epic = epicDatabaseHelper.getbyID(epicID);
            }
        } finally {
            epicDatabaseHelper.closeConnection();
            contributorDatabaseHelper.closeConnection();
        }

        return new IssueEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_ID.getColumnName()))),
                cursor.getString(cursor.getColumnIndex(IssueColumn.ISSUE_NAME.getColumnName())),
                cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_TYPE.getColumnName())),
                cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_POINT.getColumnName())),
                cursor.getString(cursor.getColumnIndex(IssueColumn.ISSUE_DESCRIPTION.getColumnName())),
                cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_PROCESS_STATUS.getColumnName())),
                cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_LOCATION_STATUS.getColumnName())),
                assignee, epic);
    }

    public void updateIssueStatus(IssueEntity issueEntity, ContributorEntity contributorEntity, String spentTime) throws DatabaseException {
        ContentValues contentUpdate = new ContentValues();
        contentUpdate.put(IssueColumn.ISSUE_PROCESS_STATUS.getColumnName(), issueEntity.getProcessStatus());
        contentUpdate.put(IssueColumn.ISSUE_ASSIGNEE.getColumnName(), contributorEntity.getId());
        ContentValues contentInsert = new ContentValues();
        contentInsert.put(TimeLogColumn.ISSUE_ID.getColumnName(), issueEntity.getId());
        contentInsert.put(TimeLogColumn.STATUS.getColumnName(), issueEntity.getProcessStatus());
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        contentInsert.put(TimeLogColumn.ASSIGNED_TIME.getColumnName(), timestamp.toString());
        contentInsert.put(TimeLogColumn.SPENT_TIME.getColumnName(), spentTime);
        try {
            database.beginTransaction();
            int rowAffect = database.update(IssueColumn.TABLE_NAME, contentUpdate, IssueColumn.ISSUE_ID.getColumnName() + " = ?", new String[]{issueEntity.getId()});
            if (rowAffect == 0) {
                throw new DatabaseException();
            }
            database.insert(TimeLogColumn.TABLE_NAME, null, contentInsert);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
