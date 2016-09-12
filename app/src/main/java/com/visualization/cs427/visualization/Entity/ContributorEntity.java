package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class ContributorEntity extends Entity {

    public ContributorEntity(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        ContributorEntity entity = (ContributorEntity) obj;
        return this.getId().equals(entity.getId());
    }
}
