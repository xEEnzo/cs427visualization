package com.visualization.cs427.visualization.Utils;

import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;

import java.util.List;

/**
 * Created by manything on 9/9/16.
 */
public class CurrentProject {
    private static  CurrentProject _instance;
    private List<IssueEntity> issueEntities;
    private List<EpicEntity> epicEntities;

    public List<EpicEntity> getEpicEntities() {
        return epicEntities;
    }

    public void setEpicEntities(List<EpicEntity> epicEntities) {
        this.epicEntities = epicEntities;
    }

    private ProjectEntity projectEntity;
    public static CurrentProject getInstance() {
        if (_instance == null) {
            _instance = new CurrentProject();
        }
        return _instance;
    }

    public List<IssueEntity> getIssueEntities() {
        return issueEntities;
    }

    public void setIssueEntities(List<IssueEntity> issueEntities) {
        this.issueEntities = issueEntities;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public int getIssueIndex(IssueEntity issueEntity) {
        for (int i = 0; i < issueEntities.size(); ++i) {
            if (issueEntity.getId().equals(issueEntities.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    public void updateIssue(IssueEntity issueEntity) {
        for (int i = 0; i < issueEntities.size(); ++i) {
            IssueEntity entity = issueEntities.get(i);
            if (entity.getId().equals(issueEntity.getId())) {
                issueEntities.set(i, issueEntity);
                return;
            }
        }
    }
}
