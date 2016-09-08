package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.Mapping.ContributorColumn;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class ContributorDatabaseHelper extends DatabaseHelper {

    public ContributorDatabaseHelper(Context context){
        super(context);
    }

    public ContributorEntity getbyID(String id) throws DatabaseException {
        String query = "SELECT * FROM " + ContributorColumn.TABLE_NAME + " WHERE " + ContributorColumn.CONTRIBUTOR_ID.getColumnName() + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{id});
        if (!cursor.moveToFirst()){
            throw new DatabaseException();
        }
        return getEntityFromCursor(cursor);
    }

    @Override
    protected ContributorEntity getEntityFromCursor(Cursor cursor) {
        return new ContributorEntity(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContributorColumn.CONTRIBUTOR_ID.getColumnName()))),
                                    cursor.getString(cursor.getColumnIndex(ContributorColumn.CONTRIBUTOR_NAME.getColumnName())));
    }
}
