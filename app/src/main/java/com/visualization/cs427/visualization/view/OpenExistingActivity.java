package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.visualization.cs427.visualization.DAL.ProjectDAL;
import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenExistingActivity extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private List<ProjectEntity> projectEntities;
    private List<String> listProjectName;
    public static final String PROJECT_ID = "project_id";
    public static final String PROJECT_NAME = "project_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_existing);
        mainListView = (ListView) findViewById( R.id.listView );
        listProjectName = new ArrayList<>();
        getAllExistingProject();
        for (ProjectEntity projectEntity : projectEntities){
            listProjectName.add(projectEntity.getName());
        }
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listProjectName);
        mainListView.setAdapter( listAdapter );
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OpenExistingActivity.this, SprintDetailActivity.class);
                intent.putExtra(PROJECT_ID, projectEntities.get(i).getId());
                intent.putExtra(PROJECT_NAME, projectEntities.get(i).getName());
                startActivity(intent);
            }
        });
    }

    private void getAllExistingProject(){
        projectEntities = new ArrayList<>();
        try {
            projectEntities = ProjectDAL.getInstance().getAllProject(this);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
