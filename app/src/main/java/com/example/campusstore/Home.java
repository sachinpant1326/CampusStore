package com.example.campusstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity
{
    FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fbauth=FirebaseAuth.getInstance();
        BottomNavigationView bnav=findViewById(R.id.home_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_frag,new Menu()).commit();
        bnav.setOnNavigationItemSelectedListener(nlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                public boolean onNavigationItemSelected(MenuItem item)
                {
                    Fragment fb=null;
                    switch (item.getItemId())
                    {
                        case R.id.nav_menu:
                            fb=new Menu();
                            break;
                        case R.id.nav_add:
                            fb=new Profile();
                            break;
                        case R.id.nav_order:
                            fb=new Order();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_frag,fb).commit();
                    return true;
                }
            };
}
