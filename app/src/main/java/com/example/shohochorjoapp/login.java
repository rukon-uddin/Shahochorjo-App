package com.example.shohochorjoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String APP_ID = "application-0-zgmij";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static login newInstance(String param1, String param2) {
        login fragment = new login();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        AppCompatButton loginButton_bt;
        EditText email_et, password_et;
        TextView forgot_pass_tv;



        loginButton_bt = (AppCompatButton) view.findViewById(R.id.appCompatButton4);
        email_et = (EditText) view.findViewById(R.id.editText);
        password_et = (EditText) view.findViewById(R.id.editText2);
        forgot_pass_tv = (TextView) view.findViewById(R.id.forgotPassword_tv);

        email_et.setTranslationX(300);
        password_et.setTranslationX(300);
        loginButton_bt.setTranslationX(300);
        forgot_pass_tv.setTranslationX(300);

        float v = 0;

        email_et.setAlpha(v);
        password_et.setAlpha(v);
        loginButton_bt.setAlpha(v);
        forgot_pass_tv.setAlpha(v);

        email_et.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password_et.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        loginButton_bt.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        forgot_pass_tv.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        App app = new App(new AppConfiguration.Builder(APP_ID).build());
        loginButton_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                Credentials credentials = Credentials.emailPassword(email, password);
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if (result.isSuccess()){
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }else {
                            Toast.makeText(getActivity().getApplicationContext(), "Password or email is wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}