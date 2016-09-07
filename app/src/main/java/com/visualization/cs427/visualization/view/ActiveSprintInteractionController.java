package com.visualization.cs427.visualization.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Toan on 9/7/2016.
 */
public class ActiveSprintInteractionController {

    private Activity activity;
    private List<IssueEntity> issueEntities;
    private List<ContributorEntity> contributorEntities;
    private HashMap<ContributorEntity, List<IssueEntity>> lineHashMap;
    private LinearLayout layoutIssue, layoutContributorPoint, layoutContributorLine;

    public ActiveSprintInteractionController(Activity activity, List<IssueEntity> issueEntities, List<ContributorEntity> contributorEntities,
                                             HashMap<ContributorEntity, List<IssueEntity>> lineHashMap, LinearLayout layoutIssue,
                                             LinearLayout layoutContributorPoint, LinearLayout layoutContributorLine) {
        this.activity = activity;
        this.issueEntities = issueEntities;
        this.contributorEntities = contributorEntities;
        this.lineHashMap = lineHashMap;
        this.layoutIssue = layoutIssue;
        this.layoutContributorPoint = layoutContributorPoint;
        this.layoutContributorLine = layoutContributorLine;
        setUpLayoutContributorPoint();
        setUpLayoutIssue();
        setUpLayoutIssueLine();
    }

    private void setUpLayoutIssueLine() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        for (ContributorEntity entity : contributorEntities) {
            LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_contributor_line, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvContributorName);
            tvName.setText(entity.getName());
            conLayout.setLayoutParams(params);
            layoutContributorLine.addView(conLayout);
        }
    }

    private void setUpLayoutIssue() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
        for (IssueEntity entity : issueEntities) {
            LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_issue, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
            tvName.setText(entity.getName());
            conLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue));
            layoutIssue.addView(conLayout, params);
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (ContributorEntity entity : contributorEntities) {
            LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_contributor_point, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvContributorName);
            tvName.setText(entity.getName());
            TextView tvPoint = (TextView) conLayout.findViewById(R.id.tvContributorPoint);
            tvPoint.setText("88");
            conLayout.setLayoutParams(params);
            layoutContributorPoint.addView(conLayout);
        }
    }

}
