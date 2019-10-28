package com.example.campusstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter
{
    ArrayList<ModelData> list_data;
    private Context ct;

    public GridAdapter(Context ct,ArrayList<ModelData> data)
    {
        this.ct=ct;
        this.list_data=data;
    }
    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.griditem, null);
        TextView t1=convertView.findViewById(R.id.grid_t1);
        TextView t2=convertView.findViewById(R.id.grid_t2);
        ImageView i1=convertView.findViewById(R.id.grid_i1);

        ModelData data=list_data.get(position);
        t1.setText(data.getItem_name());
        t2.setText("\u20B9"+" "+data.getItem_price());

        Picasso.with(ct).load(data.getItem_url()).fit().centerCrop().into(i1);
        return convertView;
    }
}