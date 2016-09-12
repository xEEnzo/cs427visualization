package com.visualization.cs427.visualization.Entity;

import com.visualization.cs427.visualization.R;

import java.util.List;

/**
 * Created by Toan on 9/1/2016.
 */
public class IssueEntity extends Entity {

    public static final int TYPE_STORY = 1;
    public static final int TYPE_TASK = 2;
    public static final int TYPE_BUG = 3;
    public static final int STATUS_TODO = 0;
    public static final int STATUS_CODING = 1;
    public static final int STATUS_REVIEWING = 2;
    public static final int STATUS_TESTING = 3;
    public static final int STATUS_DONE = 4;
    public static final int LOCATION_BACKLOG = 1;
    public static final int LOCATION_SPRINT = 2;
    public static final int LOCATION_DONE = 3;

    private int type;
    private String summary;
    private int point;
    private String description;
    private int processStatus;
    private int locationStatus;
    private ContributorEntity assignee;
    private EpicEntity epic;
    private List<IssueEntity> blocker, blocked;

    public IssueEntity (String id) {
        super(id, null);
    }

    public IssueEntity(String id, String name, int type, String summary, int point, String description, int processStatus, int locationStatus, ContributorEntity assignee, EpicEntity epic) {
        super(id, name);
        this.type = type;
        this.summary = summary;
        this.point = point;
        this.description = description;
        this.processStatus = processStatus;
        this.locationStatus = locationStatus;
        this.assignee = assignee;
        this.epic = epic;
    }

    @Override
    public boolean equals(Object obj) {
        IssueEntity entity = (IssueEntity) obj;
        return this.getId().equals(entity.getId());
    }

    public IssueEntity(String id, String name) {
        super(id, name);
    }

    public IssueEntity(String id, String name, int point) {
        super(id, name);
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }

    public ContributorEntity getAssignee() {
        return assignee;
    }

    public void setAssignee(ContributorEntity assignee) {
        this.assignee = assignee;
    }

    public EpicEntity getEpic() {
        return epic;
    }

    public void setEpic(EpicEntity epic) {
        this.epic = epic;
    }
    
    public Integer getColorIDfromType() {
        switch (type) {
            case TYPE_STORY:
                return R.color.yellow;
            case TYPE_TASK:
                return R.color.blue;
            case TYPE_BUG:
                return R.color.red;
        }
        return null;
    }
    public String getStringType() {
        switch (type){
            case IssueEntity.TYPE_BUG:
                return "Bug";
            case IssueEntity.TYPE_STORY:
                return "Story";
            case IssueEntity.TYPE_TASK:
                return "Task";
            default:
                return "";
        }
    }

    public String getStringStatus(){
        switch (processStatus){
            case IssueEntity.STATUS_CODING:
                return "Coding";
            case IssueEntity.STATUS_REVIEWING:
                return "Reviewing";
            case IssueEntity.STATUS_TESTING:
                return "Testing";
            case IssueEntity.STATUS_DONE:
                return "Done";
            default:
                return "";
        }
    }

    public List<IssueEntity> getBlocker() {
        return blocker;
    }

    public void setBlocker(List<IssueEntity> blocker) {
        this.blocker = blocker;
    }

    public List<IssueEntity> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<IssueEntity> blocked) {
        this.blocked = blocked;
    }
}
