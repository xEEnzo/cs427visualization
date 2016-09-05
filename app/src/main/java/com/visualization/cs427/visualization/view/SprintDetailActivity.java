package com.visualization.cs427.visualization.view;

import android.app.ActionBar;
import android.content.ClipData;
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
import android.view.animation.TranslateAnimation;
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
    private int heightofElement;
    private View.OnDragListener onContainerDragListener;
    private ScrollView scrollView;
    private int screenHeight;
    private boolean onDragging = false;
    private int previousIndex;
    private int selectedIndex;
    private boolean isInBackLog;
    private int oldX, oldY, diffPosX, diffPosY, posX, posY;
    private View tmpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
        setUpOnContainerDrag();
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        txtSprint = (TextView) findViewById(R.id.txtSprint);
        txtCreateissue = (TextView) findViewById(R.id.txtCreateIssue);
        txtCreateissue.setOnClickListener(this);
        layoutAll = (LinearLayout) findViewById(R.id.layoutAll);
        layoutBacklog = initLayout(R.id.layoutBacklog);
        layoutSprint = initLayout(R.id.layoutSprint);
        initDummyData();
        getScreenHeight();
        createBackLog();
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
                        ((LinearLayout) view).removeView(tmpView);
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        autoScroll(view, dragEvent);
                        break;
                    case DragEvent.ACTION_DROP:
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

    private void changeContainer(int id) {
        if (isInBackLog && id == R.id.layoutSprint) {
            selectedIndex = layoutSprint.getChildCount() + 1;
            isInBackLog = false;
            return;
        }
        if (!isInBackLog && id == R.id.layoutBacklog) {
            selectedIndex = 0;
            isInBackLog = true;
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
            if (i == 0) {
                view.setBackgroundResource(R.drawable.border_top);
            } else {
                view.setBackgroundResource(R.drawable.border_middle);
            }
            view.setOnDragListener(this);
            layoutBacklog.addView(view);
        }
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
        root.setTag(R.string.issue_location,0);
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

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int position = (int) v.getTag(R.string.issue_position);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                onDragging = true;
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                LinearLayout layout = null;
                List<View> viewList = null;
                if (isInBackLog) {
                    layout = layoutBacklog;
                    viewList = viewListBacklog;
                } else {
                    layout = layoutSprint;
                    viewList = viewListSprint;
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
                onDragging = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void deleteParent (View v){
        if (v.getParent() != null){
            ((ViewGroup)v.getParent()).removeView(v);
        }
    }

    private void dragDrop(View v, DragEvent event) {
        View dropped = (View) event.getLocalState();
        String id = (String) dropped.getTag();
        // update database
        deleteParent(dropped);
        if (isInBackLog){
            int location = (int) dropped.getTag(R.string.issue_location);
            if (location == 1){
                layoutBacklog.addView(dropped, selectedIndex);
            }
            else{
                viewListBacklog.get(selectedIndex).setVisibility(View.VISIBLE);
            }
        }
        onDragging = false;

    }

    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        selectedIndex = (int) view.getTag(R.string.issue_position);
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

    public void translate(View v, float from, float to) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, from, to);
        animation.setDuration(200);
        animation.setFillAfter(false);
        v.startAnimation(animation);
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
