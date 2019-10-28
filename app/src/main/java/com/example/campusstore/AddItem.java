package com.example.campusstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItem extends Fragment implements View.OnClickListener
 {
     EditText e1,e2,e3;
     Button b1,b2;
     ImageView i1;
     Uri uri;
     DatabaseReference data_ref;
     StorageReference store_ref;

     private static final int PICK_IMAGE=1;

    public AddItem()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_add_item, container, false);
        e1=v.findViewById(R.id.add_e1);
        e2=v.findViewById(R.id.add_e2);
        e3=v.findViewById(R.id.add_e3);
        b1=v.findViewById(R.id.add_b1);
        b1.setOnClickListener(this);
        b2=v.findViewById(R.id.add_b2);
        b2.setOnClickListener(this);
        i1=v.findViewById(R.id.add_i1);

        store_ref= FirebaseStorage.getInstance().getReference("Images");
        data_ref= FirebaseDatabase.getInstance().getReference("Items");

        return v;
    }

    public void onClick(View v) {
        if (v == b1)
            fileChooser();
        if (v == b2)
            uploadImage();
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
         i1.setImageURI(uri);
     }

     public void uploadImage()
    {
        if(uri!=null)
        {
            StorageReference fileref=store_ref.child(System.currentTimeMillis()+getFileExtension(uri));
            fileref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            String name=e1.getText().toString().trim();
                            String price=e2.getText().toString().trim();
                            String shop=e3.getText().toString().trim();

                            // this loop is to wait until image is uploaded otherwise we'll get fake url
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isComplete());
                            String url= task.getResult().toString();
                            String owner=FirebaseAuth.getInstance().getUid();

                            ModelData data=new ModelData(name,price,shop,owner,url);
                            String uploadid=data_ref.push().getKey();
                            data_ref.child(uploadid).setValue(data);

                            getFragmentManager().beginTransaction().replace(R.id.home_frag,new AddItem()).commit();
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

