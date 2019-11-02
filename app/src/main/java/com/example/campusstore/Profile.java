package com.example.campusstore;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;


public class Profile extends Fragment
{
    FloatingActionButton fbutt;
    String[] action={"Update Profile Picture","Update Name","Update Phone","Update Shop Name"};
    ImageView iv;
    TextView name,phone,email,shop;
    ModelProfile data;
    Uri uri;
    StorageReference s_ref;
    DatabaseReference d_ref;

    public Profile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        fbutt=v.findViewById(R.id.profile_edit);
        name=v.findViewById(R.id.profile_name);
        phone=v.findViewById(R.id.profile_phone);
        email=v.findViewById(R.id.profile_email);
        shop=v.findViewById(R.id.profile_shop);
        iv=v.findViewById(R.id.profile_image);

        s_ref=FirebaseStorage.getInstance().getReference("Images");
        d_ref=FirebaseDatabase.getInstance().getReference("Profiles");

        getProfile();

        fbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose what to Edit");
                builder.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                            updateImage();
                        else
                            updateData(which);
                    }
                });
                builder.show();
            }
        });
        return v;
    }

    public void updateImage()
    {
        Intent it=new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(it,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uri=data.getData();
        iv.setImageURI(uri);
        ModelProfile mp=getProfile();
        if(mp.getItem_url().length()!=0)
            deleteOldPic(mp);

        d_ref.child(mp.getKey()).removeValue();

        System.out.println(mp.getEmail() +" is taken");
        insertNewPic(uri,mp);
    }

    public void updateData(int which)
    {
        final AlertDialog.Builder build=new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        build.setView(input);
        if(which==1)
            build.setTitle("Enter your name");
        else if(which==2)
            build.setTitle("Enter your Phone");
        else
            build.setTitle("Enter your Shop");

        final int pos=which;

        build.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseAuth fbauth=FirebaseAuth.getInstance();
                ModelProfile mp=getProfile();
                if(pos==1)
                    mp.setName(input.getText().toString());
                else if(pos==2)
                    mp.setPhone(input.getText().toString());
                else
                    mp.setShop(input.getText().toString());
                d_ref.child(fbauth.getUid()).removeValue();
                d_ref.child(fbauth.getUid()).setValue(mp);
            }
        });
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        build.show();
    }


    public ModelProfile getProfile()
    {
        d_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    FirebaseAuth fb=FirebaseAuth.getInstance();
                    if(ds.getKey().equals(fb.getUid()))
                        data=ds.getValue(ModelProfile.class);
                }

                if(data.getEmail().length()!=0)
                    email.setText(data.getEmail());
                if(data.getPhone().length()!=0)
                    phone.setText(data.getPhone());
                if(data.getName().length()!=0)
                    name.setText(data.getName());
                if(data.getShop().length()!=0)
                    shop.setText(data.getShop());
                if(data.getItem_url().length()!=0)
                    Picasso.with(getContext()).load(data.getItem_url()).fit().centerCrop().into(iv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });
        return data;
    }

    public void deleteOldPic(final ModelProfile sel_item)
    {
        final FirebaseStorage store_ref = FirebaseStorage.getInstance();
        StorageReference img_ref = store_ref.getReferenceFromUrl(sel_item.getItem_url());
        img_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                System.out.println("deleted old pic");
            }
        });
    }

    public void insertNewPic(Uri uri, final ModelProfile sel_item)
    {
        if(uri!=null)
        {
            System.out.println("Insertion started");
            StorageReference fileref=s_ref.child(System.currentTimeMillis()+getFileExtension(uri));
            fileref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // this loop is to wait until image is uploaded otherwise we'll get fake url
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isComplete());
                            String url= task.getResult().toString();

                            System.out.println("insertions done");
                            sel_item.setItem_url(url);
                            d_ref.child(FirebaseAuth.getInstance().getUid()).setValue(sel_item);
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
