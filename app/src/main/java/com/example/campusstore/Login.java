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

public class Login extends AppCompatActivity implements View.OnClickListener
{
    EditText e1,e2;
    TextView t1;
    Button b1;
    ProgressDialog pd;
    FirebaseAuth fbauth;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1=findViewById(R.id.log_e1);
        e2=findViewById(R.id.log_e2);
        t1=findViewById(R.id.log_t1);
        b1=findViewById(R.id.log_b1);
        b1.setOnClickListener(this);
        t1.setOnClickListener(this);
        pd=new ProgressDialog(this);
        fbauth=FirebaseAuth.getInstance();

        /*
        if(fbauth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        */
    }

    public void onClick(View v)
    {
        if(v==b1)
            loginUser();

        if(v==t1)
        {
            finish();
            Intent it = new Intent(Login.this, Register.class);
            startActivity(it);
        }
    }

    public void loginUser()
    {
        String email=e1.getText().toString();
        String pass=e2.getText().toString();
        if(email.length()==0)
            e1.setError("Please enter your id");
        else if(pass.length()==0)
            e2.setError("Please Enter your password");
        else
        {
            pd.setMessage("Logging in ..... ");
            pd.show();
            fbauth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            pd.dismiss();
                            if(task.isSuccessful())
                            {
                                finish();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Please enter correct email and password",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}
