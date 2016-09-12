package com.visualization.cs427.visualization.Utils;

import android.content.Context;
import com.visualization.cs427.visualization.Entity.ContributorEntity;

import com.visualization.cs427.visualization.DAL.IssueDAL;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manything on 9/9/16.
 */
public class DataUtils {
    public static IssueEntity findIssueByID (List<IssueEntity> issueEntities, String id){
        for (IssueEntity entity : issueEntities){
            if (entity.getId().equals(id)){
                return entity;
            }
        }
        return null;
    }

    public static List<IssueEntity> getIssueInBackLog (List<IssueEntity> issueEntities){
        List<IssueEntity> entities = new ArrayList<>();
        for (IssueEntity entity : issueEntities){
            if (entity.getLocationStatus() == IssueEntity.LOCATION_BACKLOG){
                entities.add(entity);
            }
        }
        return entities;
    }

    public static List<IssueEntity> getIssueInSprint (List<IssueEntity> issueEntities){
        List<IssueEntity> entities = new ArrayList<>();
        for (IssueEntity entity : issueEntities){
            if (entity.getLocationStatus() == IssueEntity.LOCATION_SPRINT){
                entities.add(entity);
            }
        }
        return entities;
    }

    public static List<ContributorEntity> getAllContributors (List<IssueEntity> issueEntities){
        List<ContributorEntity> contributorEntities = new ArrayList<>();
        for (IssueEntity entity : issueEntities){
            if (!contributorEntities.contains(entity.getAssignee())) {
                contributorEntities.add(entity.getAssignee());
            }
    }
    public static List<IssueEntity> orderIssueByTimeLog(Context context, List<IssueEntity> issueEntities) throws DatabaseException {
        List<Timestamp> timestampList = new ArrayList<>();
        for (IssueEntity entity : issueEntities) {
            timestampList.add(IssueDAL.getInstance().getTimeAssigned(context, entity, entity.getProcessStatus()));
        }
        for (int i = timestampList.size() - 1; i > 0; --i) {
            int max = 0;
            for (int j = 1; j <= i; ++j) {
                if (timestampList.get(j).after(timestampList.get(max))) {
                    max = j;
                }
            }
            if (max != 0) {
                Timestamp tmp = timestampList.get(i);
                timestampList.set(i, timestampList.get(max));
                timestampList.set(max, tmp);
                IssueEntity issueEntity = issueEntities.get(i);
                issueEntities.set(i, issueEntities.get(max));
                issueEntities.set(max, issueEntity);
            }
        }
        return issueEntities;
    }
}
