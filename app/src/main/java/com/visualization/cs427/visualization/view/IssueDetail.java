package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;

public class IssueDetail extends AppCompatActivity {

    private TextView TextViewType,TextViewStotyPoints,TextViewAssignee,TextViewStatus,TextViewEpicLink;
    private EditText EditTextDescrition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        TextViewType = (TextView) findViewById(R.id.TextViewType);
        TextViewStotyPoints  = (TextView) findViewById(R.id.TextViewStoryPoints);
        TextViewAssignee = (TextView) findViewById(R.id.TextViewAssignee);
        TextViewStatus = (TextView) findViewById(R.id.TextViewStatus);
        TextViewEpicLink = (TextView) findViewById(R.id.TextViewEpicLink);
        EditTextDescrition = (EditText) findViewById(R.id.EditTextDescription);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index",0);

        IssueEntity temp= CurrentProject.getInstance().getIssueEntities().get(index);
        TextViewType.setText(temp.getType());
    }
}
