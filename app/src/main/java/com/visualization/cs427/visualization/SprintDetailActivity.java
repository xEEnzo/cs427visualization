package com.visualization.cs427.visualization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.triquach.finalproj.R;

import java.util.List;

public class SprintDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener{
    private TextView txtSprint, txtCreateissue;
    private LinearLayout layoutSprint, layoutBacklog;
    private List<String> issues;
    private List<Integer> typeIssue;
    private LayoutInflater inflater;
    private List<View> issueViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        txtSprint = (TextView) findViewById(R.id.txtSprint);
        txtCreateissue = (TextView) findViewById(R.id.txtCreateIssue);
        txtCreateissue.setOnClickListener(this);
        layoutBacklog = initLayout(R.id.layoutBacklog);
        layoutSprint = initLayout(R.id.layoutSprint);
        initDummyData();
        createBackLog();

    }

    private LinearLayout initLayout(int id){
        LinearLayout layout = (LinearLayout) findViewById(id);
        layout.setOnDragListener(this);
        return layout;
    }

    private void createBackLog() {
        for (int i=0; i< issues.size(); ++i){
            View view = createIssueView(i);
            view.setOnDragListener(this);
            layoutBacklog.addView(view);
        }
    }

    private void initDummyData() {
        int count = 0;
        for (int i =0; i<10; ++i){
            String tmp = "Issue";
            issues.add(tmp+" "+i);
            typeIssue.add(i % 3);
        }
    }

    private View createIssueView(int position){
        View root = inflater.inflate(R.layout.issue_layout_view, null);
        ViewHolder holder = new ViewHolder(root);
        holder.txtIssueName.setText(issues.get(position));
        Integer issuetypeID = getIssueTypeID(typeIssue.get(position));
        if (issuetypeID == null){
            return root;
        }
        holder.ivIssueType.setBackgroundResource(issuetypeID);
        return root;
    }

    private Integer getIssueTypeID(int type){
        switch (type){
            case 1:
                return R.drawable.circle_issue_task;
            case 2:
                return R.drawable.circle_issue_story;
            case 3:
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
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                dragStart();
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(position < increasement){
                    colorTheTop();
                }
                else if(position > length - increasement){
                    colorThelast();
                }
                else {
                    dragEnter(position);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                dragExit();
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                autoScroll(v, event);
                break;
            case DragEvent.ACTION_DROP:
                dragDrop();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                onSelecting = false;
                break;
            default:
                break;
        }
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
}
