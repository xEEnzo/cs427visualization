package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.ErrorUtils;


public class MainActivity extends AppCompatActivity {
    Button CreateButton;
    Button OpenButton;

    public void CreateButton(View view)
    {
        ErrorUtils.showDialog(this, "This function is under development");
    }
    public void OpenButton(View view)
    {
        OpenButton = (Button) findViewById(R.id.open);
        Intent intent = new Intent(this, OpenExistingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
    }
}
