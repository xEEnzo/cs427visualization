package com.visualization.cs427.visualization.DAL;

import android.content.Context;

import com.visualization.cs427.visualization.DatabaseHelper.ProjectDatabaseHelper;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class ProjectDAL {
    private static  ProjectDAL _instance;
    public static ProjectDAL getInstance() {
        if (_instance == null) {
            _instance = new ProjectDAL();
        }
        return _instance;
    }

    public List<ProjectEntity> getAllProject(Context context) throws DatabaseException {
        ProjectDatabaseHelper dbh = null;
        dbh = new ProjectDatabaseHelper(context);
        return dbh.getAll();
    }
}
