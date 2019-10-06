package com.example.campusstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText e1,e2;
    TextView t1;
    Button b1;
    ProgressDialog pd;
    FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e1=findViewById(R.id.reg_e1);
        e2=findViewById(R.id.reg_e2);
        t1=findViewById(R.id.reg_t1);
        b1=findViewById(R.id.reg_b1);
        b1.setOnClickListener(this);
        t1.setOnClickListener(this);
        pd=new ProgressDialog(this);
        fbauth=FirebaseAuth.getInstance();
    }

    public void onClick(View v)
    {
        if(v==b1)
            registerUser();
        if(v==t1)
        {
            finish();
            Intent it = new Intent(Register.this, Login.class);
            startActivity(it);
        }
    }

    public void registerUser()
    {
        String email=e1.getText().toString();
        String pass=e2.getText().toString();

        if(email.length()==0)
            e1.setError("Please enter your id");
        else if(pass.length()==0)
            e2.setError("Please Enter your password");
        else
        {
            pd.setMessage("Registering User ....");
            pd.show();

            fbauth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    pd.dismiss();
                    if(task.isSuccessful())
                    {
                        finish();
                        Toast.makeText(getApplicationContext(), "Your are registered successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_LONG).show();
                }
            });
        }



    }
}
