package com.visualization.cs427.visualization.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visualization.cs427.visualization.Entity.EpicEntity;
import com.visualization.cs427.visualization.R;

import java.util.List;

/**
 * Created by linhtnvo on 9/13/2016.
 */
public class EpicAdapter extends BaseAdapter{
    private Context mContext;
    private List<EpicEntity> epicEntities;

    public EpicAdapter(Context mContext, List<EpicEntity> epicEntities) {
        this.mContext = mContext;
        this.epicEntities = epicEntities;
    }

    @Override
    public int getCount() {
        return epicEntities.size();
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
            view = inflater.inflate(R.layout.layout_epic, null);
        }
        EpicEntity epicEntity = epicEntities.get(i);
        TextView txtEpicName = (TextView) view.findViewById(R.id.txtEpicName);
        TextView txtEpicColor = (TextView) view.findViewById(R.id.txtEpicColor);
        txtEpicName.setText(epicEntity.getName());
        txtEpicColor.setBackgroundColor(Color.parseColor("#"+epicEntity.getColorResID()));
        return view;
    }
}
