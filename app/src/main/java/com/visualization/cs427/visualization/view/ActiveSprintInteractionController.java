package com.visualization.cs427.visualization.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toan on 9/7/2016.
 */
public class ActiveSprintInteractionController implements View.OnLongClickListener, View.OnDragListener {

    private Activity activity;
    private List<IssueEntity> issueEntities;
    private List<ContributorEntity> contributorEntities;
    private HashMap<ContributorEntity, List<IssueEntity>> lineHashMap;
    private LinearLayout layoutIssue, layoutContributorPoint, layoutContributorLine;
    private List<LinearLayout> layoutIssues;
    private static final int ISSUE_INDEX_START = 100;
    private static final int LINE_INDEX_START = 200;
    private static final int ISSUE_LINE_INDEX = 10000;
    private boolean moveToLine = false;
    private int from, to;
    private final static String ISSUE_TO_DO = "to_do";
    private final static String ISSUE_LINE = "line";
    private List<LinearLayout> layoutIssueLines;

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
        layoutIssues = new ArrayList<>();
        layoutIssueLines = new ArrayList<>();
        setUpLayoutContributorPoint();
        setUpLayoutIssue();
        setUpLayoutIssueLine();
    }

    private void setUpLayoutIssueLine() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
        for (int i = 0; i < contributorEntities.size(); ++i) {
            ContributorEntity entity = contributorEntities.get(i);
            LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_contributor_line, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvContributorName);
            tvName.setText(entity.getName());
            conLayout.setLayoutParams(params);
            layoutContributorLine.addView(conLayout);
            conLayout.setTag(LINE_INDEX_START + i);
            conLayout.setOnDragListener(this);
            // add issue of the contributor to conLayout
            // ...
            layoutIssueLines.add(conLayout);
        }
    }

    private void setUpLayoutIssue() {
        for (int i = 0; i < issueEntities.size(); ++i) {
            IssueEntity entity = issueEntities.get(i);
            layoutIssues.add(createIssueLayout(entity, ISSUE_INDEX_START + i));
        }
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

    @Override
    public boolean onLongClick(View view) {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
        view.startDrag(null, builder, view, 0);
        from = (int) view.getTag();
        Toast.makeText(activity, "" + from, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED) {
            //
            return true;
        } else if (dragEvent.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
            to = (int) view.getTag();
            if (from < LINE_INDEX_START && to < LINE_INDEX_START) {
                moveBetweenIssue(view, from, to);
                from = to;
                moveToLine = false;
            } else {
                moveToLine = true;
            }
            return true;
        } else if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            if (moveToLine) {
                moveIssueToLine();
            } else {
                int toIndex = 0;
                for (int i = 0; i < layoutIssues.size(); ++i) {
                    if ((int) layoutIssues.get(i).getTag() == to) {
                        toIndex = i;
                    }
                }
                LinearLayout layoutTo = layoutIssues.get(toIndex);
                layoutTo.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }

    private void moveIssueToLine() {
        int fromIndex = 0, toIndex = 0;
        for (int i = 0; i < layoutIssues.size(); ++i) {
            if ((int) layoutIssues.get(i).getTag() == from) {
                fromIndex = i;
                break;
            }
        }
        for (int i = 0; i < layoutIssueLines.size(); ++i) {
            if ((int) layoutIssueLines.get(i).getTag() == to) {
                toIndex = i;
                break;
            }
        }
        LinearLayout layoutContributorLine = layoutIssueLines.get(toIndex);
        LinearLayout layoutIssue = layoutIssues.get(fromIndex);
        LinearLayout layoutIssueLine = (LinearLayout) layoutContributorLine.findViewById(R.id.layoutIssueLine);
        IssueEntity entity = issueEntities.get(fromIndex);
        // add to line
        layoutIssueLine.addView(createIssueLineLayout(entity, lineHashMap.get(contributorEntities.get(toIndex)).size()));
        // remove issue
        LinearLayout parent = (LinearLayout) layoutIssue.getParent();
        parent.removeView(layoutIssue);
        lineHashMap.get(contributorEntities.get(toIndex)).add(entity);
        issueEntities.remove(entity);
        layoutIssues.remove(layoutIssue);
    }

    private void moveBetweenIssue(View v, int from, int to) {
        if (from == to) {
            return;
        }
        int fromIndex = 0, toIndex = 0;
        for (int i = 0; i < layoutIssues.size(); ++i) {
            if ((int) layoutIssues.get(i).getTag() == from) {
                fromIndex = i;
            } else if ((int) layoutIssues.get(i).getTag() == to) {
                toIndex = i;
            }
        }
        LinearLayout layoutFrom = layoutIssues.get(fromIndex);
        LinearLayout layoutTo = layoutIssues.get(toIndex);
        layoutFrom.setVisibility(View.VISIBLE);
        layoutTo.setVisibility(View.INVISIBLE);
        // swap entity
        IssueEntity issueEntity = issueEntities.get(fromIndex);
        issueEntities.set(fromIndex, issueEntities.get(toIndex));
        issueEntities.set(toIndex, issueEntity);
        // animation layout
        TranslateAnimation animation;
        if (from > to) {
            animation = new TranslateAnimation(0, v.getWidth(), 0, 0);
        } else {
            animation = new TranslateAnimation(0, -v.getWidth(), 0, 0);
        }
        animation.setDuration(150);
        animation.setFillAfter(false);
        layoutTo.startAnimation(animation);
        // swap layout
        TextView tvName1 = (TextView) layoutFrom.findViewById(R.id.tvIssueName);
        tvName1.setText(issueEntities.get(fromIndex).getName());
        TextView tvName2 = (TextView) layoutTo.findViewById(R.id.tvIssueName);
        tvName2.setText(issueEntities.get(toIndex).getName());
        Toast.makeText(activity, "" + fromIndex + " to " + toIndex, Toast.LENGTH_SHORT).show();
    }

    private FrameLayout createIssueLineLayout(IssueEntity entity, int tag) {
        FrameLayout conLayout = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.item_issue_line, null);
        TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
        tvName.setText(entity.getName());
        TextView tvEpic = (TextView) conLayout.findViewById(R.id.tvEpicName);
        tvEpic.setText("Epic");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200 + entity.getPoint() * 50, ViewGroup.LayoutParams.MATCH_PARENT);
        conLayout.setLayoutParams(params);
        conLayout.setOnLongClickListener(this);
        conLayout.setOnDragListener(this);
        conLayout.setTag(tag);
        return conLayout;
    }

    private LinearLayout createIssueLayout(IssueEntity entity, int tag) {
        LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_issue, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
        tvName.setText(entity.getName());
        conLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue));
        layoutIssue.addView(conLayout, params);
        conLayout.setTag(tag);
        conLayout.setOnLongClickListener(this);
        conLayout.setOnDragListener(this);
        return conLayout;
    }
}
