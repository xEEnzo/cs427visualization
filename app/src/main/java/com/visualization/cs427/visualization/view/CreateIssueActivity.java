package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Entity.IssueEntity;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;
import com.visualization.cs427.visualization.Utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateIssueActivity extends AppCompatActivity {
    public void Create(View view)
    {
        setContentView(R.layout.activity_sprint_detail);

    }
    public void Cancle(View view)

    {
        setContentView(R.layout.activity_sprint_detail);
    }
    public void AddEpic(View view)
    {
        Intent intent = new Intent(this,AddNewEpic.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_EPIC);
    }

    private Spinner spinnerIssueType,spinnerAssignee,spinnerEpic,spinnerLinkedIssue,spinnerIssue;
    private TextView TextViewAddEpic;
    private Button Create,Cancle;
    private ImageView ImageAddEpic;
    private EditText editTextSummary,editTextStoryPoints,editTextDescription;
    private String textspinnerIssueType,textSummary,textStoryPoints,textAssignee,textEpic,textLinkedIssue,textIssue,textDescription;
    private List<EpicEntity> epicEntities = new ArrayList<>();
    private List<ContributorEntity> contributorEntities = new ArrayList<>();
    public static final int REQUEST_CODE_ADD_EPIC = 100;
    public static final int RESULT_OK_NEW_EPIC = 200;
    public static final int RESULT_CANCEL_NEW_EPIC = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue2);

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
        editTextSummary = (EditText) findViewById(R.id.editTextSummary);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextStoryPoints = (EditText) findViewById(R.id.editTextStoryPoints);

        String[] items = new String[]{"Story", "Bug", "Task"};
        ArrayAdapter<String> adapterIssiueType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerIssueType.setAdapter(adapterIssiueType);

        String[] itemsLinedIssues = new String[]{"Blocks", "Is blocked by"};
        ArrayAdapter<String> adapterLinedIssues = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsLinedIssues);
        spinnerLinkedIssue.setAdapter(adapterLinedIssues);

        setUpEpicSpinner();
        setUpAssigneeSpinner();
        setUpIssueLinkSpinner();


        /*----------------------------------Get content of user input ----------------------------*/
        textspinnerIssueType = spinnerIssueType.getSelectedItem().toString();
        textSummary = editTextSummary.getText().toString();
        textStoryPoints = editTextStoryPoints.getText().toString();
        textAssignee = spinnerAssignee.getSelectedItem().toString();
        textEpic = spinnerEpic.getSelectedItem().toString();
        textLinkedIssue = spinnerLinkedIssue.getSelectedItem().toString();
        textIssue = spinnerIssue.getSelectedItem().toString();
        textDescription = editTextDescription.getText().toString();
        /*----------------------------------Get content of user input ----------------------------*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_EPIC){
            if (resultCode == RESULT_OK_NEW_EPIC){
                setUpEpicSpinner();
            }
        }
    }

    private void setUpEpicSpinner (){
        if (!epicEntities.isEmpty()){
            epicEntities.clear();
        }
        epicEntities.addAll(CurrentProject.getInstance().getEpicEntities());
        int count = 0;
        String [] epicNames = new String[epicEntities.size()];
        for (EpicEntity epicEntity : epicEntities){
            epicNames[count] = epicEntity.getName();
            ++count;
        }
        ArrayAdapter<String> adapterEpic = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, epicNames);
        spinnerEpic.setAdapter(adapterEpic);
    }

    private void setUpAssigneeSpinner(){
        if (!contributorEntities.isEmpty()){
            contributorEntities.clear();
        }
        int count = 0;
        contributorEntities = DataUtils.getAllContributors(CurrentProject.getInstance().getIssueEntities());
        String [] contributorNames = new String[contributorEntities.size()];
        for (ContributorEntity contributorEntity : contributorEntities){
            contributorNames[count] = contributorEntity.getName();
            ++count;
        }
        ArrayAdapter<String> adapterAssignee = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contributorNames);
        spinnerAssignee.setAdapter(adapterAssignee);
    }

    private void setUpIssueLinkSpinner(){
        int count = 0;
        List<IssueEntity> issueEntities = CurrentProject.getInstance().getIssueEntities();
        String[] issueNames = new String[issueEntities.size()];
        for (IssueEntity issueEntity : issueEntities){
            issueNames[count] = issueEntity.getName();
            ++count;
        }
        ArrayAdapter<String> adapterIssue = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, issueNames);
        spinnerIssue.setAdapter(adapterIssue);
    }
}
