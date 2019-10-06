package com.example.campusstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener
{
    Button b1;
    FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b1=findViewById(R.id.home_b1);
        b1.setOnClickListener(this);
        fbauth=FirebaseAuth.getInstance();
    }

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
        startActivity(new Intent(Home.this,Login.class));
    }
}
