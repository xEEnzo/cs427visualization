package com.visualization.cs427.visualization.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.ProjectEntity;
import com.visualization.cs427.visualization.R;

import java.util.List;

/**
 * Created by linhtnvo on 9/13/2016.
 */
public class ProjectAdapter extends BaseAdapter {
    private Context mContext;
    private List<ProjectEntity> existingProjects;
    private List<Integer> pictureIDs;

    public ProjectAdapter(Context mContext, List<ProjectEntity> existingProjects, List<Integer> pictureIDs) {
        this.mContext = mContext;
        this.existingProjects = existingProjects;
        this.pictureIDs = pictureIDs;
    }

    @Override
    public int getCount() {
        return existingProjects.size();
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
            view = inflater.inflate(R.layout.layout_project, null);
        }
        TextView txtProjectName = (TextView) view.findViewById(R.id.txtProjectName);
        ImageView ivProjectIcon = (ImageView) view.findViewById(R.id.ivProjectIcon);
        txtProjectName.setText(existingProjects.get(i).getName());
        ivProjectIcon.setImageResource(pictureIDs.get(i));
        return view;
    }
}
