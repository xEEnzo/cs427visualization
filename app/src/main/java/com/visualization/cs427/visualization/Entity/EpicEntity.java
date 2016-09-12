package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class EpicEntity extends Entity {

    private String colorResID;
    private ProjectEntity projectEntity;

    public EpicEntity(String id, String name, String colorResID, ProjectEntity projectEntity) {
        super(id, name);
        this.colorResID = colorResID;
        this.projectEntity = projectEntity;
    }

    public String getColorResID() {
        return colorResID;
    }

    public void setColorResID(String colorResID) {
        this.colorResID = colorResID;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }
}
