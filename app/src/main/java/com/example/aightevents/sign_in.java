package com.example.aightevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ncorti.slidetoact.SlideToActView;

public class sign_in extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        SlideToActView sta = (SlideToActView) findViewById(R.id.swipeSIstd);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                startActivity(new Intent(sign_in.this,home_page.class));
                finish();
            }
        });
    }
    public void backToHome(View backHome){
        startActivity(new Intent(this,StartPage.class));
    }
}