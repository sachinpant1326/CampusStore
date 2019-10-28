package com.example.campusstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu extends Fragment
{
    GridView gv;
    GridAdapter cg;
    ArrayList<ModelData> data;
    DatabaseReference data_ref;

    public Menu()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu, container, false);
        data_ref= FirebaseDatabase.getInstance().getReference("Items");
        gv=v.findViewById(R.id.menu_gv);
        getData();
        return v;
    }

    public void getData()
    {
        data=new ArrayList<>();
        data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ModelData d=ds.getValue(ModelData.class);
                    FirebaseAuth fb=FirebaseAuth.getInstance();
                    if(d.getItem_owner().equals(fb.getUid()))
                        data.add(d);
                }

                cg=new GridAdapter(getActivity(),data);
                gv.setAdapter(cg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });
    }

}
