package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class Entity {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
