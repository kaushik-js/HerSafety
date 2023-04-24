package com.kosec.hersafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class ProfilePage extends AppCompatActivity {

    FirebaseFirestore db;
    String unm="";
    TextView pf_tv;
    TextView profile,sec,lgout,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        db = FirebaseFirestore.getInstance();

        home = findViewById(R.id.home_tab);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,HomePage.class);
                startActivity(intent);
            }
        });

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,ProfilePage.class);
                startActivity(intent);
            }
        });

        lgout = findViewById(R.id.logout_tab);
        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    FileOutputStream fos = openFileOutput("login.txt", Context.MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e){
                    Toast.makeText(ProfilePage.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ProfilePage.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        sec = findViewById(R.id.security_tab);
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,AddSecurity.class);
                startActivity(intent);
            }
        });
        pf_tv = findViewById(R.id.pf_tv);

        try{
            FileInputStream fin = openFileInput("login.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while((a=fin.read())!=-1){
                temp.append((char)a);
            }
            unm = temp.toString();
        } catch(Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        db.collection("userDetails").document(unm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot doc = task.getResult();
                pf_tv.append("\n"+ Objects.requireNonNull(doc.getData()).get("fnm"));
                pf_tv.append("\n"+ Objects.requireNonNull(doc.getData()).get("dob"));
                pf_tv.append("\n"+ Objects.requireNonNull(doc.getData()).get("add"));
                pf_tv.append("\n"+ Objects.requireNonNull(doc.getData()).get("city"));
                pf_tv.append("\n"+ Objects.requireNonNull(doc.getData()).get("state"));
                db.collection("userNumbers").document(unm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        pf_tv.append("\n\nFavourite Contacts -\n");
                        if(doc.exists()){
                            String phns = Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("phns")).toString();
                            ArrayList<String> contacts = new ArrayList<>(Arrays.asList(phns.split(",")));
                            for(String cc : contacts){
                                pf_tv.append(cc+"\n");
                            }
                        }
                    }
                });

            }
        });



    }
}