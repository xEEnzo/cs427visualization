package com.visualization.cs427.visualization.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.visualization.cs427.visualization.DAL.EpicDAL;
import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.Exception.DatabaseException;
import com.visualization.cs427.visualization.R;
import com.visualization.cs427.visualization.Utils.CurrentProject;

public class AddNewEpic extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextAddEpic;
    private LinearLayout square_1,square_2,square_3,square_4,square_5,square_6,square_7;
    int selectedColorId;
    LinearLayout previouSelected = null;
    public void Add(View view)
    {
        EpicEntity epicEntity = new EpicEntity(null, editTextAddEpic.getText().toString(), getColorString(selectedColorId));
        try {
            CurrentProject.getInstance().setEpicEntities(EpicDAL.getInstance().insertNewEpic(this,epicEntity,CurrentProject.getInstance().getProjectEntity().getId()));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        setResult(CreateIssueActivity.RESULT_OK_NEW_EPIC, null);
        finish();
    }
    public void Cancle(View view)
    {
        setResult(CreateIssueActivity.RESULT_CANCEL_NEW_EPIC, null);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_epic);

        editTextAddEpic = (EditText) findViewById(R.id.editTextEpicName);
        square_1 = (LinearLayout) findViewById(R.id.square_1);
        square_1.setOnClickListener(this);
        square_2 = (LinearLayout) findViewById(R.id.square_2);
        square_2.setOnClickListener(this);
        square_3 = (LinearLayout) findViewById(R.id.square_3);
        square_3.setOnClickListener(this);
        square_4 = (LinearLayout) findViewById(R.id.square_4);
        square_4.setOnClickListener(this);
        square_5 = (LinearLayout) findViewById(R.id.square_5);
        square_5.setOnClickListener(this);
        square_6 = (LinearLayout) findViewById(R.id.square_6);
        square_6.setOnClickListener(this);
        square_7 = (LinearLayout) findViewById(R.id.square_7);
        square_7.setOnClickListener(this);
    }

    private String getColorString (int id){
        if (id == R.id.square_1){
            return "9e015f";
        }
        else if (id == R.id.square_2){
            return "117c00";
        }
        else if (id == R.id.square_3){
            return "4000e6";
        }
        else if (id == R.id.square_4){
            return "005963";
        }
        else if (id == R.id.square_5){
            return "d02900";
        }
        else if (id == R.id.square_6){
            return "ea405f";
        }
        else{
            return "314661";
        }
    }


    @Override
    public void onClick(View view) {
        if (previouSelected != null){
            previouSelected.removeAllViews();
        }
        selectedColorId = view.getId();
        LinearLayout selected = (LinearLayout) view;
        ImageView imageView = new ImageView(AddNewEpic.this);
        imageView.setImageResource(R.drawable.select_icon);
        selected.addView(imageView);
        previouSelected = selected;
    }
}
