package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.visualization.cs427.visualization.R;

import java.util.ArrayList;
import java.util.Arrays;

public class OpenExistingActivity extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_existing);


        mainListView = (ListView) findViewById( R.id.listView );
        String[] planets = new String[] { "Proj 1", "Proj 2", "Proj 3", "Proj 4",
                "Proj 5", "Proj 6", "Proj 7", "Proj 8"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        mainListView.setAdapter( listAdapter );
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OpenExistingActivity.this, SprintDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
