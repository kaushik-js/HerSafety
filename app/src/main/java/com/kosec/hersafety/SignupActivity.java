package com.kosec.hersafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    Button reg_bt;
    EditText v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        reg_bt = findViewById(R.id.reg_bt);

        v1 = ((EditText)findViewById(R.id.reg_fullnm));
        v2 = ((EditText)findViewById(R.id.reg_dob));
        v3 = ((EditText)findViewById(R.id.reg_add));
        v4 = ((EditText)findViewById(R.id.reg_city));
        v5 = ((EditText)findViewById(R.id.reg_state));
        v6 = ((EditText)findViewById(R.id.reg_mob));
        v7 = ((EditText)findViewById(R.id.reg_altermob));
        v8 = ((EditText)findViewById(R.id.reg_parents_mob1));
        v9 = ((EditText)findViewById(R.id.reg_parents_mob2));
        v10 = ((EditText)findViewById(R.id.reg_pwd));
        v11  = ((EditText)findViewById(R.id.reg_confirm_pwd));


        db = FirebaseFirestore.getInstance();
        reg_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v10.getText().toString().equals(v11.getText().toString())){

                    HashMap<String,String> user = new HashMap<>();
                    user.put("fnm",v1.getText().toString());
                    user.put("dob",v2.getText().toString());
                    user.put("add",v3.getText().toString());
                    user.put("city",v4.getText().toString());
                    user.put("state",v5.getText().toString());
                    user.put("mob",v6.getText().toString());
                    user.put("amob",v7.getText().toString());
                    user.put("pmob",v8.getText().toString());
                    user.put("pmob1",v9.getText().toString());
                    user.put("pwd",v10.getText().toString());

                    db.collection("userDetails").document(""+v6.getText().toString()).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignupActivity.this, "Successfully Registered...!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent  = new Intent(SignupActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(SignupActivity.this, "Password and Confirm password are not same..!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}