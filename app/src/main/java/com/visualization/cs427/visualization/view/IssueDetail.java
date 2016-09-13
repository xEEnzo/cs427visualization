package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.visualization.cs427.visualization.DAL.TimeLogDAL;
import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;
import com.visualization.cs427.visualization.Utils.ErrorUtils;

import java.util.List;

public class IssueDetail extends AppCompatActivity {

    private TextView TextViewType,TextViewStotyPoints,TextViewAssignee,TextViewStatus,TextViewEpicLink;
    private TextView txtDescrition, txtTitle, txtBlockIssue, txtBlockedIssue, txtCtoR, txtRtoT, txtTtoD;
    private IssueEntity issueEntity;
    private LinearLayout layoutBlock, layoutBlockBy;
    private ImageView ivAssigneePic;
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
        txtBlockIssue = (TextView) findViewById(R.id.txtBlockIssue);
        txtBlockedIssue = (TextView) findViewById(R.id.txtBlockedIssue);
        layoutBlock = (LinearLayout) findViewById(R.id.layoutBlock);
        layoutBlockBy = (LinearLayout) findViewById(R.id.layoutIsBlockBy);
        txtCtoR = (TextView) findViewById(R.id.txtCtoR);
        txtRtoT = (TextView) findViewById(R.id.txtRtoT);
        txtTtoD = (TextView) findViewById(R.id.txtTtoD);
        ivAssigneePic = (ImageView) findViewById(R.id.ivPicAssignee);
        Intent intent = getIntent();
        int index = intent.getIntExtra(SprintDetailActivity.ISSUE_POSITION,-1);
        if (index == -1){
            ErrorUtils.showDialog(this, "Can not open this issue details");
            finish();
            return;
        }

        issueEntity = CurrentProject.getInstance().getIssueEntities().get(index);
        loadData();
        setTimeLog();

    }

    private void loadData() {
        txtTitle.setText(issueEntity.getName());
        TextViewType.setText(issueEntity.getStringType());
        if (issueEntity.getPoint() != -1){
            TextViewStotyPoints.setText(issueEntity.getPoint()+"");
        }
        if (issueEntity.getAssignee() != null){
            ContributorEntity entity = issueEntity.getAssignee();
            String name = entity.getName();
            String [] names = name.split(" ");
            String assigneeName = names[names.length-1].toLowerCase();
            int id = getResources().getIdentifier(assigneeName+"_pic","drawable", getPackageName());
            ivAssigneePic.setBackgroundResource(id);
            TextViewAssignee.setText(name);
        }
        else{
            ivAssigneePic.setVisibility(View.GONE);
            TextViewAssignee.setText("Unassigned");
        }
        TextViewStatus.setText(issueEntity.getStringStatus());
        if (issueEntity.getEpic() != null){
            EpicEntity epicEntity = issueEntity.getEpic();
            TextViewEpicLink.setText(epicEntity.getName());
            TextViewEpicLink.setBackgroundColor(Color.parseColor("#" + epicEntity.getColorResID()));
        }
        txtDescrition.setText(issueEntity.getDescription());
        if (!issueEntity.getBlocker().isEmpty()){
            List<IssueEntity> blocker = issueEntity.getBlocker();
            String blockerString = "";
            for (int i =0;i<blocker.size(); ++i){
                blockerString += "+ " + blocker.get(i).getName();
                if (i!= blocker.size()-1) {
                    blockerString += "\n";
                }
            }
            txtBlockedIssue.setText(blockerString);
        }
        else{
            layoutBlockBy.setVisibility(View.GONE);
        }

        if (!issueEntity.getBlocked().isEmpty()){
            List<IssueEntity> blocked = issueEntity.getBlocked();
            String blockerString = "";
            for (int i =0;i<blocked.size(); ++i){
                blockerString += "+ " + blocked.get(i).getName();
                if (i!= blocked.size()-1) {
                    blockerString += "\n";
                }
            }
            txtBlockIssue.setText(blockerString);
        }
        else{
            layoutBlock.setVisibility(View.GONE);
        }
    }

    private void setTimeLog (){
        int processStatus = issueEntity.getProcessStatus();
        for (int i=1; i<=processStatus;++i){
            try {
                String timeSpent = TimeLogDAL.getInstance().getTimeSpent(this, issueEntity, i+1);
                switch (i){
                    case 1:
                        txtCtoR.setText(timeSpent);
                        break;
                    case 2:
                        txtRtoT.setText(timeSpent);
                        break;
                    case 3:
                        txtTtoD.setText(timeSpent);
                        break;
                }
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }
}
