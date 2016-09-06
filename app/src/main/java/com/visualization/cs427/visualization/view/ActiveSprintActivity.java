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
import java.util.List;

public class ActiveSprintActivity extends AppCompatActivity {

    private List<ContributorEntity> contributorEntities;
    private ProjectEntity projectEntity;

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
        setUpLayoutContributorPoint();
        setUpLayoutIssue();
    }

    private void setUpLayoutIssue() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutIssue);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
        for (IssueEntity entity : projectEntity.getIssueEntities()) {
            LinearLayout conLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_issue, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
            tvName.setText(entity.getName());
            conLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            layout.addView(conLayout, params);
        }
        /*
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (layout.getParent() == null){
            horizontalScrollView.addView(layout, params1);
            return;
        }
        ((ViewGroup)layout.getParent()).removeView(layout);
        horizontalScrollView.addView(layout);
        ((ViewGroup)layout.getParent()).addView(horizontalScrollView);*/
    }

    private void setUpLayoutContributorPoint() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutContributorPoint);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (ContributorEntity entity : contributorEntities) {
            LinearLayout conLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_contributor_point, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvContributorName);
            tvName.setText(entity.getName());
            TextView tvPoint = (TextView) conLayout.findViewById(R.id.tvContributorPoint);
            tvPoint.setText("88");
            conLayout.setLayoutParams(params);
            layout.addView(conLayout);
        }
    }
}
