package com.example.aightevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

public class select_filter extends AppCompatActivity {

    RadioButton todayRb;
    RadioButton thisWeekRb;
    PowerSpinnerView collegeFil;
    PowerSpinnerView categoryFil;
    Filter filter;
    String time;
    String collegeStr;
    String categoryStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_filter);
        getSupportActionBar().hide();

        todayRb = findViewById(R.id.todayFilter);
        thisWeekRb = findViewById(R.id.thisWeekFilter);
        collegeFil = findViewById(R.id.collegeFilter);
        categoryFil = findViewById(R.id.categoryFilter);


        collegeFil.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {

            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable String s, int i1, String t1) {
                collegeStr = t1;
            }
        });

        categoryFil.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                categoryStr = newItem;
            }
        });
    }

    public void applyFilter(View view){


        if(todayRb.isChecked()){
            time = "today";
        }
        else if(thisWeekRb.isChecked()){
            time = "thisWeek";
        }

        filter = Filter.getInstance();
        filter.setCategory(categoryStr);
        filter.setCollege(collegeStr);
        filter.setTime(time);
        filter.setFlag(1);
        finish();
    }

}