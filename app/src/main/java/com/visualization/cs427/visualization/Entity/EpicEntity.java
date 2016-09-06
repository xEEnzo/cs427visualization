package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class EpicEntity extends Entity {

    private int colorResID;

    public EpicEntity(String id, String name) {
        super(id, name);
    }

    public int getColorResID() {
        return colorResID;
    }

    public void setColorResID(int colorResID) {
        this.colorResID = colorResID;
    }
}
