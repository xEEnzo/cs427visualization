package com.visualization.cs427.visualization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SprintDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener{
    private TextView txtSprint, txtCreateissue;
    private LinearLayout layoutSprint, layoutBacklog;
    private List<String> issues;
    private List<Integer> typeIssue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
        txtSprint = (TextView) findViewById(R.id.txtSprint);
        txtCreateissue = (TextView) findViewById(R.id.txtCreateIssue);
        txtCreateissue.setOnClickListener(this);
        initDummyData();

    }

    private void initDummyData() {
        int count = 0;
        for (int i =0; i<10; ++i){

        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }
}
