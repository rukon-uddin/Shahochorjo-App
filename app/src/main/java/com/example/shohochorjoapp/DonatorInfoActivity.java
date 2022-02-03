package com.example.shohochorjoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.bson.Document;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DonatorInfoActivity extends AppCompatActivity {

    TimePicker timePicker_tp;
    Button setTime_bt, setLocation_bt;
    TextView time_view_tv;
    EditText location_viewer;
    Button donator_bt;
    TextInputLayout name_tiet;
    TextInputLayout phone_number_tiet;
    TextInputLayout food_quantity_tiet;

    private static final String APP_ID = "application-0-zgmij";
    MongoDatabase mongoDatabase, locationDatabase;
    MongoClient mongoClient, locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_info);
        timePicker_tp = findViewById(R.id.timePicker);
        setTime_bt = findViewById(R.id.timeSet);
        time_view_tv = findViewById(R.id.textView9);
        setLocation_bt = findViewById(R.id.Button_setLocation);
        location_viewer = findViewById(R.id.editText7);
        donator_bt = findViewById(R.id.appCompatButton5);

        AtomicReference<String> deviceToken = new AtomicReference<>("");

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                deviceToken.set(token);
                Log.i("Rukon", "retrieve token successful : " + deviceToken);
            } else{
                Log.i("Rukon", "token should not be null...");
            }
        }).addOnFailureListener(ex -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task -> {

        });




        App app = new App(new AppConfiguration.Builder(APP_ID).build());


        Intent intent = getIntent();
        String location_address = intent.getStringExtra("location");
        String lat = intent.getStringExtra("latitude");
        String lon = intent.getStringExtra("longitude");
        try {
            location_viewer.setText(location_address);

        }catch (Exception e){

        }


        setTime_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker_tp.getHour();
                    minute = timePicker_tp.getMinute();
                }
                else{
                    hour = timePicker_tp.getCurrentHour();
                    minute = timePicker_tp.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                String t = hour + ":" + minute + " " + am_pm;
                time_view_tv.setText(t);
            }
        });

        setLocation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonatorInfoActivity.this, DonatorMapActivity.class));
            }
        });


        donator_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = app.currentUser();
                Document filter = new Document()
                        .append("userid", new Document().append("$eq", user.getId()));

                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("DonatorLocations");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("lat_long");

                Log.i("Rukon", "Searching");

                mongoCollection.findOne(filter).getAsync(result -> {
                    if(result.isSuccess()){
                        Log.i("Rukon", "FOUND!!");
                        try{
                            Document data = result.get();
                            Log.i("Rukon", data.getString("latitude"));
                            Toast.makeText(getApplication().getApplicationContext(), "Data already inserted", Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
//                            Log.i("Rukon", "Not FOUND :{");
                            name_tiet = findViewById(R.id.outlinedTextField);
                            phone_number_tiet = findViewById(R.id.textInputLayout);
                            food_quantity_tiet = findViewById(R.id.textInputLayout2);
                            time_view_tv = findViewById(R.id.textView9);


                            String name = name_tiet.getEditText().getText().toString();
                            String phone_number = phone_number_tiet.getEditText().getText().toString();
                            String food_quantity = food_quantity_tiet.getEditText().getText().toString();
                            String time = time_view_tv.getText().toString();

                            locationClient = user.getMongoClient("mongodb-atlas");
                            locationDatabase = locationClient.getDatabase("DonatorLocations");
                            MongoCollection<Document> locationCollection = locationDatabase.getCollection("lat_long");
                            Document locations = new Document("userid", user.getId());
                            locations.append("latitude", lat);
                            locations.append("longitude", lon);
                            locations.append("token", deviceToken.toString());
                            locationCollection.insertOne(locations).getAsync(r -> {
                                if(r.isSuccess()){
                                    Toast.makeText(DonatorInfoActivity.this, "Latitude longitude inserted", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(DonatorInfoActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                                }

                            });

                            mongoClient = user.getMongoClient("mongodb-atlas");
                            mongoDatabase = mongoClient.getDatabase("UserInformation");
                            MongoCollection<Document> mongoCollection1 = mongoDatabase.getCollection("Users");
                            Document doc = new Document("userid", user.getId());
                            doc.append("Name", name);
                            doc.append("PhoneNumber", phone_number);
                            doc.append("FoodQuantity", food_quantity);
                            doc.append("Time", time);
                            doc.append("Location", location_address);
                            mongoCollection1.insertOne(doc).getAsync(r -> {
                                if(r.isSuccess()){
                                    Toast.makeText(DonatorInfoActivity.this, "Location registered successfully", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(DonatorInfoActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

//                        Toast.makeText(getApplication().getApplicationContext(), "Item found", Toast.LENGTH_LONG);
                    }else{
                        Log.i("Rukon", "Not FOUND :{");
//                        Toast.makeText(getApplication().getApplicationContext(), "Item not found", Toast.LENGTH_LONG);
                    }
                });

//                checkDatabase(app, "userid", lat, lon, location_address);
            }
        });

    }

//    public void checkDatabase(App app, String id, String lat, String lon, String location_address){
//        User user = app.currentUser();
//        Document queryFilter = new Document();
//        queryFilter.append(id, user.getId());
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("DonatorLocations");
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("lat_long");
//        mongoCollection.findOne(queryFilter).getAsync(result -> {
//            if (result.isSuccess()){
//                Toast.makeText(DonatorInfoActivity.this, "Id found", Toast.LENGTH_LONG).show();
//
//            }else {
//
//                insert_Data(app, lat, lon, location_address);
//
//            }
//        });
//    }
//
//
//    public void insert_Data(App app, String lat, String lon, String location_address){
//
//        name_tiet = findViewById(R.id.outlinedTextField);
//        phone_number_tiet = findViewById(R.id.textInputLayout);
//        food_quantity_tiet = findViewById(R.id.textInputLayout2);
//        time_view_tv = findViewById(R.id.textView9);
//
//        String name = name_tiet.getEditText().getText().toString();
//        String phone_number = phone_number_tiet.getEditText().getText().toString();
//        String food_quantity = food_quantity_tiet.getEditText().getText().toString();
//        String time = time_view_tv.getText().toString();
//
//        User user = app.currentUser();
//
//
//        locationClient = user.getMongoClient("mongodb-atlas");
//        locationDatabase = locationClient.getDatabase("DonatorLocations");
//        MongoCollection<Document> locationCollection = locationDatabase.getCollection("lat_long");
//        Document locations = new Document("userid", user.getId());
//        locations.append("latitude", lat);
//        locations.append("longitude", lon);
//        locationCollection.insertOne(locations).getAsync(result -> {
//            if(result.isSuccess()){
//                Toast.makeText(DonatorInfoActivity.this, "Latitude longitude inserted", Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(DonatorInfoActivity.this, "Please try again", Toast.LENGTH_LONG).show();
//            }
//
//        });
//
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("UserInformation");
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
//        Document doc = new Document("userid", user.getId());
//        doc.append("Name", name);
//        doc.append("PhoneNumber", phone_number);
//        doc.append("FoodQuantity", food_quantity);
//        doc.append("Time", time);
//        doc.append("Location", location_address);
//        mongoCollection.insertOne(doc).getAsync(result -> {
//            if(result.isSuccess()){
//                Toast.makeText(DonatorInfoActivity.this, "Location registered successfully", Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(DonatorInfoActivity.this, "Please try again", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
}