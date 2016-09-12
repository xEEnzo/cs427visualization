package com.visualization.cs427.visualization.DAL;

import android.content.Context;

import com.visualization.cs427.visualization.DatabaseHelper.EpicDatabaseHelper;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

import java.util.List;

/**
 * Created by linhtnvo on 9/12/2016.
 */
public class EpicDAL {
    private static  EpicDAL _instance;
    public static EpicDAL getInstance() {
        if (_instance == null) {
            _instance = new EpicDAL();
        }
        return _instance;
    }

    public List<EpicEntity> insertNewEpic (Context context, EpicEntity epicEntity) throws DatabaseException {
        EpicDatabaseHelper helper = null;
        try {
            helper = new EpicDatabaseHelper(context);
            return helper.insertNewEpic(epicEntity);
        }
        finally {
            helper.closeConnection();
        }
    }

    public List<EpicEntity> getAllbyProjectID (Context context, ProjectEntity projectEntity) throws DatabaseException {
        EpicDatabaseHelper helper = null;
        try {
            helper = new EpicDatabaseHelper(context);
            return helper.getAllEpic(projectEntity);
        }
        finally {
            helper.closeConnection();
        }
    }
}
