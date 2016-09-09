package com.visualization.cs427.visualization.Utils;

import com.visualization.cs427.visualization.Entity.IssueEntity;

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
}
