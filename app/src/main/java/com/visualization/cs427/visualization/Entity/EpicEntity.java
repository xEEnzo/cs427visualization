package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class EpicEntity extends Entity {

    private String colorResID;

    public EpicEntity(String id, String name, String colorResID) {
        super(id, name);
        this.colorResID = colorResID;
    }

    public String getColorResID() {
        return colorResID;
    }

    public void setColorResID(String colorResID) {
        this.colorResID = colorResID;
    }
}
