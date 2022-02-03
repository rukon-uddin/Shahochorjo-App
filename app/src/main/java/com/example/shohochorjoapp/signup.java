package com.example.shohochorjoapp;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String APP_ID = "application-0-zgmij";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup.
     */
    // TODO: Rename and change types and number of parameters
    public static signup newInstance(String param1, String param2) {
        signup fragment = new signup();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        EditText userName_et, email_et, password_et, C_password_et;
        AppCompatButton signupButton_bt;
        String email, password, cPassword;

        userName_et = view.findViewById(R.id.editText5);
        email_et = view.findViewById(R.id.editText3);
        password_et = view.findViewById(R.id.editText4);
        C_password_et = view.findViewById(R.id.editText6);
        signupButton_bt = view.findViewById(R.id.appCompatButton4);


        signupButton_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, cPassword;
                email = email_et.getText().toString();
                password = password_et.getText().toString();

                App app = new App(new AppConfiguration.Builder(APP_ID).build());
                User user = app.currentUser();

                app.getEmailPassword().registerUserAsync(email, password, it -> {
                    if (it.isSuccess()){
                        Log.i("rukon", "successfully registered");
                        Toast.makeText(getActivity().getApplicationContext(), "Successfully registered!\nplease login now", Toast.LENGTH_LONG).show();
                    }else{
                        Log.e("EXAMPLE", "Failed to register user: " + it.getError().getErrorMessage());
                    }
                });

            }
        });


        return view;
    }
}