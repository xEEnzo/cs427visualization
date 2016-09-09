package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteTransactionListener;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.IssueColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class IssueDatabaseHelper extends DatabaseHelper {
    private Context context;
    public IssueDatabaseHelper(Context context){
        super(context);
        this.context = context;
    }

    public List<IssueEntity> getIssueByProjectID(String projectID) throws DatabaseException {
        List<IssueEntity> issueEntities = new ArrayList<>();
        String query = "SELECT * FROM " + IssueColumn.TABLE_NAME + " WHERE " + IssueColumn.ISSUE_PROJECT.getColumnName() + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{projectID});
        if (!cursor.moveToFirst()){
            throw new DatabaseException();
        }
        do {
            issueEntities.add(getEntityFromCursor(cursor));
        } while (cursor.moveToNext());
        return issueEntities;
    }

    public void updateLocationOfIssue(IssueEntity issueEntity) throws DatabaseException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IssueColumn.ISSUE_LOCATION_STATUS.getColumnName(), issueEntity.getLocationStatus());
        try{
            database.beginTransaction();
            int rowAffect = database.update(IssueColumn.TABLE_NAME, contentValues, IssueColumn.ISSUE_ID.getColumnName() + "=?", new String[]{issueEntity.getId()});
            if (rowAffect == 0){
                throw new DatabaseException();
            }
            database.setTransactionSuccessful();
        }
        finally{
            database.endTransaction();
        }

    }

    public List<IssueEntity> updateIssueLocation (IssueEntity entity, String projectID) throws DatabaseException {
        updateLocationOfIssue(entity);
        return getIssueByProjectID(projectID);
    }


    @Override
    protected IssueEntity getEntityFromCursor(Cursor cursor) throws DatabaseException {
        EpicDatabaseHelper epicDatabaseHelper = null;
        ContributorDatabaseHelper contributorDatabaseHelper = null;
        EpicEntity epic = null;
        ContributorEntity assignee = null;
        try{
            epicDatabaseHelper = new EpicDatabaseHelper(context);
            contributorDatabaseHelper = new ContributorDatabaseHelper(context);
            String contributorID = String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_ASSIGNEE.getColumnName())));
            String epicID = String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_EPIC.getColumnName())));
            epic = epicDatabaseHelper.getbyID(epicID);
            assignee = contributorDatabaseHelper.getbyID(contributorID);
        }
        finally {
            epicDatabaseHelper.closeConnection();
            contributorDatabaseHelper.closeConnection();
        }

        return new IssueEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_ID.getColumnName()))),
                               cursor.getString(cursor.getColumnIndex(IssueColumn.ISSUE_NAME.getColumnName())),
                               cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_TYPE.getColumnName())),
                               cursor.getString(cursor.getColumnIndex(IssueColumn.ISSUE_SUMMARY.getColumnName())),
                               cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_POINT.getColumnName())),
                               cursor.getString(cursor.getColumnIndex(IssueColumn.ISSUE_DESCRIPTION.getColumnName())),
                               cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_PROCESS_STATUS.getColumnName())),
                               cursor.getInt(cursor.getColumnIndex(IssueColumn.ISSUE_LOCATION_STATUS.getColumnName())),
                               assignee, epic);
    }
}
