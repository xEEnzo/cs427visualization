package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
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

    public List<EpicEntity> getAllEpic (ProjectEntity projectEntity) throws DatabaseException {
        List<EpicEntity> epicEntities = new ArrayList<>();
        String query = "SELECT * FROM " + EpicColumn.TABLE_NAME + " WHERE " + EpicColumn.PROJECT_ID.getColumnName() +" = ?";
        Cursor cursor = database.rawQuery(query, new String[]{projectEntity.getId()});
        if (!cursor.moveToFirst()){
            throw new DatabaseException();
        }
        do {
            EpicEntity epicEntity = getEntityFromCursor(cursor);
            epicEntity.setProjectEntity(projectEntity);
            epicEntities.add(epicEntity);
        } while (cursor.moveToNext());
        cursor.close();
        return epicEntities;
    }

    public void createNewEpic (EpicEntity epicEntity) throws DatabaseException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EpicColumn.EPIC_ID.getColumnName(), epicEntity.getId());
        contentValues.put(EpicColumn.EPIC_NAME.getColumnName(), epicEntity.getName());
        contentValues.put(EpicColumn.EPIC_COLOR.getColumnName(), epicEntity.getColorResID());
        contentValues.put(EpicColumn.PROJECT_ID.getColumnName(), epicEntity.getProjectEntity().getId());
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

    public List<EpicEntity> insertNewEpic (EpicEntity epicEntity) throws DatabaseException {
        createNewEpic(epicEntity);
        return getAllEpic(epicEntity.getProjectEntity());
    }


    @Override
    protected EpicEntity getEntityFromCursor(Cursor cursor) {
        return new EpicEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(EpicColumn.EPIC_ID.getColumnName()))),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_NAME.getColumnName())),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_COLOR.getColumnName())),null);
    }
}
