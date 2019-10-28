package com.example.campusstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Order extends Fragment
{

     RecyclerView rv;
     OrderAdapter adapter;
     ArrayList<ModelOrder> data;

    public Order()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_order, container, false);

        Toolbar tb=(Toolbar)v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);

        rv=v.findViewById(R.id.order_rv);
        getData();
        return v;
    }

    public void getData()
    {
        data=new ArrayList<>();

        DatabaseReference data_ref= FirebaseDatabase.getInstance().getReference("Orders");
        data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ModelOrder d=ds.getValue(ModelOrder.class);
                    data.add(d);
                }

                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new OrderAdapter(getContext(),data);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });
        // after extracting data

    }

    /*
    @Override
    public void onClick(View v)
    {
        if(v==b1)
            logout();
    }

    public void logout()
    {
        fbauth.signOut();
        finish();
        startActivity(new Intent(getContext(),Login.class));
    }
    */
}
