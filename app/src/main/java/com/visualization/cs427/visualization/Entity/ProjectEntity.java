package com.visualization.cs427.visualization.Entity;

import java.util.ArrayList;

/**
 * Created by Toan on 9/1/2016.
 */
public class ProjectEntity extends Entity {

    private ArrayList<IssueEntity> issueEntities;

    public ArrayList<IssueEntity> getIssueEntities() {
        return issueEntities;
    }

    public void setIssueEntities(ArrayList<IssueEntity> issueEntities) {
        this.issueEntities = issueEntities;
    }
}
