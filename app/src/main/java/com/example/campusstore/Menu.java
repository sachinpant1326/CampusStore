package com.example.campusstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Menu extends Fragment implements View.OnClickListener
{
    GridView gv;
    GridAdapter cg;
    ArrayList<ModelData> data;
    DatabaseReference data_ref;
    FloatingActionButton menuButton;
    private static final int PICK_IMAGE=1;
    Uri uri;

    public Menu()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu, container, false);
        data_ref= FirebaseDatabase.getInstance().getReference("Items");
        menuButton=v.findViewById(R.id.menu_addbutton);
        menuButton.setOnClickListener(this);

        getData();

        gv=v.findViewById(R.id.menu_gv);
        cg=new GridAdapter(getActivity(),data);
        gv.setAdapter(cg);
        return v;
    }

    public void getData()
    {
        data=new ArrayList<>();
        data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                data.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ModelData d=ds.getValue(ModelData.class);
                    d.setKey(ds.getKey());
                    FirebaseAuth fb=FirebaseAuth.getInstance();
                    if(d.getItem_owner().equals(fb.getUid()))
                        data.add(d);

                }
                cg.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==menuButton)
            addtoCart();
    }

    public void addtoCart()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View v=getLayoutInflater().inflate(R.layout.additem,null);
        alert.setTitle("Add New Item to Menu");
        final EditText name=v.findViewById(R.id.add_e1);
        final EditText price=v.findViewById(R.id.add_e2);
        final EditText shop=v.findViewById(R.id.add_e3);
        final ImageView iv=v.findViewById(R.id.add_i1);

        final Button b1=v.findViewById(R.id.add_b1);
        final Button b2=v.findViewById(R.id.add_b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fileChooser();
                iv.setImageURI(uri);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String n=name.getText().toString().trim();
                String p=price.getText().toString().trim();
                String s=shop.getText().toString().trim();;
                uploadData(n,p,s);
            }
        });
        alert.setView(v);
        alert.show();
    }


    public void fileChooser()
    {
        Intent it=new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(it,PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri=data.getData();
    }

    public void uploadData(final String name,final String price,final String shop)
    {
        final DatabaseReference data_ref= FirebaseDatabase.getInstance().getReference("Items");
        final StorageReference store_ref= FirebaseStorage.getInstance().getReference("Images");
        if(uri!=null)
        {
            StorageReference fileref=store_ref.child(System.currentTimeMillis()+getFileExtension(uri));
            fileref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // this loop is to wait until image is uploaded otherwise we'll get fake url
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isComplete());
                            String url= task.getResult().toString();
                            String owner=FirebaseAuth.getInstance().getUid();

                            ModelData data=new ModelData(name,price,shop,owner,url);
                            String uploadid=data_ref.push().getKey();
                            data_ref.child(uploadid).setValue(data);

                            getFragmentManager().beginTransaction().replace(R.id.home_frag,new Menu()).commit();
                        }
                    });
        }

    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mp=MimeTypeMap.getSingleton();
        return mp.getExtensionFromMimeType(cr.getType(uri));
    }
}
