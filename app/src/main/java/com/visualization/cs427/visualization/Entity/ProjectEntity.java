package com.visualization.cs427.visualization.Entity;

import java.util.ArrayList;

/**
 * Created by Toan on 9/1/2016.
 */
public class ProjectEntity extends Entity {

    private ArrayList<IssueEntity> issueEntities;

    public ProjectEntity(String id, String name) {
        super(id, name);
    }

    public ArrayList<IssueEntity> getIssueEntities() {
        return issueEntities;
    }

    public void setIssueEntities(ArrayList<IssueEntity> issueEntities) {
        this.issueEntities = issueEntities;
    }
}
