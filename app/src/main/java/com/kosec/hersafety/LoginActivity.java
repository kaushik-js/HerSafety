package com.kosec.hersafety;

import static android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button login_bt;
    EditText unm_et,pwd_et;
    TextView signup_tv;
    File file;
    Context context;
    FirebaseFirestore db;
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        login_bt = findViewById(R.id.login_bt);
        unm_et = findViewById(R.id.login_unm);
        pwd_et = findViewById(R.id.login_pwd);
        signup_tv = findViewById(R.id.signup_bt);
        db = FirebaseFirestore.getInstance();



            login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String unm = unm_et.getText().toString();
                String pwd = pwd_et.getText().toString();
                db.collection("userDetails").document(unm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("WorldReadableFiles")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){

                            String c_pwd = Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("pwd")).toString();

                            if(c_pwd.equals(pwd)){


                                try{
                                    FileOutputStream fos = openFileOutput("login.txt", Context.MODE_PRIVATE);
                                    fos.write(unm.getBytes());
                                    fos.flush();
                                    fos.close();
                                    Toast.makeText(context, "Saved file", Toast.LENGTH_SHORT).show();

                                } catch(Exception e){
                                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,HomePage.class);
                                startActivity(intent);


                            } else  {
                                Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });

        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}