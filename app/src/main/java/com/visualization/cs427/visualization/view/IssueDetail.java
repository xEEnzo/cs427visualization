package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;
import com.visualization.cs427.visualization.Utils.ErrorUtils;

public class IssueDetail extends AppCompatActivity {

    private TextView TextViewType,TextViewStotyPoints,TextViewAssignee,TextViewStatus,TextViewEpicLink;
    private TextView txtDescrition, txtTitle;
    private IssueEntity issueEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        TextViewType = (TextView) findViewById(R.id.TextViewType);
        TextViewStotyPoints  = (TextView) findViewById(R.id.TextViewStoryPoints);
        TextViewAssignee = (TextView) findViewById(R.id.TextViewAssignee);
        TextViewStatus = (TextView) findViewById(R.id.TextViewStatus);
        TextViewEpicLink = (TextView) findViewById(R.id.TextViewEpicLink);
        txtDescrition = (TextView) findViewById(R.id.txtDescription);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        Intent intent = getIntent();
        int index = intent.getIntExtra(SprintDetailActivity.ISSUE_POSITION,-1);
        if (index == -1){
            ErrorUtils.showDialog(this, "Can not open this issue details");
            finish();
            return;
        }

        issueEntity = CurrentProject.getInstance().getIssueEntities().get(index);
        loadData();

    }

    private void loadData() {
        txtTitle.setText(issueEntity.getName());
        TextViewType.setText(issueEntity.getStringType());
        if (issueEntity.getPoint() != -1){
            TextViewStotyPoints.setText(issueEntity.getPoint()+"");
        }
        if (issueEntity.getAssignee() != null){
            TextViewAssignee.setText(issueEntity.getAssignee().getName());
        }
        TextViewStatus.setText(issueEntity.getStringStatus());
        if (issueEntity.getEpic() != null){
            EpicEntity epicEntity = issueEntity.getEpic();
            TextViewEpicLink.setText(epicEntity.getName());
            TextViewEpicLink.setBackgroundColor(Color.parseColor("#"+epicEntity.getColorResID()));
        }
        txtDescrition.setText(issueEntity.getDescription());
    }
}
