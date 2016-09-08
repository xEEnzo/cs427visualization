package com.visualization.cs427.visualization.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.visualization.cs427.visualization.R;

public class CreateIssueActivity extends AppCompatActivity {
    public void Create(View view)
    {
        setContentView(R.layout.activity_sprint_detail);
    }
    public void Cancle(View view)
    {
        setContentView(R.layout.activity_sprint_detail);
    }
    private Spinner spinnerIssueType,spinnerAssignee,spinnerEpic,spinnerLinkedIssue,spinnerIssue;
    private TextView TextViewAddEpic;
    private Button Create,Cancle;
    private ImageView ImageAddEpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue2);

        /*-------------------------------- set content for Spinner -------------------------------*/

        Spinner dropdownIssueType = (Spinner)findViewById(R.id.spinnerIssueType);
        String[] items = new String[]{"Story", "Bug", "Task"};
        ArrayAdapter<String> adapterIssiueType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownIssueType.setAdapter(adapterIssiueType);

        Spinner dropdownAssingee = (Spinner)findViewById(R.id.spinnerAssignee);
        String[] itemsAssignee = new String[]{"Contributor1", "Contributor2", "Contributor3"};
        ArrayAdapter<String> adapterAssignee = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsAssignee);
        dropdownAssingee.setAdapter(adapterAssignee);

        Spinner dropdownEpic = (Spinner)findViewById(R.id.spinnerEpic);
        String[] itemsEpic = new String[]{"Epic1", "Epic2", "Epic3"};
        ArrayAdapter<String> adapterEpic = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsEpic);
        dropdownEpic.setAdapter(adapterEpic);

        Spinner dropdownLinedIssues = (Spinner)findViewById(R.id.spinnerLinkedIssues);
        String[] itemsLinedIssues = new String[]{"Blocks", "Is blocked by"};
        ArrayAdapter<String> adapterLinedIssues = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsLinedIssues);
        dropdownLinedIssues.setAdapter(adapterLinedIssues);

        Spinner dropdownIssue = (Spinner)findViewById(R.id.spinnerIssue);
        String[] itemsIssue = new String[]{"Issue1", "Issue2","Issue3","Issue4"};
        ArrayAdapter<String> adapterIssue = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsIssue);
        dropdownIssue.setAdapter(adapterIssue);

        /*-------------------------------- set content for Spinner -------------------------------*/



        /*-------------------------------------findViewById---------------------------------------*/
        spinnerIssueType = (Spinner) findViewById(R.id.spinnerIssueType);
        spinnerAssignee = (Spinner) findViewById(R.id.spinnerAssignee);
        spinnerEpic = (Spinner) findViewById(R.id.spinnerEpic);
        spinnerLinkedIssue = (Spinner) findViewById(R.id.spinnerLinkedIssues);
        spinnerIssue = (Spinner) findViewById(R.id.spinnerIssue);

        ImageAddEpic = (ImageView) findViewById(R.id.ImageAddEpic);
        Create = (Button) findViewById(R.id.Create);
        Cancle = (Button) findViewById(R.id.Cancle);

        TextViewAddEpic = (TextView) findViewById(R.id.TextViewAddEpic);

        /*-------------------------------------findViewById---------------------------------------*/

    }
}
