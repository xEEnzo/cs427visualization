package com.visualization.cs427.visualization.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.visualization.cs427.visualization.DAL.IssueDAL;
import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toan on 9/7/2016.
 */
public class ActiveSprintInteractionController implements View.OnLongClickListener, View.OnDragListener, View.OnClickListener {

    private Activity activity;
    private List<IssueEntity> issueEntities;
    private List<ContributorEntity> contributorEntities;
    private HashMap<ContributorEntity, List<IssueEntity>> lineHashMap;
    private LinearLayout layoutIssue, layoutContributorPoint, layoutContributorLine;
    private List<LinearLayout> layoutIssues;
    private static final int ISSUE_INDEX_START = 100;
    private static final int LINE_INDEX_START = 200;
    private static final int ISSUE_LINE_INDEX = 10000;
    private static final int DUMMY_INDEX = 300;
    private boolean moveToLine = false, moveToBacklog = false, moveBetweenLine = false;
    private int from, to;
    private List<LinearLayout> layoutIssueLines;
    private String timeSpent;
    private IssueEntity currentIssue;

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
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
            for (int j = 0; j < lineHashMap.get(entity).size(); ++j) {
                IssueEntity issueEntity = lineHashMap.get(entity).get(j);
                FrameLayout frameLayout = createIssueLineLayout(issueEntity, ISSUE_LINE_INDEX + i * 100 + j);
                LinearLayout layout = (LinearLayout) conLayout.findViewById(R.id.layoutIssueLine);
                layout.addView(frameLayout);
            }
            // -----------
            layoutIssueLines.add(conLayout);
        }
    }

    private void setUpLayoutIssue() {
        for (int i = 0; i < issueEntities.size(); ++i) {
            IssueEntity entity = issueEntities.get(i);
            LinearLayout layout = createIssueLayout(entity, ISSUE_INDEX_START + i);
            layoutIssues.add(layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutIssue.addView(layout, params);
        }
    }

    private void setUpLayoutContributorPoint() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < contributorEntities.size(); ++i) {
            ContributorEntity entity = contributorEntities.get(i);
            LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_contributor_point, null);
            TextView tvName = (TextView) conLayout.findViewById(R.id.tvContributorName);
            tvName.setText(entity.getName());
            conLayout.setLayoutParams(params);
            layoutContributorPoint.addView(conLayout, i);
        }
        updatePoint();
    }

    @Override
    public boolean onLongClick(View view) {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
        from = (int) view.getTag();
        view.startDrag(null, builder, view, 0);
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED) {
            //
            return true;
        } else if (dragEvent.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
            // remove dummy blank layout if exist
            LinearLayout removeView = null;
            for (int i = 0; i < layoutIssues.size(); ++i) {
                if ((int) layoutIssues.get(i).getTag() == DUMMY_INDEX) {
                    removeView = layoutIssues.get(i);
                }
            }
            if (removeView != null) {
                layoutIssues.remove(removeView);
                layoutIssue.removeView(removeView);
            }
            to = (int) view.getTag();
            highLightLine();
            if (from < LINE_INDEX_START) {
                if (to < LINE_INDEX_START) {
                    moveBetweenIssue(view, from, to);
                    from = to;
                    moveToLine = false;
                    moveToBacklog = false;
                    moveBetweenLine = false;
                } else {
                    moveToLine = true;
                    moveToBacklog = false;
                    moveBetweenLine = false;
                }
            } else {
                if (to < LINE_INDEX_START) {
                    moveToBacklog = true;
                    moveToLine = false;
                    moveBetweenLine = false;
                    // add dummy blank layout
                    int index = to % 100;
                    LinearLayout dummyLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_issue, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
                    TextView tvName = (TextView) dummyLayout.findViewById(R.id.tvIssueName);
                    tvName.setText("");
                    dummyLayout.setTag(DUMMY_INDEX);
                    dummyLayout.setOnDragListener(this);
                    layoutIssue.addView(dummyLayout, index, params);
                    layoutIssues.add(index, dummyLayout);
                } else {
                    moveBetweenLine = true;
                    moveToLine = false;
                    moveToBacklog = false;
                }
            }
            return true;
        } else if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            if (moveToLine) {
                moveIssueToLine();
            } else if (moveToBacklog) {
                moveIssueToBacklog();
            } else if (moveBetweenLine) {
                moveBetweenLine();
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
            // remove dummy blank layout if exist
            LinearLayout removeView = null;
            for (int i = 0; i < layoutIssues.size(); ++i) {
                if ((int) layoutIssues.get(i).getTag() == DUMMY_INDEX) {
                    removeView = layoutIssues.get(i);
                }
            }
            if (removeView != null) {
                layoutIssues.remove(removeView);
                layoutIssue.removeView(removeView);
            }
            return true;
        } else if (dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED) {
            if (moveToBacklog) {
                moveIssueToBacklog();
                moveToBacklog = false;
            }
            removeHighLight();
            updatePoint();
            return true;
        }
        return false;
    }

    private void moveBetweenLine() {
        int fromIndex = (from / 100) % 100;
        int fromLineIndex = from % 100;
        int toIndex;
        if (to >= ISSUE_LINE_INDEX) {
            toIndex = (to / 100) % 100;
        } else {
            toIndex = to % 100;
        }
        if (fromIndex == toIndex) {
            return;
        }
        // build dialog mes
        IssueEntity issueEntity = lineHashMap.get(contributorEntities.get(fromIndex)).get(fromLineIndex);
        StringBuilder builder = new StringBuilder();
        builder.append("Assign ticket " + issueEntity.getName() + "(");
        if (issueEntity.getProcessStatus() == IssueEntity.STATUS_TODO) {
            builder.append("Coding");
        } else if (issueEntity.getProcessStatus() == IssueEntity.STATUS_CODING) {
            builder.append("Reviewing");
        } else if (issueEntity.getProcessStatus() == IssueEntity.STATUS_REVIEWING) {
            builder.append("Testing");
        }
        builder.append(") to ");
        builder.append(contributorEntities.get(toIndex).getName() + "?");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Update issue");
        LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_time_spent, null);
        ((TextView) layout.findViewById(R.id.tvMessage)).setText(builder.toString());
        final EditText edtTime = (EditText) layout.findViewById(R.id.edtTimeSpent);
        edtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                timeSpent = edtTime.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialogBuilder.setView(layout);
        dialogBuilder.setPositiveButton("Yes", updateAndAssignDialogListener);
        dialogBuilder.setNegativeButton("No", updateAndAssignDialogListener);
        dialogBuilder.create().show();
    }

    private void moveIssueToBacklog() {
        // get chosen layouts
        int fromIndex = (from / 100) % 100;
        int toIndex = to % 100;
        int issueLineIndex = from % 100;
        // get entity
        IssueEntity entity = lineHashMap.get(contributorEntities.get(fromIndex)).get(issueLineIndex);
        // cannot move issue to backlog if not in coding
        if (entity.getProcessStatus() != IssueEntity.STATUS_CODING) {
            // remove dummy blank layout if exist
            LinearLayout removeView = null;
            for (int i = 0; i < layoutIssues.size(); ++i) {
                if ((int) layoutIssues.get(i).getTag() == DUMMY_INDEX) {
                    removeView = layoutIssues.get(i);
                }
            }
            if (removeView != null) {
                layoutIssues.remove(removeView);
                layoutIssue.removeView(removeView);
            }
            return;
        }
        entity.setProcessStatus(IssueEntity.STATUS_TODO);
        // update database
        try {
            IssueDAL.getInstance().updateStatus(activity, entity, entity.getAssignee(), "");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        // remove dummy blank layout if exist
        LinearLayout removeView = null;
        for (int i = 0; i < layoutIssues.size(); ++i) {
            if ((int) layoutIssues.get(i).getTag() == DUMMY_INDEX) {
                removeView = layoutIssues.get(i);
            }
        }
        if (removeView != null) {
            layoutIssues.remove(removeView);
            layoutIssue.removeView(removeView);
        }
        // move entity to backlog
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout moveIssue = createIssueLayout(entity, ISSUE_INDEX_START + toIndex);
        layoutIssue.addView(moveIssue, toIndex, params);
        layoutIssues.add(toIndex, moveIssue);
        // update tag position
        for (int i = toIndex + 1; i < layoutIssues.size(); ++i) {
            layoutIssues.get(i).setTag((int) layoutIssues.get(i).getTag() + 1);
        }
        // remove view on line
        LinearLayout layout = (LinearLayout) layoutIssueLines.get(fromIndex).findViewById(R.id.layoutIssueLine);
        layout.removeViewAt(issueLineIndex);
        // remove entity on line
        lineHashMap.get(contributorEntities.get(fromIndex)).remove(issueLineIndex);
        // add enity on backlog
        issueEntities.add(toIndex, entity);
        // update tag on line
        for (int i = issueLineIndex; i < lineHashMap.get(contributorEntities.get(fromIndex)).size(); i++) {
            layout.getChildAt(i).setTag((int) layout.getChildAt(i).getTag() - 1);
        }
    }

    private void moveIssueToLine() {
        int fromIndex;
        if (from >= ISSUE_LINE_INDEX) {
            fromIndex = (from / 100) % 100;
        } else {
            fromIndex = from % 100;
        }
        int toIndex;
        if (to >= ISSUE_LINE_INDEX) {
            toIndex = (to / 100) % 100;
        } else {
            toIndex = to % 100;
        }
        IssueEntity entity = issueEntities.get(fromIndex);
        // check re-assign
        if (entity.getAssignee() != null && !entity.getAssignee().getId().equals(contributorEntities.get(toIndex).getId())) {
            // show re-assign dialog
            StringBuilder builder = new StringBuilder();
            builder.append("This issue was originally assigned to ");
            builder.append(entity.getAssignee().getName());
            builder.append(". Do you want to re-assign this to ");
            builder.append(contributorEntities.get(toIndex).getName() + "?");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder.setTitle("Update issue");
            dialogBuilder.setMessage(builder.toString());
            dialogBuilder.setPositiveButton("Yes", reAssignDialogListener);
            dialogBuilder.setNegativeButton("No", reAssignDialogListener);
            dialogBuilder.create().show();
        } else {
            entity.setProcessStatus(IssueEntity.STATUS_CODING);
            // update database
            try {
                IssueDAL.getInstance().updateStatus(activity, entity, contributorEntities.get(toIndex), "");
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            LinearLayout layoutContributorLine = layoutIssueLines.get(toIndex);
            LinearLayout layoutIssue = layoutIssues.get(fromIndex);
            LinearLayout layoutIssueLine = (LinearLayout) layoutContributorLine.findViewById(R.id.layoutIssueLine);
            // add to line
            FrameLayout layout = createIssueLineLayout(entity, ISSUE_LINE_INDEX + toIndex * 100 + lineHashMap.get(contributorEntities.get(toIndex)).size());
            layoutIssueLine.addView(layout);
            // remove issue
            LinearLayout parent = (LinearLayout) layoutIssue.getParent();
            parent.removeView(layoutIssue);
            lineHashMap.get(contributorEntities.get(toIndex)).add(entity);
            issueEntities.remove(entity);
            layoutIssues.remove(layoutIssue);
            // update tag
            for (int i = fromIndex; i < layoutIssues.size(); ++i) {
                layoutIssues.get(i).setTag((int) layoutIssues.get(i).getTag() - 1);
            }
        }
        updatePoint();
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
        LinearLayout layout1 = (LinearLayout) layoutFrom.findViewById(R.id.layoutBackground);
        layout1.setBackgroundColor(ContextCompat.getColor(activity, issueEntities.get(fromIndex).getColorIDfromType()));
        TextView tvName2 = (TextView) layoutTo.findViewById(R.id.tvIssueName);
        tvName2.setText(issueEntities.get(toIndex).getName());
        LinearLayout layout2 = (LinearLayout) layoutTo.findViewById(R.id.layoutBackground);
        layout2.setBackgroundColor(ContextCompat.getColor(activity, issueEntities.get(toIndex).getColorIDfromType()));
    }

    private FrameLayout createIssueLineLayout(IssueEntity entity, int tag) {
        FrameLayout conLayout = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.item_issue_line, null);
        TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
        tvName.setText(entity.getName());
        TextView tvEpic = (TextView) conLayout.findViewById(R.id.tvEpicName);
        tvEpic.setText(entity.getEpic().getName());
        tvEpic.setBackgroundColor(Color.parseColor("#" + entity.getEpic().getColorResID()));
        switch (entity.getProcessStatus()) {
            case IssueEntity.STATUS_DONE:
                conLayout.findViewById(R.id.layoutDone).setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
            case IssueEntity.STATUS_TESTING:
                conLayout.findViewById(R.id.layoutTest).setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
            case IssueEntity.STATUS_REVIEWING:
                conLayout.findViewById(R.id.layoutReview).setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
            case IssueEntity.STATUS_CODING:
                conLayout.findViewById(R.id.layoutCoding).setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
        }
        LinearLayout layoutType = (LinearLayout) conLayout.findViewById(R.id.layoutType);
        layoutType.setBackgroundColor(ContextCompat.getColor(activity, entity.getColorIDfromType()));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150 + entity.getPoint() * 100, ViewGroup.LayoutParams.MATCH_PARENT);
        conLayout.setLayoutParams(params);
        conLayout.setOnLongClickListener(this);
        conLayout.setOnDragListener(this);
        conLayout.setTag(tag);
        conLayout.setOnClickListener(this);
        return conLayout;
    }

    private LinearLayout createIssueLayout(IssueEntity entity, int tag) {
        LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_issue, null);
        TextView tvName = (TextView) conLayout.findViewById(R.id.tvIssueName);
        tvName.setText(entity.getName());
        if (entity.getType() == IssueEntity.TYPE_BUG) {
            tvName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        }
        // set background based on type
        LinearLayout layout = (LinearLayout) conLayout.findViewById(R.id.layoutBackground);
        layout.setBackgroundColor(ContextCompat.getColor(activity, entity.getColorIDfromType()));
        conLayout.setTag(tag);
        conLayout.setOnLongClickListener(this);
        conLayout.setOnDragListener(this);
        return conLayout;
    }

    DialogInterface.OnClickListener updateAndAssignDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                int fromIndex = (from / 100) % 100;
                int fromLineIndex = from % 100;
                int toIndex;
                if (to >= ISSUE_LINE_INDEX) {
                    toIndex = (to / 100) % 100;
                } else {
                    toIndex = to % 100;
                }
                // modify entity
                IssueEntity entity = lineHashMap.get(contributorEntities.get(fromIndex)).get(fromLineIndex);
                if (entity.getProcessStatus() == IssueEntity.STATUS_CODING) {
                    entity.setProcessStatus(IssueEntity.STATUS_REVIEWING);
                } else if (entity.getProcessStatus() == IssueEntity.STATUS_REVIEWING) {
                    entity.setProcessStatus(IssueEntity.STATUS_TESTING);
                } else if (entity.getProcessStatus() == IssueEntity.STATUS_TODO) {
                    entity.setProcessStatus(IssueEntity.STATUS_CODING);
                }
                entity.setAssignee(contributorEntities.get(toIndex));
                // update database
                try {
                    IssueDAL.getInstance().updateStatus(activity, entity, contributorEntities.get(toIndex), timeSpent);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                // add to line
                FrameLayout frameLayout = createIssueLineLayout(entity, ISSUE_LINE_INDEX + toIndex * 100 + lineHashMap.get(contributorEntities.get(toIndex)).size());
                ((LinearLayout) layoutIssueLines.get(toIndex).findViewById(R.id.layoutIssueLine)).addView(frameLayout);
                // remove from line
                LinearLayout layout = ((LinearLayout) layoutIssueLines.get(fromIndex).findViewById(R.id.layoutIssueLine));
                layout.removeViewAt(fromLineIndex);
                for (int j = fromLineIndex; j < layout.getChildCount(); ++j) {
                    layout.getChildAt(j).setTag((int) layout.getChildAt(j).getTag() - 1);
                }
                // move entity
                lineHashMap.get(contributorEntities.get(toIndex)).add(entity);
                lineHashMap.get(contributorEntities.get(fromIndex)).remove(fromLineIndex);
                // update point
                updatePoint();
            } else {
                dialogInterface.dismiss();
            }
        }
    };

    private DialogInterface.OnClickListener reAssignDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                int fromIndex;
                if (from >= ISSUE_LINE_INDEX) {
                    fromIndex = (from / 100) % 100;
                } else {
                    fromIndex = from % 100;
                }
                int toIndex;
                if (to >= ISSUE_LINE_INDEX) {
                    toIndex = (to / 100) % 100;
                } else {
                    toIndex = to % 100;
                }
                IssueEntity entity = issueEntities.get(fromIndex);
                entity.setProcessStatus(IssueEntity.STATUS_CODING);
                // update database
                try {
                    IssueDAL.getInstance().updateStatus(activity, entity, contributorEntities.get(toIndex), "");
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                LinearLayout layoutContributorLine = layoutIssueLines.get(toIndex);
                LinearLayout layoutIssue = layoutIssues.get(fromIndex);
                LinearLayout layoutIssueLine = (LinearLayout) layoutContributorLine.findViewById(R.id.layoutIssueLine);
                // add to line
                FrameLayout layout = createIssueLineLayout(entity, ISSUE_LINE_INDEX + toIndex * 100 + lineHashMap.get(contributorEntities.get(toIndex)).size());
                layoutIssueLine.addView(layout);
                // remove issue
                LinearLayout parent = (LinearLayout) layoutIssue.getParent();
                parent.removeView(layoutIssue);
                lineHashMap.get(contributorEntities.get(toIndex)).add(entity);
                issueEntities.remove(entity);
                layoutIssues.remove(layoutIssue);
                // update tag
                for (int j = fromIndex; j < layoutIssues.size(); ++j) {
                    layoutIssues.get(j).setTag((int) layoutIssues.get(j).getTag() - 1);
                }
            } else {
                dialogInterface.dismiss();
            }
        }
    };

    private void highLightLine() {
        int position;
        if (to >= ISSUE_LINE_INDEX) {
            position = (to / 100) % 100;
        } else {
            position = to % 100;
        }
        for (int i = 0; i < layoutIssueLines.size(); ++i) {
            if (i == position && to >= LINE_INDEX_START) {
                layoutIssueLines.get(i).setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_highlight));
            } else {
                layoutIssueLines.get(i).setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            }
        }
    }

    private void removeHighLight() {
        for (int i = 0; i < layoutIssueLines.size(); ++i) {
            layoutIssueLines.get(i).setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        }
    }

    private void updatePoint() {
        List<LinearLayout> pointLayouts = new ArrayList<>();
        List<Integer> points = new ArrayList<>();
        for (int i = 0; i < contributorEntities.size(); ++i) {
            int sum = 0;
            for (IssueEntity entity : issueEntities) {
                if ((entity.getProcessStatus() == IssueEntity.STATUS_TODO || entity.getProcessStatus() == IssueEntity.STATUS_CODING)
                        && entity.getAssignee().getId().equals(contributorEntities.get(i).getId()))
                    sum += entity.getPoint();
            }
            for (IssueEntity entity : lineHashMap.get(contributorEntities.get(i))) {
                if (entity.getProcessStatus() == IssueEntity.STATUS_TODO || entity.getProcessStatus() == IssueEntity.STATUS_CODING) {
                    sum += entity.getPoint();
                }
            }
            LinearLayout layout = (LinearLayout) layoutContributorPoint.getChildAt(i);
            TextView tvPoint = (TextView) layout.findViewById(R.id.tvContributorPoint);
            tvPoint.setText("" + sum);
            LinearLayout highlight = (LinearLayout) layout.findViewById(R.id.layoutPoint);
            pointLayouts.add(highlight);
            points.add(sum);
        }
        // compare and highlight
        int minIndex = 0;
        for (int i = 1; i < points.size(); ++i) {
            if (points.get(i) < points.get(minIndex)) {
                minIndex = i;
            }
        }
        for (int i = 0; i < points.size(); ++i) {
            if (points.get(i) - points.get(minIndex) >= 8) {
                pointLayouts.get(i).setBackgroundColor(ContextCompat.getColor(activity, R.color.red_highlight));
            } else {
                pointLayouts.get(i).setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            }
        }
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        int index = (tag / 100) % 100;
        ContributorEntity contributorEntity = contributorEntities.get(index);
        currentIssue = lineHashMap.get(contributorEntity).get(tag % 100);
        contextDialog().show();
    }

    private Dialog contextDialog() {
        final Dialog dialog = new Dialog(activity);
        LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.menu_context, null);
        TextView tvReassign = (TextView) layout.findViewById(R.id.tvReassign);
        TextView tvUpdate = (TextView) layout.findViewById(R.id.tvUpdate);
        TextView tvSeeDetail = (TextView) layout.findViewById(R.id.tvSeeDetail);
        TextView tvDone = (TextView) layout.findViewById(R.id.tvDone);
        if (currentIssue.getProcessStatus() == IssueEntity.STATUS_TESTING) {
            tvUpdate.setVisibility(View.GONE);
            tvReassign.setVisibility(View.GONE);
        } else {
            tvDone.setVisibility(View.GONE);
        }
        tvReassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contributorDialog(false).show();
                dialog.dismiss();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contributorDialog(true).show();
                dialog.dismiss();
            }
        });
        tvSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to Issue detail

                dialog.dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update to done

                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        return dialog;
    }

    private Dialog contributorDialog(final boolean update) {
        final Dialog dialog = new Dialog(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        final ContributorEntity contributorEntity = currentIssue.getAssignee();
        for (final ContributorEntity entity : contributorEntities) {
            if (!contributorEntity.getId().equals(entity.getId())) {
                final LinearLayout conLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.menu_contributor, null);
                TextView tvContributor = (TextView) conLayout.findViewById(R.id.tvContributorName);
                tvContributor.setText(entity.getName());
                tvContributor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // set to and from global var for update function
                        if (!update) {
                            if (currentIssue.getProcessStatus() == IssueEntity.STATUS_CODING) {
                                currentIssue.setProcessStatus(IssueEntity.STATUS_TODO);
                            } else if (currentIssue.getProcessStatus() == IssueEntity.STATUS_REVIEWING) {
                                currentIssue.setProcessStatus(IssueEntity.STATUS_CODING);
                            } else if (currentIssue.getProcessStatus() == IssueEntity.STATUS_TESTING) {
                                currentIssue.setProcessStatus(IssueEntity.STATUS_REVIEWING);
                            }
                        }
                        to = LINE_INDEX_START + contributorEntities.indexOf(entity);
                        int fromIndex = 0;
                        for (int i = 0; i < contributorEntities.size(); ++i) {
                            if (contributorEntities.get(i).getId().equals(contributorEntity.getId())) {
                                fromIndex = i;
                                break;
                            }
                        }
                        from = ISSUE_LINE_INDEX + fromIndex * 100 + lineHashMap.get(contributorEntities.get(fromIndex)).indexOf(currentIssue);
                        moveBetweenLine();
                        dialog.dismiss();
                    }
                });
                layout.addView(conLayout);
            }
        }
        dialog.setContentView(layout);
        return dialog;
    }
}
