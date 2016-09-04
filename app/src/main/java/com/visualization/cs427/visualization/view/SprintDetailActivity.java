package com.visualization.cs427.visualization.view;

import android.app.ActionBar;
import android.content.ClipData;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import java.util.List;

public class SprintDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener{
    private TextView txtSprint, txtCreateissue;
    private LinearLayout layoutSprint, layoutBacklog, layoutAll;
    private List<IssueEntity> issueEntities = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int LAYOUT_CONTAINER_TYPE = 1;
    private static final int LAYOUT_ELEMENT_TYPE = 2;
    private static final int LAYOUT_TYPE = 0;
    private int heightofElement;
    private ScrollView scrollView;
    private int screenHeight;
    private boolean onDragging = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
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

    private void setUpScrollView() {
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup parent = (ViewGroup) layoutAll.getParent();
        if (parent != null){
            parent.removeView(layoutAll);
            scrollView.addView(layoutAll);
            parent.addView(scrollView);
        }
        else{
            scrollView.addView(layoutAll);
        }
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onDragging;
            }
        });
    }

    private LinearLayout initLayout(int id){
        LinearLayout layout = (LinearLayout) findViewById(id);
        layout.setOnDragListener(this);
        layout.setTag(R.string.layout_type, LAYOUT_CONTAINER_TYPE);
        return layout;
    }

    private void createBackLog() {
        for (IssueEntity entity : issueEntities){
            View view = createIssueView(entity);
            if (issueEntities.indexOf(entity) == 0){
                view.setBackgroundResource(R.drawable.border_top);
            }
            else{
                view.setBackgroundResource(R.drawable.border_middle);
            }
            view.setOnDragListener(this);
            layoutBacklog.addView(view);
        }
    }

    private void initDummyData() {
        for (int i =0; i<10; ++i){
            String  name = "Issue" + i;
            IssueEntity entity = new IssueEntity(String.valueOf(i), name, i%3, null, 0, null, -1, -1);
            issueEntities.add(entity);
        }
    }

    private View createIssueView(IssueEntity issueEntity){
        View root = inflater.inflate(R.layout.issue_layout_view, null);
        ViewHolder holder = new ViewHolder(root);
        holder.txtIssueName.setText(issueEntity.getName());
        Integer issuetypeID = getIssueTypeID(issueEntity.getType());
        if (issuetypeID != null){
            holder.ivIssueType.setBackgroundResource(issuetypeID);
        }
        root.setOnDragListener(this);
        root.setOnLongClickListener(this);
        root.setTag(R.string.layout_type, LAYOUT_ELEMENT_TYPE);
        root.setTag(R.string.issue_id, issueEntity.getId());
        return root;
    }

    private Integer getIssueTypeID(int type){
        switch (type){
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
        int type = (int) v.getTag(R.string.layout_type);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (type == LAYOUT_CONTAINER_TYPE){
                    return false;
                }
                heightofElement = v.getHeight();
                onDragging =true;
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (type == LAYOUT_CONTAINER_TYPE){
                    int currnentHeight = v.getHeight();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                    params.height = currnentHeight + heightofElement;
                    v.setLayoutParams(params);
                }
                else{
                    float viewY = v.getY();
                    Point p = getTouchPositionFromDragEvent(v, event);
                    if (p.y <= (viewY - heightofElement/2)){
                        translate(v, viewY, viewY - heightofElement);
                    }
                    else{
                        translate(v, viewY, viewY + heightofElement);
                    }
                }
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                autoScroll(v, event);
                break;
            case DragEvent.ACTION_DROP:
                if (type == LAYOUT_ELEMENT_TYPE){
                    return false;
                }
                dragDrop(v, event);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                onDragging = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void dragDrop(View v, DragEvent event) {
        View dropped = (View) event.getLocalState();
        String id = (String) dropped.getTag();
        // update database
        int index = scrollView.indexOfChild(v);
        scrollView.removeView(v);
        ((LinearLayout) v).addView(dropped);
        scrollView.addView(v, index);
        if (v.getId() == R.id.layoutBacklog){
            layoutSprint.removeView(dropped);
        }
        else{
            layoutBacklog.removeView(dropped);
        }
        onDragging = false;

    }

    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        return true;
    }


    private class ViewHolder{
        private TextView txtIssueName, txtEpicName, txtContributor;
        private ImageView ivIssueType;
        public ViewHolder(View root){
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

    public void translate(View v, float from, float to)
    {
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
