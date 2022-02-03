package com.example.shohochorjoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import io.realm.Realm;

public class SigninSignupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter fragmentAdapter;
    FloatingActionButton fb, google, tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        tabLayout = findViewById(R.id.tabLayout);
        pager2 = findViewById(R.id.viewPager2);

        fb = findViewById(R.id.Facebook_floatingActionButton);
        google = findViewById(R.id.floatingActionButton);
        tweet = findViewById(R.id.floatingActionButton2);

        FragmentManager fm = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Sign in"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

//        fb.setTranslationY(300);
//        google.setTranslationY(300);
//        tweet.setTranslationY(300);
//        tabLayout.setTranslationY(300);

        fb.setTranslationX(300);
        google.setTranslationX(300);
        tweet.setTranslationX(300);
        tabLayout.setTranslationX(300);
        float v = 0;

        fb.setAlpha(v);
        google.setAlpha(v);
        tweet.setAlpha(v);
        tabLayout.setAlpha(v);

/*        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tweet.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();*/

        fb.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tweet.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();


    }
}