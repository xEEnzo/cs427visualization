package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.EpicColumn;

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

    @Override
    protected EpicEntity getEntityFromCursor(Cursor cursor) {
        return new EpicEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(EpicColumn.EPIC_ID.getColumnName()))),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_NAME.getColumnName())),
                cursor.getString(cursor.getColumnIndex(EpicColumn.EPIC_COLOR.getColumnName())));
    }
}
