package com.kosec.hersafety;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    MyReceiver myReceiver;
    TextView title;
    String unm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);

        title = findViewById(R.id.app_title);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("H");
            }
        },200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("e");
            }
        },400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("r");
            }
        },600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("S");
            }
        },800);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("a");
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("f");
            }
        },1200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("e");
            }
        },1400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("t");
            }
        },1600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.append("y");
            }
        },1800);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try{

                    FileInputStream fin = openFileInput("login.txt");
                    StringBuilder temp = new StringBuilder();
                    int a;
                    while ((a = fin.read()) != -1)
                    {
                        temp.append((char)a);
                    }
                    String unm = temp.toString();
                    if(unm.length()>0){
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                }catch(Exception e){
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }


            }
        },2200);



    }

    @Override
    protected void onDestroy()
    {
        if (myReceiver != null)
        {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }
}