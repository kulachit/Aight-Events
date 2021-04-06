package com.example.aightevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start_page);
    }

    public void studentLogin (View studentlogin) {
        startActivity(new Intent(StartPage.this,sign_up.class));
    }

    public void orgLogin (View orglogin) {
        startActivity(new Intent(StartPage.this,sign_in_org.class));
    }

}