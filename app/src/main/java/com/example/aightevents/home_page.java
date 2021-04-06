package com.example.aightevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class home_page extends AppCompatActivity {

    MeowBottomNavigation meow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        meow = (MeowBottomNavigation) findViewById(R.id.bottom_nav);
        meow.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        meow.add(new MeowBottomNavigation.Model(2,R.drawable.search));
        meow.add(new MeowBottomNavigation.Model(3,R.drawable.profile));
    }
}