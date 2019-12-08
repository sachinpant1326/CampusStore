package com.example.campusstore;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>
{
    ArrayList<ModelOrder> list_Data;
    Context ct;

    public OrderAdapter(Context ct, ArrayList<ModelOrder> list_Data)
    {
        this.ct=ct;
        this.list_Data=list_Data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater lf = LayoutInflater.from(ct);
        View v = lf.inflate(R.layout.orderitem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        ModelOrder data=list_Data.get(position);
        holder.t1.setText("Item                          : "+data.getItem_name()+" ( Qty "+data.getItem_quantity()+" )");
        holder.t2.setText("Customer Name     : "+data.getUname());
        holder.t3.setText("Phone No          : "+data.getUphone());
        holder.t4.setText("Customer Address : "+data.getUaddress());
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ModelOrder sel_item=list_Data.get(position);
                final DatabaseReference data_ref= FirebaseDatabase.getInstance().getReference("Orders");
                data_ref.child(sel_item.getKey()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2, t3,t4;
        Button b1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.order_t1);
            t2 = itemView.findViewById(R.id.order_t2);
            t3 = itemView.findViewById(R.id.order_t3);
            t4 = itemView.findViewById(R.id.order_t4);
            b1 = itemView.findViewById(R.id.order_b1);
        }
    }
}
