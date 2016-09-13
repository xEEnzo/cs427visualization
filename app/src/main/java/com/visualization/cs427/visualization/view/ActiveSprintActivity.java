package com.visualization.cs427.visualization.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.visualization.cs427.visualization.DAL.IssueDAL;
import com.visualization.cs427.visualization.DAL.ProjectDAL;
import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;
import com.visualization.cs427.visualization.Utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveSprintActivity extends AppCompatActivity {

    private List<ContributorEntity> contributorEntities;
    private ProjectEntity projectEntity;
    private LinearLayout layoutIssue, layoutContributorPoint, layoutContributorLine;
    private ActiveSprintInteractionController controller;
    private HashMap<ContributorEntity, List<IssueEntity>> lineHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_sprint);
        // get from data
        try {
            projectEntity = CurrentProject.getInstance().getProjectEntity();
            List<IssueEntity> issueEntitiesAll = IssueDAL.getInstance().getIssuebyProject(this, projectEntity.getId());
            projectEntity.setIssueEntities((ArrayList<IssueEntity>) issueEntitiesAll);
            List<IssueEntity> issueEntities = new ArrayList<>();
            for (IssueEntity issueEntity : issueEntitiesAll) {
                if (issueEntity.getLocationStatus() == IssueEntity.LOCATION_SPRINT) {
                    issueEntities.add(issueEntity);
                }
            }
            contributorEntities = new ArrayList<>();
            for (IssueEntity entity : issueEntitiesAll) {
                addContributorToList(entity.getAssignee(), contributorEntities);
            }
            lineHashMap = new HashMap<>();
            for (ContributorEntity entity : contributorEntities) {
                List<IssueEntity> issueEntitiesContributor = new ArrayList<>();
                List<IssueEntity> removeList = new ArrayList<>();
                for (int i = 0; i < issueEntities.size(); i++) {
                    IssueEntity issueEntity = issueEntities.get(i);
                    if (issueEntity.getAssignee().getId().equals(entity.getId()) && issueEntity.getProcessStatus() != IssueEntity.STATUS_TODO) {
                        issueEntitiesContributor.add(issueEntity);
                        removeList.add(issueEntity);
                    }
                }
                issueEntities.removeAll(removeList);
                issueEntitiesContributor = DataUtils.orderIssueByTimeLog(this, issueEntitiesContributor);
                lineHashMap.put(entity, issueEntitiesContributor);
            }
            layoutIssue = (LinearLayout) findViewById(R.id.layoutIssue);
            layoutContributorPoint = (LinearLayout) findViewById(R.id.layoutContributorPoint);
            layoutContributorLine = (LinearLayout) findViewById(R.id.layoutContributorIssue);
            controller = new ActiveSprintInteractionController(this, issueEntities, contributorEntities, lineHashMap,
                    layoutIssue, layoutContributorPoint, layoutContributorLine);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void addContributorToList(ContributorEntity entity, List<ContributorEntity> contributorEntities) {
        if (entity == null) {
            return;
        }
        for (ContributorEntity contributorEntity : contributorEntities) {
            if (contributorEntity.getId().equals(entity.getId())) {
                return;
            }
        }
        contributorEntities.add(entity);
    }

    @Override
    protected void onStop() {
        try {
            CurrentProject.getInstance().setIssueEntities(IssueDAL.getInstance().getIssuebyProject(this, CurrentProject.getInstance().getProjectEntity().getId()));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
