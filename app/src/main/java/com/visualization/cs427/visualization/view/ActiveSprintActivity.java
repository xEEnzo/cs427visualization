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

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.R;

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
        // ...
        // -----------------
        // dummy
        ContributorEntity entity1 = new ContributorEntity("1", "Linh Vo");
        ContributorEntity entity2 = new ContributorEntity("2", "Toan Nguyen");
        ContributorEntity entity3 = new ContributorEntity("3", "Tri Quach");
        contributorEntities = new ArrayList<>();
        contributorEntities.add(entity1);
        contributorEntities.add(entity2);
        contributorEntities.add(entity3);
        // ----------------------
        projectEntity = new ProjectEntity("1", "Jira");
        List<IssueEntity> issueEntities = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            issueEntities.add(new IssueEntity("" + i, "Issue" + 1, i));
        }
        projectEntity.setIssueEntities((ArrayList<IssueEntity>) issueEntities);
        // ----------------------
        for (int i = 0; i < 10; ++i) {
            issueEntities.get(i).setPoint(i);
        }
        // ----------------------
        lineHashMap = new HashMap<>();
        for (ContributorEntity entity : contributorEntities) {
            lineHashMap.put(entity, new ArrayList<IssueEntity>());
        }
        // ----------------------
        layoutIssue = (LinearLayout) findViewById(R.id.layoutIssue);
        layoutContributorPoint = (LinearLayout) findViewById(R.id.layoutContributorPoint);
        layoutContributorLine = (LinearLayout) findViewById(R.id.layoutContributorLine);
        controller = new ActiveSprintInteractionController(this, issueEntities, contributorEntities, lineHashMap,
                layoutIssue, layoutContributorPoint, layoutContributorLine);
    }


}
