package com.example.triquach.finalproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Proj 1", "Proj 2", "Proj 3", "Proj 4",
                "Proj 5", "Proj 6", "Proj 7", "Proj 8"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );
        listAdapter.add( "Proj x" );



        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
    }
}
