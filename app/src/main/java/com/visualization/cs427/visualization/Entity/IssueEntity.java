package com.visualization.cs427.visualization.Entity;

/**
 * Created by Toan on 9/1/2016.
 */
public class IssueEntity extends Entity {

    public static final int TYPE_STORY = 1;
    public static final int TYPE_TASK = 2;
    public static final int TYPE_BUG = 3;
    public static int STATUS_CODING = 1;
    public static int STATUS_REVIEWING = 2;
    public static int STATUS_TESTING = 3;
    public static int STATUS_DONE = 4;
    public static int LOCATION_BACKLOG = 1;
    public static int LOCATION_SPRINT = 2;
    public static int LOCATION_DONE = 3;

    private int type;
    private String summary;
    private int point;
    private String description;
    private int processStatus;
    private int locationStatus;
    private ContributorEntity assignee;
    private EpicEntity epic;

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
}
