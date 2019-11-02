package com.example.campusstore;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.griditem, null);
        TextView t1=convertView.findViewById(R.id.grid_t1);
        TextView t2=convertView.findViewById(R.id.grid_t2);
        ImageView i1=convertView.findViewById(R.id.grid_i1);
        ImageView i2=convertView.findViewById(R.id.grid_i2);

        ModelData data=list_data.get(position);
        t1.setText(data.getItem_name());
        t2.setText("\u20B9"+" "+data.getItem_price());

        Picasso.with(ct).load(data.getItem_url()).fit().centerCrop().into(i1);

        final int pos=position;
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ModelData sel_item=list_data.get(pos);
                final DatabaseReference data_ref= FirebaseDatabase.getInstance().getReference("Items");
                final FirebaseStorage store_ref= FirebaseStorage.getInstance();

                StorageReference img_ref=store_ref.getReferenceFromUrl(sel_item.getItem_url());
                img_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        data_ref.child(sel_item.getKey()).removeValue();
                    }
                });
            }
        });
        return convertView;
    }
}