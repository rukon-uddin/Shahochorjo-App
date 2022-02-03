package com.example.shohochorjoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.shohochorjoapp.databinding.ActivityAngelMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AngelMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityAngelMapBinding binding;
    private static final String APP_ID = "application-0-zgmij";
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE_FOR_FINE_LOCATION = 1234;
    private boolean PERMISSION = false;
    Location angelLocation = new Location("");
    Location donatorLocation = new Location("");
    Location location;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAngelMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(AngelMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();

        } else {
            requestPermission();
        }
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(AngelMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AngelMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(AngelMapActivity.this).setMessage("Required permission for location").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(AngelMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(AngelMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
            }

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FOR_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PERMISSION = true;

            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(AngelMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {
                    PERMISSION = true;
                }

            }
        }
    }

    public void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                location = task.getResult();
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                LatLng latLng = new LatLng(lat, lon);
                try{
                    angelLocation.setLatitude(lat);
                    angelLocation.setLongitude(lon);
                }catch (Exception e){
                    Log.e("myError", ""+e);
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.75f));
            }
        });


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final String[] lat = new String[1];
        final String[] lon = new String[1];
        final String[] deviceToken = new String[1];
        Map<String, String> tokens = new HashMap<String, String>();
        final String[] sendRequestToken = new String[1];

        try{
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        MongoDatabase mongoDatabase2;
        MongoClient mongoClient2;
        App app = new App(new AppConfiguration.Builder(APP_ID).build());
        User user = app.currentUser();

        mongoClient2 = user.getMongoClient("mongodb-atlas");
        mongoDatabase2 = mongoClient2.getDatabase("DonatorLocations");
        MongoCollection<Document> mongoCollection2 = mongoDatabase2.getCollection("lat_long");
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection2.find().iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()){
                MongoCursor<Document> results = task.get();
                while (results.hasNext()){
                    Document currentDoc = results.next();
                    lat[0] = currentDoc.getString("latitude");
                    lon[0] = currentDoc.getString("longitude");
                    deviceToken[0] = currentDoc.getString("token");

                    String latlon = lat[0]+lon[0];
                    tokens.put(deviceToken[0], latlon);

                    double lati = Double.parseDouble(lat[0]);
                    double lonn = Double.parseDouble(lon[0]);
                    try{
                        donatorLocation.setLatitude(lati);
                        donatorLocation.setLongitude(lonn);
//                        double distance = angelLocation.distanceTo(donatorLocation);
//                        if(distance < 2000){
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lati,lonn)).icon(BitmapDescriptorFactory.fromResource(R.drawable.foodmarker)));
//                        }
                    }catch (Exception e){
                        Log.e("myError", ""+e);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lati,lonn)).icon(BitmapDescriptorFactory.fromResource(R.drawable.foodmarker)));
                    }

//                    Log.v("Rukon", currentDoc.getString("userid"));
                }

            }else{
                Log.v("Rukon", task.getError().toString());

            }
        });

        dialog = new Dialog(AngelMapActivity.this);
        dialog.setContentView(R.layout.donator_info_popup);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.popupAnimation;

        AppCompatButton ok = dialog.findViewById(R.id.btn_okay);
        AppCompatButton cancel = dialog.findViewById(R.id.btn_cancel);
        String title = "Angel Request";
        String message = "An angel has requested for your food Do you want to donate it now?";

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Rukon", sendRequestToken[0]);
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(sendRequestToken[0], title, message, getApplicationContext(), AngelMapActivity.this);
                notificationsSender.SendNotifications();
                Toast.makeText(AngelMapActivity.this, "A request has been sent please wait", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markertitle = "Angel";
                LatLng position = marker.getPosition();
                double markerlat = position.latitude;
                double markerlon = position.longitude;
                String latlon = String.valueOf(markerlat)+String.valueOf(markerlon);
                Log.v("Rukon", latlon);
                for(String token: tokens.keySet()){
                    String t = tokens.get(token);
                    if (t.equals(latlon)){
                        sendRequestToken[0] = token;
                    }
                }

                dialog.show();

                return false;
            }
        });

    }
}