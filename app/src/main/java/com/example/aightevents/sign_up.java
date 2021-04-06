package com.example.aightevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ncorti.slidetoact.SlideToActView;

public class sign_up extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        SlideToActView sta = (SlideToActView) findViewById(R.id.swipeSU);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                startActivity(new Intent(sign_up.this,home_page.class));
                finish();
            }
        });
    }

    public void backToHome(View backHome) {
        startActivity(new Intent(this,StartPage.class));
    }

    public void signUp(View backHome) {
        startActivity(new Intent(this,sign_in.class));
    }

}