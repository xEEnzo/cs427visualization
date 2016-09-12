package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.EpicColumn;
import com.visualization.cs427.visualization.Mapping.IssueColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class EpicDatabaseHelper extends DatabaseHelper {

    public EpicDatabaseHelper(Context context){
        super(context);
    }

    public EpicEntity getbyID(String id) throws DatabaseException {
        String query = "SELECT * FROM " + EpicColumn.TABLE_NAME + " WHERE " + EpicColumn.EPIC_ID.getColumnName() + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{id});
        if (!cursor.moveToFirst()){
            throw new DatabaseException();
        }
        return getEntityFromCursor(cursor);
    }

    public List<EpicEntity> getAllEpic (String projectID) throws DatabaseException {
        List<EpicEntity> epicEntities = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(EpicColumn.EPIC_ID.getColumnName()+ ", ");
        query.append(EpicColumn.EPIC_NAME.getColumnName() + ", ");
        query.append(EpicColumn.EPIC_COLOR.getColumnName() + " ");
        query.append("FROM ");
        query.append(EpicColumn.TABLE_NAME+ " AS A");
        query.append(" LEFT JOIN "+ IssueColumn.TABLE_NAME + " AS B");
        query.append(" ON ");
        query.append("A."+EpicColumn.EPIC_ID.getColumnName());
        query.append(" = B."+IssueColumn.ISSUE_EPIC.getColumnName());
        query.append(" WHERE "+ IssueColumn.ISSUE_PROJECT.getColumnName());
        query.append(" = ?");
        Cursor cursor = database.rawQuery(query.toString(), new String[]{projectID});
        if (!cursor.moveToFirst()){
            throw new DatabaseException();
        }
        do {
            epicEntities.add(getEntityFromCursor(cursor));
        } while (cursor.moveToNext());
        cursor.close();
        return epicEntities;
    }

    public void createNewEpic (EpicEntity epicEntity) throws DatabaseException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EpicColumn.EPIC_ID.getColumnName(), epicEntity.getId());
        contentValues.put(EpicColumn.EPIC_NAME.getColumnName(), epicEntity.getName());
        contentValues.put(EpicColumn.EPIC_COLOR.getColumnName(), epicEntity.getColorResID());
        try{
            database.beginTransaction();
            long id = database.insert(EpicColumn.TABLE_NAME, null, contentValues);
            if (id == -1){
                throw new DatabaseException();
            }
            database.setTransactionSuccessful();
        }
        finally{
            database.endTransaction();
        }
    }

    public List<EpicEntity> insertNewEpic (EpicEntity epicEntity, String projectID) throws DatabaseException {
        createNewEpic(epicEntity);
        return getAllEpic(projectID);

    }


    @Override
    protected EpicEntity getEntityFromCursor(Cursor cursor) {
        return new EpicEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(EpicColumn.EPIC_ID.getColumnName()))),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_NAME.getColumnName())),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_COLOR.getColumnName())));
    }
}
