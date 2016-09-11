package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.visualization.cs427.visualization.R;

public class AddNewEpic extends AppCompatActivity {
    private EditText editTextAddEpic;
    private String textAddEpic;
    private View square_1,square_2,square_3,square_4,square_5,square_6,square_7;

    public void Add(View view)
    {

    }
    public void Cancle(View view)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_epic);

        editTextAddEpic = (EditText) findViewById(R.id.editTextEpicName);
        square_1 = (View) findViewById(R.id.square_1);
        square_2 = (View) findViewById(R.id.square_2);
        square_3 = (View) findViewById(R.id.square_3);
        square_4 = (View) findViewById(R.id.square_4);
        square_5 = (View) findViewById(R.id.square_5);
        square_6 = (View) findViewById(R.id.square_6);
        square_7 = (View) findViewById(R.id.square_7);

        textAddEpic = editTextAddEpic.getText().toString();


    }


}
