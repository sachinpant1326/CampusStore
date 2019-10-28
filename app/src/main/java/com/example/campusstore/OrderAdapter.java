package com.example.campusstore;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ModelOrder data=list_Data.get(position);
        holder.t1.setText("Item             : "+data.getItem_name()+" ( Qty "+data.getItem_quantity()+" )");
        holder.t2.setText("Customer Name    : "+data.getUname());
        holder.t3.setText("Customer Address : "+data.getUaddress());

    }

    @Override
    public int getItemCount() {
        return list_Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2, t3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.order_t1);
            t2 = itemView.findViewById(R.id.order_t2);
            t3 = itemView.findViewById(R.id.order_t3);
        }
    }
}
