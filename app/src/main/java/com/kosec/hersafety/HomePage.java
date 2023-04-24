package com.kosec.hersafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements  LocationListener{


    TextView locv,sec,lgout,name,phn,liveloc,profile;
    String ppp;
    Address address;
    public String mainaddress;
    LocationManager lmng;
    WebView webview;
    double lat, lng;
    Button sendloc,sendliveloc;
    FirebaseFirestore db;
    FusedLocationProviderClient fusedLocationClient;

    @SuppressLint({"MissingPermission", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.user_name);
        sendliveloc = findViewById(R.id.sendliveloc);
        sendliveloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestLocation();
            }
        });
        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,ProfilePage.class);
                startActivity(intent);
            }
        });
        webview = findViewById(R.id.webview);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setGeolocationEnabled(true);
        webview.loadUrl("https://www.google.com/maps/");

        name.append("You Have Logged in as\n");
        phn = findViewById(R.id.user_phone);

        try{
            FileInputStream fin = openFileInput("login.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            ppp = temp.toString();
            phn.setText(ppp);

        } catch(Exception e) { Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show(); }

        db.collection("userDetails").document(ppp).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                name.append(Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("fnm")).toString());
            }
        });
        locv = findViewById(R.id.curloc_tv);
        liveloc = findViewById(R.id.liveloc);
        getLocation();
        sendloc = findViewById(R.id.sendloc);
        sec = findViewById(R.id.security_tab);
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
                    Toast.makeText(HomePage.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(HomePage.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,AddSecurity.class);
                startActivity(intent);
            }
        });
        sendloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocation();
            }
        });

    }
    @SuppressLint("MissingPermission")
    public void RequestLocation(){
        if(lmng==null){
            lmng = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        if(lmng.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lmng.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,1000,this);
        }
    }
    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    public void onLocationChanged(@NonNull Location location) {
        liveloc.setText("Location : "+location.getLongitude()+", "+location.getLatitude());
        sendsos("https://www.google.com/maps/@"+location.getLatitude()+","+location.getLongitude()+",17z");
    }

    @SuppressLint("MissingPermission")
    private void sendLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    lat = latitude;
                    lng = longitude;

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            address = addresses.get(0);
                            mainaddress = address.getAddressLine(0);
                            locv.setText("" + longitude + ", " + latitude + "\nLocation : " + mainaddress);
                            liveloc.setText("" + longitude + ", " + latitude + "\nLocation : " + mainaddress);
                            sendsos("https://www.google.com/maps/@"+latitude+","+longitude+",17z");
                            webview = findViewById(R.id.webview);
                            webview.getSettings().setLoadsImagesAutomatically(true);
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            webview.getSettings().setGeolocationEnabled(true);
                            webview.loadUrl("https://www.google.com/maps/@"+latitude+","+longitude+",17z");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    protected void getLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    lat = latitude;
                    lng = longitude;

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            address = addresses.get(0);
                            mainaddress = address.getAddressLine(0);
                            locv.setText("" + longitude + ", " + latitude + "\nLocation : " + mainaddress);
                            liveloc.setText("" + longitude + ", " + latitude + "\nLocation : " + mainaddress);
                            webview = findViewById(R.id.webview);
                            webview.getSettings().setLoadsImagesAutomatically(true);
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            webview.getSettings().setGeolocationEnabled(true);
                            webview.loadUrl("https://www.google.com/maps/@"+latitude+","+longitude+",17z");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void sendsos(String sms) {

        String userphn="";
        ArrayList<String> contacts = new ArrayList<>();
        try{
            FileInputStream fin = openFileInput("login.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            userphn = temp.toString();

        } catch(Exception e) { Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show(); }

        db.collection("userNumbers").document(userphn).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String phns = Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("phns")).toString();
                    Toast.makeText(HomePage.this, ""+phns, Toast.LENGTH_SHORT).show();
                    contacts.addAll(Arrays.asList(phns.split(",")));
                    Toast.makeText(HomePage.this, ""+contacts, Toast.LENGTH_SHORT).show();
                            SmsManager smsManager = SmsManager.getDefault();
                            for (String phn : contacts) {
                                smsManager.sendTextMessage(phn, null, "" + sms, null, null);
                            }
                    Toast.makeText(HomePage.this, "SOS Alert send", Toast.LENGTH_SHORT).show();

                }
            }
        });

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setGeolocationEnabled(true);
        webview.loadUrl(sms);
        Toast.makeText(this, ""+sms, Toast.LENGTH_SHORT).show();

    }


}