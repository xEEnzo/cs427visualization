package com.example.triquach.finalproj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button CreateButton;
    Button OpenButton;

    public void CreateButton(View view)
    {
      //  CreateButton = (Button) findViewById(R.id.button);
     //   CreateButton.setTextColor(0xFFFFFF);
      //  CreateButton.setBackgroundColor(0x0091ff);
    }
    public void OpenButton(View view)
    {
        OpenButton = (Button) findViewById(R.id.open);
        Intent intent = new Intent(this,OpenExistingActivity.class);

  //      int id = view.getId();
    //    intent.putExtra("book_id",id);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
