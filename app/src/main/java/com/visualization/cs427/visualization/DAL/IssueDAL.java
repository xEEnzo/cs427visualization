package com.visualization.cs427.visualization.DAL;

import android.content.Context;

import com.visualization.cs427.visualization.DatabaseHelper.IssueDatabaseHelper;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

import java.util.List;

/**
 * Created by linhtnvo on 9/8/2016.
 */
public class IssueDAL {
    private static  IssueDAL _instance;
    public static IssueDAL getInstance() {
        if (_instance == null) {
            _instance = new IssueDAL();
        }
        return _instance;
    }

    public List<IssueEntity> getIssuebyProject (Context context, String projectID) throws DatabaseException {
        IssueDatabaseHelper helper = null;
        try {
            helper = new IssueDatabaseHelper(context);
            return helper.getIssueByProjectID(projectID);
        }
        finally {
            helper.closeConnection();
        }
    }
}
