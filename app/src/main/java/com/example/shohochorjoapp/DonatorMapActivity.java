package com.example.shohochorjoapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shohochorjoapp.databinding.ActivityDonatorMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DonatorMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap, map;
    private ActivityDonatorMapBinding binding;
    private Button msetLocation;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE  = 99;
    private boolean permissionDenied = false;
    private static final String APP_ID = "application-0-zgmij";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDonatorMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        msetLocation = (Button) findViewById(R.id.donatemap_setLocation);



        if (ContextCompat.checkSelfPermission(DonatorMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        App app = new App(new AppConfiguration.Builder(APP_ID).build());
        Geocoder geocoder = new Geocoder(DonatorMapActivity.this, Locale.getDefault());
        msetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
                String lat = String.valueOf(latitude);
                String lon = String.valueOf(longitude);
                String lat_long = lat + " " + lon;
                Log.i("Rukon", lat_long);
//                User user = app.currentUser();
//                mongoClient = user.getMongoClient("mongodb-atlas");
//                mongoDatabase = mongoClient.getDatabase("DonatorLocations");
//                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("lat_long");
//
//                String lat_long = lat + " " + lon;
//
//                mongoCollection.insertOne(new Document("userid", user.getId()).append("latitude and longitude", lat_long)).getAsync(result ->{
//                    if(result.isSuccess()){
//                        Toast.makeText(DonatorMapActivity.this, "Location registered successfully", Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(DonatorMapActivity.this, "Please try again", Toast.LENGTH_LONG).show();
//                    }
//                });
                String locationAddress = "";
                try {
                    List<Address> addresses  = geocoder.getFromLocation(latitude,longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String zip = addresses.get(0).getPostalCode();
                    String country = addresses.get(0).getCountryName();

                    locationAddress = address + "," + state + ","+ city + "," + country + " " + zip;
                    Intent intent = new Intent(DonatorMapActivity.this, DonatorInfoActivity.class);
                    intent.putExtra("location", locationAddress);
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lon);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(DonatorMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DonatorMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(DonatorMapActivity.this).setMessage("Required permission for location").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(DonatorMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(DonatorMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionDenied = true;

            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(DonatorMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {
                    permissionDenied = true;
                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateLocationUI();

        getDeviceLocation();


        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
    }

    private void updateLocationUI(){
        if(map==null){
            return;
        }
        try{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getDeviceLocation(){
        try{

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        lastKnownLocation = task.getResult();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
                    }else{
                        Toast.makeText(DonatorMapActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



}