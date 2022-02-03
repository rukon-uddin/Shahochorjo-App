package com.example.shohochorjoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String APP_ID = "application-0-zgmij";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_home newInstance(String param1, String param2) {
        Home_home fragment = new Home_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // ******************************This block of code finds all the userids*****************
//        MongoDatabase mongoDatabase2;
//        MongoClient mongoClient2;
//        App app = new App(new AppConfiguration.Builder(APP_ID).build());
//        User user = app.currentUser();
//
//        mongoClient2 = user.getMongoClient("mongodb-atlas");
//        mongoDatabase2 = mongoClient2.getDatabase("DonatorLocations");
//        MongoCollection<Document> mongoCollection2 = mongoDatabase2.getCollection("lat_long");
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection2.find().iterator();
//        findTask.getAsync(task -> {
//            if (task.isSuccess()){
//                MongoCursor<Document> results = task.get();
//                while (results.hasNext()){
//                    Document currentDoc = results.next();
//                    Log.v("Rukon", currentDoc.getString("userid"));
//
//                }
//
//            }else{
//                Log.v("Rukon", task.getError().toString());
//
//            }
//        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_home, container, false);
        CardView donateFood_bt, findFood_bt;

        donateFood_bt = view.findViewById(R.id.cardView);
        findFood_bt = view.findViewById(R.id.cardView2);

        donateFood_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), DonatorInfoActivity.class));
            }
        });

        findFood_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AngelMapActivity.class));
            }
        });


        return view;
    }

}

