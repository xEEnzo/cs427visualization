package com.visualization.cs427.visualization.view;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SprintDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener {
    private TextView txtSprint, txtCreateissue;
    private LinearLayout layoutSprint, layoutBacklog, layoutAll;
    private List<IssueEntity> issueEntities = new ArrayList<>();
    private List<View> viewListBacklog;
    private List<View> viewListSprint;
    private LayoutInflater inflater;
    private TextView txtEmpty, txtNumIssues;
    private View.OnDragListener onContainerDragListener;
    private ScrollView scrollView;
    private int screenHeight;
    private boolean onDragging = false;
    private int selectedIndex;
    private boolean isInBackLog;
    private boolean emptyLayoutInBackLog = false;
    private boolean emptyLayoutInSprint = false;
    private boolean isChangeBackToLog = false;
    private View.OnLongClickListener sprintIssueLongCLick;
    private boolean rightDrop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
        setUpOnContainerDrag();
        setUpSprintIssueLongClick();
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        txtSprint = (TextView) findViewById(R.id.txtSprint);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        txtNumIssues = (TextView) findViewById(R.id.txtNumIssues);
        txtCreateissue = (TextView) findViewById(R.id.txtCreateIssue);
        txtCreateissue.setOnClickListener(this);
        layoutAll = (LinearLayout) findViewById(R.id.layoutAll);
        layoutBacklog = initLayout(R.id.layoutBacklog);
        layoutSprint = initLayout(R.id.layoutSprint);
        initDummyData();
        getScreenHeight();
        createBackLog();
        createSprint();
        setUpScrollView();
    }

    private void setUpOnContainerDrag() {
        onContainerDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("nhatlinh95", "startDraginContainer");
                        changeContainer(view.getId());
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("nhatlinh95", "onExitContainer");
                        changeContainer(view.getId());
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        autoScroll(view, dragEvent);
                        break;
                    case DragEvent.ACTION_DROP:
                        rightDrop = true;
                        dragDrop(view, dragEvent);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        onDragging = false;
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

    private void setUpSprintIssueLongClick(){
        sprintIssueLongCLick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                selectedIndex = (int) view.getTag(R.string.issue_position);
                isInBackLog = false;
                return true;
            }
        };
    }

    private void changeContainer(int id) {
        if (isInBackLog && id == R.id.layoutSprint) {
            selectedIndex = layoutSprint.getChildCount();
            isInBackLog = false;
            return;
        }
        if (!isInBackLog && id == R.id.layoutBacklog) {
            selectedIndex = 0;
            isInBackLog = true;
            isChangeBackToLog = true;
            return;
        }
    }


    private void setUpScrollView() {
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup parent = (ViewGroup) layoutAll.getParent();
        if (parent != null) {
            parent.removeView(layoutAll);
            scrollView.addView(layoutAll);
            parent.addView(scrollView);
        } else {
            scrollView.addView(layoutAll);
        }
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onDragging;
            }
        });
    }

    private LinearLayout initLayout(int id) {
        LinearLayout layout = (LinearLayout) findViewById(id);
        layout.setOnDragListener(onContainerDragListener);
        return layout;
    }

    private void createBackLog() {
        viewListBacklog = new ArrayList<>();
        for (int i = 0; i < issueEntities.size(); ++i) {
            View view = createIssueView(issueEntities.get(i));
            view.setTag(R.string.issue_position, i);
            viewListBacklog.add(view);
            view.setOnDragListener(this);
            layoutBacklog.addView(view);
        }
    }

    private void createSprint(){
        viewListSprint = new ArrayList<>();
    }

    private void initDummyData() {
        for (int i = 0; i < 10; ++i) {
            String name = "Issue" + i;
            IssueEntity entity = new IssueEntity(String.valueOf(i), name, i % 3, null, 0, null, -1, -1);
            issueEntities.add(entity);
        }
    }

    private View createIssueView(IssueEntity issueEntity) {
        View root = inflater.inflate(R.layout.issue_layout_view, null);
        ViewHolder holder = new ViewHolder(root);
        holder.txtIssueName.setText(issueEntity.getName());
        Integer issuetypeID = getIssueTypeID(issueEntity.getType());
        if (issuetypeID != null) {
            holder.ivIssueType.setBackgroundResource(issuetypeID);
        }
        root.setOnDragListener(this);
        root.setOnLongClickListener(this);
        root.setTag(R.string.issue_id, issueEntity.getId());
        root.setTag(R.string.issue_location, IssueEntity.LOCATION_BACKLOG);
        return root;
    }

    private Integer getIssueTypeID(int type) {
        switch (type) {
            case IssueEntity.TYPE_TASK:
                return R.drawable.circle_issue_task;
            case IssueEntity.TYPE_STORY:
                return R.drawable.circle_issue_story;
            case IssueEntity.TYPE_BUG:
                return R.drawable.circle_issue_bug;
            default:
                return null;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtCreateIssue )
        {
            Intent intent = new Intent(this,CreateIssueActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int position = (int) v.getTag(R.string.issue_position);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                onDragging = true;
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                View dragging = (View) event.getLocalState();
                int fromLocation = (int) dragging.getTag(R.string.issue_location);
                LinearLayout layout = null;
                List<View> viewList = null;
                if (isInBackLog) {
                    layout = layoutBacklog;
                    viewList = viewListBacklog;
                } else {
                    layout = layoutSprint;
                    viewList = viewListSprint;
                }
                if (selectedIndex >= viewList.size()){
                    changeFromBackLogToSprint(layout, viewList, v);
                }
                if (fromLocation == IssueEntity.LOCATION_SPRINT && selectedIndex == 0 && isChangeBackToLog){
                    changeFromSprintToBackLog(layout, viewList, v);
                    for (View view:viewList){
                        deleteParent(view);
                        layout.addView(view);
                    }
                    isChangeBackToLog = false;
                    return true;
                }
                viewList.get(selectedIndex).setTag(R.string.issue_position, position);
                viewList.get(position).setTag(R.string.issue_position, selectedIndex);
                Collections.swap(viewList,selectedIndex, position);
                layout.removeAllViews();
                for (View view:viewList){
                    deleteParent(view);
                    layout.addView(view);
                }
                selectedIndex = position;
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                autoScroll(v, event);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                checkDropIntoEmptySpace(v);
                onDragging = false;
                rightDrop = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void checkDropIntoEmptySpace(View dropped){
        if (rightDrop){
            return;
        }
        layoutSprint.removeAllViews();
        for (View v : viewListSprint){
            if (v.getVisibility() == View.INVISIBLE){
                v.setVisibility(View.VISIBLE);
            }
            layoutSprint.addView(v);
        }
        layoutBacklog.removeAllViews();
        for (View v : viewListBacklog){
            if (v.getVisibility() == View.INVISIBLE){
                v.setVisibility(View.VISIBLE);
            }
            layoutBacklog.addView(v);
        }
        setUpWhenContainerEmpty(layoutSprint);
        setUpWhenContainerEmpty(layoutBacklog);
    }

    private void changeFromSprintToBackLog(LinearLayout layout, List<View> viewList, View v) {
        LinearLayout layoutTmp = new LinearLayout(SprintDetailActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v.getLayoutParams().width, v.getHeight());
        layoutTmp.setLayoutParams(params);
        layout.addView(layoutTmp, 0);
        layoutTmp.setVisibility(View.INVISIBLE);
        layoutTmp.setTag(R.string.issue_position, 0);
        viewList.add(0, layoutTmp);
        emptyLayoutInBackLog = true;
        for (int i=1; i<viewList.size(); ++i){
            View view = viewList.get(i);
            int oldPos = (int) view.getTag(R.string.issue_position);
            view.setTag(R.string.issue_position, oldPos+1);
        }
    }

    private void deleteParent (View v){
        if (v == null){
            return;
        }
        if (v.getParent() != null){
            ((ViewGroup)v.getParent()).removeView(v);
        }
    }

    private void changeFromBackLogToSprint(LinearLayout layout, List<View> viewList, View v){
        LinearLayout layoutTmp = new LinearLayout(SprintDetailActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v.getLayoutParams().width, v.getHeight());
        layoutTmp.setLayoutParams(params);
        layout.addView(layoutTmp);
        layoutTmp.setVisibility(View.INVISIBLE);
        viewList.add(layoutTmp);
        emptyLayoutInSprint = true;
    }

    private void dragDrop(View v, DragEvent event) {
        View dropped = (View) event.getLocalState();
        int issueLocation = (int) dropped.getTag(R.string.issue_location);
        int oldPosofDropped = (int) dropped.getTag(R.string.issue_position);
        String id = (String) dropped.getTag();
        // update database
         if (dropped.getVisibility() == View.INVISIBLE){
            dropped.setVisibility(View.VISIBLE);
        }
        deleteParent(dropped);
         if (isInBackLog){
            dropped.setTag(R.string.issue_location, IssueEntity.LOCATION_BACKLOG);
            if (emptyLayoutInBackLog){
                layoutBacklog.removeViewAt(selectedIndex);
                viewListBacklog.remove(selectedIndex);
                emptyLayoutInBackLog = false;
            }
            if (emptyLayoutInSprint){
                layoutSprint.removeViewAt(layoutSprint.getChildCount()-1);
                viewListSprint.remove(viewListSprint.size()-1);
                emptyLayoutInSprint = false;
            }
            addViewToContainer(dropped, layoutBacklog);
            if (issueLocation != IssueEntity.LOCATION_BACKLOG){
                viewListSprint.remove(dropped);
                layoutSprint.removeView(dropped);
                addViewToList(viewListBacklog, dropped);
                for (int i = oldPosofDropped+1; i<viewListSprint.size(); ++i){
                    View view = viewListSprint.get(i);
                    int oldPos = (int) view.getTag(R.string.issue_position);
                    view.setTag(R.string.issue_position, oldPos-1);
                }
            }
             setUpWhenContainerEmpty(layoutSprint);
        }
        else {
            dropped.setTag(R.string.issue_location, IssueEntity.LOCATION_SPRINT);
            dropped.setOnLongClickListener(sprintIssueLongCLick);
            if (layoutSprint.getChildCount()==1){
                layoutSprint.removeAllViews();
                layoutSprint.setBackgroundResource(R.drawable.border);
            }
            if (emptyLayoutInSprint){
                layoutSprint.removeViewAt(selectedIndex);
                viewListSprint.remove(selectedIndex);
                emptyLayoutInSprint = false;
            }
            if (emptyLayoutInBackLog){
                layoutBacklog.removeViewAt(0);
                viewListSprint.remove(0);
                for (View view :viewListSprint){
                    int oldPos = (int) view.getTag(R.string.issue_position);
                    view.setTag(R.string.issue_position, oldPos-1);
                }
                emptyLayoutInBackLog = false;
            }
            addViewToContainer(dropped, layoutSprint);
            if (issueLocation != IssueEntity.LOCATION_SPRINT){
                viewListBacklog.remove(dropped);
                layoutBacklog.removeView(dropped);
                addViewToList(viewListSprint, dropped);
                for (View view : viewListBacklog){
                    int oldPos = (int) view.getTag(R.string.issue_position);
                    view.setTag(R.string.issue_position, oldPos-1);
                }
            }
             setUpWhenContainerEmpty(layoutSprint);
        }
        onDragging = false;

    }
    private void setUpWhenContainerEmpty(LinearLayout container){
        if (container.getChildCount() != 0){
            return;
        }
        deleteParent(txtEmpty);
        container.setBackground(null);
        container.addView(txtEmpty);
    }
    private void addViewToContainer(View dropped, LinearLayout container){
        if (selectedIndex >= container.getChildCount()){
            dropped.setTag(R.string.issue_position, container.getChildCount());
            container.addView(dropped);
        }
        else {
            dropped.setTag(R.string.issue_position, selectedIndex);
            container.addView(dropped, selectedIndex);
        }
    }

    private void addViewToList (List<View> viewList, View dropped){
        if (viewList.size() < selectedIndex){
            dropped.setTag(R.string.issue_position, viewList.size());
            viewList.add(dropped);
        }
        else {
            dropped.setTag(R.string.issue_position, selectedIndex);
            viewList.add(selectedIndex, dropped);
        }
        setNumIssues(viewList);
    }

    private void setNumIssues(List<View> viewLists){
        int size = viewLists.size();
        if (size == 0){
            txtNumIssues.setVisibility(View.INVISIBLE);
            return;
        }
        txtNumIssues.setVisibility(View.VISIBLE);
        if (size == 1){
            txtNumIssues.setText(size + " issue");
            return;
        }
        txtNumIssues.setText(size + " issues");
    }

    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        selectedIndex = (int) view.getTag(R.string.issue_position);
        isInBackLog = true;
        return true;
    }


    private class ViewHolder {
        private TextView txtIssueName, txtEpicName, txtContributor;
        private ImageView ivIssueType;

        public ViewHolder(View root) {
            txtIssueName = (TextView) root.findViewById(R.id.txtIssueName);
            txtContributor = (TextView) root.findViewById(R.id.txtContributor);
            txtEpicName = (TextView) root.findViewById(R.id.txtEpicName);
            ivIssueType = (ImageView) root.findViewById(R.id.ivIssueType);

        }
    }

    public Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

    private void autoScroll(View v, DragEvent event) {
        Point touchPosition = getTouchPositionFromDragEvent(v, event);
        if (screenHeight - touchPosition.y < 200) {
            scrollView.smoothScrollBy(0, 15);
            return;
        }
        if (touchPosition.y > 200 && touchPosition.y < 700) {
            scrollView.smoothScrollBy(0, -15);
        }
    }

    public void getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
    }
}