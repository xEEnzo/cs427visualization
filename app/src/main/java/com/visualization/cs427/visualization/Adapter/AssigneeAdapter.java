package com.visualization.cs427.visualization.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.ContributorEntity;
import com.visualization.cs427.visualization.R;

import java.util.List;

/**
 * Created by linhtnvo on 9/13/2016.
 */
public class AssigneeAdapter extends BaseAdapter{
    private Context mContext;
    private List<ContributorEntity> contributorEntities;

    public AssigneeAdapter(Context mContext, List<ContributorEntity> contributorEntities) {
        this.mContext = mContext;
        this.contributorEntities = contributorEntities;
    }

    @Override
    public int getCount() {
        return contributorEntities.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_assignee, null);
        }
        TextView txtAssignee = (TextView) view.findViewById(R.id.txtAssignee);
        ImageView ivAssignee = (ImageView) view.findViewById(R.id.ivAssigneePic);
        if (i == 0){
            LinearLayout layout = (LinearLayout) txtAssignee.getParent();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivAssignee.getLayoutParams();
            params.setMargins(10,0,0,0);
            ivAssignee.setLayoutParams(params);
            ivAssignee.setVisibility(View.INVISIBLE);
            txtAssignee.setText("Unassigned");
            txtAssignee.setPadding(0,0,0,0);
            layout.removeAllViews();
            deleteParent(txtAssignee);
            deleteParent(ivAssignee);
            layout.addView(txtAssignee);
            layout.addView(ivAssignee);
            return view;
        }
        ContributorEntity contributorEntity = contributorEntities.get(i-1);
        String name = contributorEntity.getName();
        String [] names = name.split(" ");
        String assigneeName = names[names.length-1].toLowerCase();
        int id = mContext.getResources().getIdentifier(assigneeName+"_pic","drawable", mContext.getPackageName());
        txtAssignee.setText(name);
        ivAssignee.setBackgroundResource(id);
        return view;
    }

    private void deleteParent (View v){
        if (v == null){
            return;
        }
        if (v.getParent() != null){
            ((ViewGroup)v.getParent()).removeView(v);
        }
    }
}
