package com.kosec.hersafety;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class AddSecurity extends AppCompatActivity {

    TextView cnt_tv,cnm_tv,sec,lgout,home;
    Button save_cnt;
    FirebaseFirestore db;
    String myno;
    TextView profile;


    @SuppressLint({"MissingInflatedId", "HardwareIds", "WorldReadableFiles", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_security);
        db = FirebaseFirestore.getInstance();
        cnm_tv = findViewById(R.id.contact_name);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSecurity.this,ProfilePage.class);
                startActivity(intent);
            }
        });


        String usernm = "";
        try{
            FileInputStream fin = openFileInput("login.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while((a=fin.read())!=-1){
                temp.append((char)a);
            }
            usernm = temp.toString();
        } catch(Exception e){
            Toast.makeText(AddSecurity.this, ""+e, Toast.LENGTH_SHORT).show();
        }

        db.collection("userNumbers").document(usernm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String phns = Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("phns")).toString();
                    ArrayList<String> contacts = new ArrayList<>(Arrays.asList(phns.split(",")));
                    for(String cc : contacts){
                        cnt_tv.append(cc+"\n");
                    }

                    String names = Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("names")).toString();
                    ArrayList<String> nl = new ArrayList<>(Arrays.asList(names.split(",")));
                    for(String cc : nl){
                        cnm_tv.append(cc+"\n");
                    }


                }
            }
        });

        sec = findViewById(R.id.home_tab);
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
                    Toast.makeText(AddSecurity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(AddSecurity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSecurity.this,HomePage.class);
                startActivity(intent);
            }
        });

        cnt_tv = findViewById(R.id.contact_list);
        save_cnt = findViewById(R.id.save_cnt);
        save_cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernm = "";
                try{
                    FileInputStream fin = openFileInput("login.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while((a=fin.read())!=-1){
                        temp.append((char)a);
                    }
                    usernm = temp.toString();
                } catch(Exception e){
                    Toast.makeText(AddSecurity.this, ""+e, Toast.LENGTH_SHORT).show();
                }

                HashMap<String,String> map = new HashMap<>();
                String data = (cnt_tv.getText().toString()).replace("\n",",");
                String data1 = (cnm_tv.getText().toString()).replace("\n",",");
                data = data.substring(0,data.length());
                data1 = data1.substring(0,data1.length());

                map.put("phns",data);
                map.put("names",data1);
                db.collection("userNumbers").document(usernm).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddSecurity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSecurity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        
    }
    public void chooseContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            Uri contactUri = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    int numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = phoneCursor.getString(numberIndex);
                    phoneCursor.close();
                    cursor.close();
                    number = number.replace("-", "");
                    if (number.length() == 10) {
                        number = "+91" + number;
                    }
                    cnt_tv.append(number+"\n");


                    Uri contactUri1 = data.getData();
                    String[] projection1 = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                    @SuppressLint("Recycle") Cursor cursor1 = getContentResolver().query(contactUri1, projection1, null, null, null);
                    String name = null;
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        int index = cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        name = cursor1.getString(index);
                        cursor1.close();
                    }
                    cnm_tv.append(name+"\n");


                }





            }
        }
    }
}