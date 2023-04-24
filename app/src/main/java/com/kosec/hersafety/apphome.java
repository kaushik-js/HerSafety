package com.kosec.hersafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class apphome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView home,addsec,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphome);
        Toolbar toolbar = findViewById(R.id.toolbar) ;
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string. navigation_drawer_open ,
                R.string. navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        NavigationView navigationView = findViewById(R.id. nav_view ) ;
        navigationView.setNavigationItemSelectedListener(apphome.this) ;
//        home = findViewById(R.id.nav_home);
//        addsec = findViewById(R.id.nav_sec);
//        logout = findViewById(R.id.nav_logout);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(apphome.this, "Home", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        addsec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(apphome.this, "Security", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(apphome.this, "Log out", Toast.LENGTH_SHORT).show();
//            }
//        });



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId() ;
//        if(id == R.id.nav_home) {
//
//        }
//        else if(id == R.id.nav_sec) {
//
//        }
//        else if (id==R.id.nav_logout) {
//
//        }

        return true;
    }


}