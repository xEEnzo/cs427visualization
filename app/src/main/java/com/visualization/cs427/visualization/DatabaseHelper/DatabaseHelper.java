package com.visualization.cs427.visualization.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visualization.cs427.visualization.Entity.Entity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public abstract class DatabaseHelper {
    protected SQLiteDatabase database;
    protected Context context;
    private String DB_NAME = "structure";

    public DatabaseHelper(Context context) {
        this.context = context;
        copyDataBaseFromAsset();
        File dbFile = context.getDatabasePath(DB_NAME);
        database = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    protected abstract Entity getEntityFromCursor(Cursor cursor) throws DatabaseException;

    public void closeConnection() {
        if (database == null) {
            return;
        }
        database.close();
    }

    private void copyDataBaseFromAsset() {
        File file = context.getDatabasePath(DB_NAME);
        if (file.exists()) {
            return;
        }
        try {
            InputStream inputStream = context.getAssets().open(DB_NAME+".db");
            File dir = new File(context.getApplicationInfo().dataDir + "/databases");
            if (!dir.exists()) {
                dir.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
